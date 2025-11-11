<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.*, org.example.demo.model.*, java.time.format.DateTimeFormatter" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Phiếu mượn - LibMan</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <div class="container" style="margin-top:2rem; max-width:900px;">
                <div class="card"
                    style="padding: 32px 24px; border-radius: 12px; box-shadow: 0 4px 16px 0 rgba(60,60,60,0.08); background: #fff;">
                    <div class="card-header" style="text-align:center; margin-bottom:2rem;">
                        <h2 class="card-title"
                            style="font-size:2.1rem; font-weight:700; color:#276ef1; letter-spacing:0.02em;">Phiếu mượn
                            tài liệu</h2>
                    </div>

                    <% LoanSlip loanSlip=(LoanSlip) request.getAttribute("loanSlip"); ReaderCard reader=(ReaderCard)
                        request.getAttribute("readerCard"); List<LoanSlipDetail> details = (List<LoanSlipDetail>)
                            request.getAttribute("details");
                            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            if (loanSlip != null) {
                            %>

                            <div
                                style="margin-bottom:1.3rem; background: #f3f7fb; padding:1.1rem 1.5rem; border-radius:7px;">
                                <div
                                    style="display:grid; grid-template-columns:repeat(auto-fit, minmax(230px,1fr)); gap:1.1rem;">
                                    <div><strong>Mã phiếu:</strong> <span style="color:#2f4e7b;">
                                            <%= String.valueOf(loanSlip.getId()) %>
                                        </span></div>
                                    <div><strong>Ngày mượn:</strong> <span style="color:#2f4e7b;">
                                            <%= loanSlip.getBorrowDate() !=null ?
                                                loanSlip.getBorrowDate().toLocalDate().format(df) : "N/A" %>
                                        </span></div>
                                    <div><strong>Hạn trả:</strong> <span style="color:#2f4e7b;">
                                            <%= loanSlip.getDueDate() !=null ?
                                                loanSlip.getDueDate().toLocalDate().format(df) : "N/A" %>
                                        </span></div>
                                </div>
                            </div>

                            <div style="display: flex; gap: 2.5rem; flex-wrap:wrap; margin-bottom:1.3rem;">
                                <div
                                    style="flex:1; min-width:260px; background: #ebf4fb; padding:1.2rem 1.3rem; border-radius:8px;">
                                    <h4 style="margin-top:0; color:#215591; font-weight:600; margin-bottom:0.7rem;">Bạn
                                        đọc</h4>
                                    <div style="margin-bottom:0.5rem;"><strong>Họ tên:</strong>
                                        <%= reader !=null ? reader.getReader().getFullName() : "N/A" %>
                                    </div>
                                    <div style="margin-bottom:0.5rem;"><strong>Mã bạn đọc:</strong>
                                        <%= reader !=null ? reader.getCardId() : "" %>
                                    </div>
                                    <% if (reader !=null && reader.getReader() !=null) { %>
                                        <div style="font-size:0.96em; line-height:1.7;">
                                            <strong>Địa chỉ:</strong>
                                            <%= reader.getReader().getAddress() !=null ? reader.getReader().getAddress()
                                                : "N/A" %><br>
                                                <strong>Ngày sinh:</strong>
                                                <%= reader.getReader().getDob() !=null ?
                                                    reader.getReader().getDob().toLocalDate().format(df) : "N/A" %><br>
                                                    <strong>Email:</strong>
                                                    <%= reader.getReader().getEmail() !=null ?
                                                        reader.getReader().getEmail() : "N/A" %><br>
                                                        <strong>SĐT:</strong>
                                                        <%= reader.getReader().getPhoneNumber() !=null ?
                                                            reader.getReader().getPhoneNumber() : "N/A" %>
                                        </div>
                                        <% } %>
                                </div>
                            </div>

                            <div
                                style="margin-bottom:2rem; background: #fff8df; border-radius:8px; padding: 1rem 1.25rem; color:#876900; font-size:1.03em; box-shadow:0 2px 8px 0 #ffefbc38;">
                                <strong>Ghi chú:</strong>
                                <%= loanSlip.getNotes() !=null && !loanSlip.getNotes().isEmpty() ? loanSlip.getNotes()
                                    : "Không có" %>
                            </div>

                            <div class="table-responsive" style="margin-bottom:2rem;">
                                <table style="width:100%; border-collapse:collapse; font-size:1.05em;">
                                    <thead style="background:#215591; color:white;">
                                        <tr>
                                            <th style="padding:8px 0;">STT</th>
                                            <th>Mã bản sao</th>
                                            <th>Mã tài liệu</th>
                                            <th>Tên</th>
                                            <th>Tác giả</th>
                                            <th>Năm xuất bản</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% if (details !=null) { int i=1; for (LoanSlipDetail d : details) { String
                                            bg=(i % 2==0) ? "#f8fafc" : "#fff" ; String trStyle="style='background:" +
                                            bg + ";'" ; %>
                                            <tr <%=trStyle %> >
                                                <td style="text-align:center; padding:7px 0;">
                                                    <%= i++ %>
                                                </td>
                                                <td style="text-align:center;">
                                                    <%= d.getDocumentCopy() !=null ? d.getDocumentCopy().getBarcode()
                                                        : "" %>
                                                </td>
                                                <td style="text-align:center;">
                                                    <%= d.getDocumentCopy() !=null &&
                                                        d.getDocumentCopy().getDocument().getId() !=null ?
                                                        d.getDocumentCopy().getDocument().getId() : "" %>
                                                </td>
                                                <td>
                                                    <%= d.getDocumentCopy() !=null &&
                                                        d.getDocumentCopy().getDocument().getName() !=null ?
                                                        d.getDocumentCopy().getDocument().getName() : "" %>
                                                </td>
                                                <td>
                                                    <%= d.getDocumentCopy() !=null &&
                                                        d.getDocumentCopy().getDocument().getAuthor() !=null ?
                                                        d.getDocumentCopy().getDocument().getAuthor() : "" %>
                                                </td>
                                                <td style="text-align:center;">
                                                    <%= d.getDocumentCopy() !=null &&
                                                        d.getDocumentCopy().getDocument().getYearOfPublication() !=null
                                                        ? d.getDocumentCopy().getDocument().getYearOfPublication() : ""
                                                        %>
                                                </td>
                                            </tr>
                                            <% } } %>
                                    </tbody>
                                </table>
                            </div>

                            <div style="margin-top:1.7rem; display:flex; gap:1.1rem; justify-content: flex-end;">
                                <button class="btn btn-primary" onclick="window.print()">In phiếu</button>
                                <a href="${pageContext.request.contextPath}/librarian/borrow"
                                    class="btn btn-secondary">Hoàn thành</a>
                            </div>

                            <% } else { %>
                                <div class="alert alert-error">Không có thông tin phiếu mượn.</div>
                                <% } %>

                </div>
            </div>
        </body>

        </html>