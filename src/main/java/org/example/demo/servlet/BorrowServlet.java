package org.example.demo.servlet;

import org.example.demo.dao.*;
import org.example.demo.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/librarian/borrow")
public class BorrowServlet extends HttpServlet {
    private LoanSlipDAO loanSlipDAO = new LoanSlipDAO();
    private LoanSlipDetailDAO detailDAO = new LoanSlipDetailDAO();
    private DocumentCopyDAO copyDAO = new DocumentCopyDAO();
    private ReaderCardDAO readerCardDAO = new ReaderCardDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String lookupBarcode = request.getParameter("lookupBarcode");
        if (lookupBarcode != null && !lookupBarcode.trim().isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            try {
                DocumentCopy copy = copyDAO.findByCode(lookupBarcode.trim());
                if (copy == null) {
                    response.getWriter().write("{\"success\":false,\"message\":\"Không tìm thấy bản sao\"}");
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("{\"success\":true,\"data\":{");
                sb.append("\"barcode\":\"").append(escape(copy.getBarcode())).append("\",");
                sb.append("\"status\":\"").append(escape(copy.getStatus())).append("\",");
                if (copy.getDocument() != null) {
                    sb.append("\"document\":{");
                    sb.append("\"id\":")
                            .append(copy.getDocument().getId() == null ? "null" : copy.getDocument().getId())
                            .append(",");
                    sb.append("\"ISBN\":\"").append(escape(copy.getDocument().getISBN())).append("\",");
                    sb.append("\"name\":\"").append(escape(copy.getDocument().getName())).append("\",");
                    sb.append("\"author\":\"").append(escape(copy.getDocument().getAuthor())).append("\"");
                    sb.append("}");
                } else {
                    sb.append("\"document\":null");
                }
                sb.append("}}");
                response.getWriter().write(sb.toString());
                return;
            } catch (Exception ex) {
                ex.printStackTrace();
                response.getWriter().write("{\"success\":false,\"message\":\"Lỗi server\"}");
                return;
            }
        }

        String readerCode = request.getParameter("readerCode");

        if (readerCode != null && !readerCode.isEmpty()) {
            ReaderCard readerCard = readerCardDAO.findByCode(readerCode);
            request.setAttribute("readerCard", readerCard);

            if (readerCard != null) {
                request.getSession().setAttribute("borrowReaderCard", readerCard);
            } else {
                request.getSession().removeAttribute("borrowReaderCard");
            }
        }

        request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
    }

    private String escape(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String readerCode = request.getParameter("readerCode");
            String[] copyCodes = request.getParameterValues("copyCodes");

            ReaderCard readerCard = (ReaderCard) request.getSession().getAttribute("borrowReaderCard");
            if (readerCard != null) {
                if (readerCode != null && !readerCode.isEmpty() && !readerCode.equals(readerCard.getCardId())) {
                    readerCard = readerCardDAO.findByCode(readerCode);
                    if (readerCard != null)
                        request.getSession().setAttribute("borrowReaderCard", readerCard);
                }
            } else {
                if (readerCode != null && !readerCode.isEmpty()) {
                    readerCard = readerCardDAO.findByCode(readerCode);
                    if (readerCard != null)
                        request.getSession().setAttribute("borrowReaderCard", readerCard);
                }
            }
            if (readerCard == null) {
                request.setAttribute("error", "Không tìm thấy bạn đọc!");
                request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
                return;
            }

            if (copyCodes == null || copyCodes.length == 0) {
                request.setAttribute("error", "Vui lòng nhập mã bản sao tài liệu cần mượn!");
                request.setAttribute("readerCard", readerCard);
                request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
                return;
            }

            List<String> invalidCodes = new ArrayList<>();
            List<DocumentCopy> copiesToBorrow = new ArrayList<>();
            for (String rawCode : copyCodes) {
                if (rawCode == null)
                    continue;
                String code = rawCode.trim();
                if (code.isEmpty())
                    continue;

                DocumentCopy copy = null;
                String[] foundBarcodes = request.getParameterValues("foundBarcode");
                String[] foundStatuses = request.getParameterValues("foundStatus");
                String[] foundDocIds = request.getParameterValues("foundDocId");
                String[] foundDocNames = request.getParameterValues("foundDocName");
                String[] foundDocAuthors = request.getParameterValues("foundDocAuthor");
                int idx = -1;
                for (int j = 0; j < copyCodes.length; j++) {
                    if (copyCodes[j] != null && copyCodes[j].trim().equals(code)) {
                        idx = j;
                        break;
                    }
                }
                if (idx >= 0 && foundBarcodes != null && idx < foundBarcodes.length && foundBarcodes[idx] != null
                        && foundBarcodes[idx].equals(code)) {
                    String status = (foundStatuses != null && idx < foundStatuses.length) ? foundStatuses[idx] : null;
                    String docIdS = (foundDocIds != null && idx < foundDocIds.length) ? foundDocIds[idx] : null;
                    String docName = (foundDocNames != null && idx < foundDocNames.length) ? foundDocNames[idx] : null;
                    String docAuthor = (foundDocAuthors != null && idx < foundDocAuthors.length) ? foundDocAuthors[idx]
                            : null;
                    Document doc = null;
                    try {
                        if (docIdS != null && !docIdS.trim().isEmpty()) {
                            doc = new Document();
                            doc.setId(Integer.parseInt(docIdS));
                        }
                    } catch (NumberFormatException ignore) {
                    }
                    if (doc == null && (docName != null || docAuthor != null)) {
                        doc = new Document();
                    }
                    if (doc != null) {
                        if (docName != null && !docName.isEmpty())
                            doc.setName(docName);
                        if (docAuthor != null && !docAuthor.isEmpty())
                            doc.setAuthor(docAuthor);
                    }
                    copy = new DocumentCopy();
                    copy.setBarcode(code);
                    copy.setStatus(status);
                    copy.setDocument(doc);
                } else {
                    copy = copyDAO.findByCode(code);
                }
                if (copy == null) {
                    invalidCodes.add(code);
                } else if (!"AVAILABLE".equalsIgnoreCase(copy.getStatus())) {
                    invalidCodes.add(code + " (không khả dụng)");
                } else {
                    copiesToBorrow.add(copy);
                }
            }

            if (!invalidCodes.isEmpty()) {
                request.setAttribute("error",
                        "Một số mã bản sao không hợp lệ hoặc không khả dụng: " + String.join(", ", invalidCodes));
                request.setAttribute("readerCard", readerCard);

                List<DocumentCopy> copyDetails = new ArrayList<>();
                if (copyCodes != null) {
                    for (String rawCode : copyCodes) {
                        if (rawCode == null) {
                            copyDetails.add(null);
                            continue;
                        }
                        String code = rawCode.trim();
                        if (code.isEmpty()) {
                            copyDetails.add(null);
                            continue;
                        }
                        try {
                            DocumentCopy c = copyDAO.findByCode(code);
                            copyDetails.add(c);
                        } catch (Exception e) {
                            copyDetails.add(null);
                        }
                    }
                }
                request.setAttribute("copyDetails", copyDetails);
                request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
                return;
            }

            LoanSlip loanSlip = new LoanSlip();
            loanSlip.setReader(readerCard.getReader());
            loanSlip.setBorrowDate(LocalDateTime.now());
            loanSlip.setDueDate(LocalDateTime.now().plusDays(14));
            loanSlip.setNotes(request.getParameter("notes"));
            boolean inserted = loanSlipDAO.insert(loanSlip);

            if (!inserted) {
                request.setAttribute("error", "Không thể tạo phiếu mượn (DB error)");
                request.setAttribute("readerCard", readerCard);
                request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
                return;
            }

            for (DocumentCopy copy : copiesToBorrow) {
                LoanSlipDetail detail = new LoanSlipDetail();
                detail.setLoanSlip(loanSlip);
                detail.setDocumentCopy(copy);

                if (detailDAO.insert(detail)) {
                    copyDAO.updateStatus(copy.getBarcode(), "BORROWED");
                }
            }

            List<LoanSlipDetail> details = detailDAO.findByLoanSlipId(loanSlip.getId());
            request.setAttribute("success", "Cho mượn thành công!");
            request.setAttribute("loanSlip", loanSlip);
            request.setAttribute("details", details);
            request.setAttribute("readerCard", readerCard);

            request.getSession().removeAttribute("borrowReaderCard");
            request.getRequestDispatcher("/librarian/borrow-success.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
        }
    }
}
