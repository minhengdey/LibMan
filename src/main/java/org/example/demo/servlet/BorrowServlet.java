package org.example.demo.servlet;

import org.example.demo.dao.*;
import org.example.demo.model.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    private LibrarianDAO librarianDAO = new LibrarianDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String readerCode = request.getParameter("readerCode");

        if (readerCode != null && !readerCode.isEmpty()) {
            ReaderCard readerCard = readerCardDAO.findByCode(readerCode);
            request.setAttribute("readerCard", readerCard);
        }

        request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String readerCode = request.getParameter("readerCode");
            String[] copyCodes = request.getParameterValues("copyCodes");

            ReaderCard readerCard = readerCardDAO.findByCode(readerCode);
            if (readerCard == null) {
                request.setAttribute("error", "Không tìm thấy bạn đọc!");
                request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            int librarianId = (int) session.getAttribute("userId");

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
                DocumentCopy copy = copyDAO.findByCode(code);
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
                request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
                return;
            }

            LoanSlip loanSlip = new LoanSlip();
            loanSlip.setReader(readerCard.getReader());
            loanSlip.setLibrarian(librarianDAO.findLibrarianById(librarianId));
            loanSlip.setBorrowDate(LocalDateTime.now());
            loanSlip.setDueDate(LocalDateTime.now().plusDays(14));
            loanSlip.setStatus("BORROWED");
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
            request.getRequestDispatcher("/librarian/borrow-success.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/librarian/borrow-form.jsp").forward(request, response);
        }
    }
}
