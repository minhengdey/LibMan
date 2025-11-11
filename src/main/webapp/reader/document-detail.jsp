<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.*, org.example.demo.model.*" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Chi tiết tài liệu - LibMan</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <header>
                <div class="header-content">
                    <div class="logo">
                        <div class="logo-icon">📚</div>
                        <span>LibMan</span>
                    </div>
                    <div class="user-info">
                        <span class="user-name">Xin chào</span>
                    </div>
                </div>
            </header>

            <div class="container">
                <% Document document=(Document) request.getAttribute("document"); List<DocumentCopy> copies = (List
                    <DocumentCopy>) request.getAttribute("copies");

                        if (document != null) {
                        %>
                        <div class="card">
                            <a href="${pageContext.request.contextPath}/reader/search"
                                style="display: inline-block; margin-bottom: 1rem; color: var(--primary-blue); text-decoration: none;">←
                                Quay lại tìm kiếm</a>

                            <div class="document-detail-container">
                                <div>
                                    <h1 style="font-size: 2rem; color: var(--dark-blue); margin-bottom: 1rem;">
                                        <%= document.getName() %>
                                    </h1>

                                    <div style="display: grid; gap: 1rem; font-size: 1rem;">
                                        <div>
                                            <strong style="color: var(--text-dark);">Mã tài liệu:</strong>
                                            <span style="color: var(--text-light);">
                                                <%= document.getId() %>
                                            </span>
                                        </div>
                                        <div>
                                            <strong style="color: var(--text-dark);">Tác giả:</strong>
                                            <span style="color: var(--text-light);">
                                                <%= document.getAuthor() !=null ? document.getAuthor() : "Không rõ" %>
                                            </span>
                                        </div>
                                        <div>
                                            <strong style="color: var(--text-dark);">Nhà xuất bản:</strong>
                                            <span style="color: var(--text-light);">
                                                <%= document.getPublisher() !=null ? document.getPublisher()
                                                    : "Không rõ" %>
                                            </span>
                                        </div>
                                        <div>
                                            <strong style="color: var(--text-dark);">Năm xuất bản:</strong>
                                            <span style="color: var(--text-light);">
                                                <%= document.getYearOfPublication() %>
                                            </span>
                                        </div>
                                        <div>
                                            <strong style="color: var(--text-dark);">ISBN:</strong>
                                            <span style="color: var(--text-light);">
                                                <%= document.getISBN() !=null ? document.getISBN() : "N/A" %>
                                            </span>
                                        </div>
                                        <div>
                                            <strong style="color: var(--text-dark);">Thể loại:</strong>
                                            <span style="color: var(--text-light);">
                                                <%= document.getGenre() %>
                                            </span>
                                        </div>
                                        <div>
                                            <strong style="color: var(--text-dark);">Tình trạng:</strong>
                                            <span style="color: var(--text-light);">
                                                <% int total=(copies !=null) ? copies.size() : 0; int available=0; if
                                                    (copies !=null) { for (DocumentCopy c : copies) { if (c.getStatus()
                                                    !=null && c.getStatus().trim().equalsIgnoreCase("AVAILABLE")) {
                                                    available++; } } } %>
                                                    <%= available %> / <%= total %> bản có sẵn
                                            </span>
                                        </div>
                                    </div>

                                    <% if (document.getDescription() !=null && !document.getDescription().isEmpty()) {
                                        %>
                                        <div
                                            style="margin-top: 2rem; padding-top: 2rem; border-top: 2px solid var(--border);">
                                            <h3 style="color: var(--dark-blue); margin-bottom: 1rem;">Mô tả</h3>
                                            <p style="color: var(--text-light); line-height: 1.6;">
                                                <%= document.getDescription() %>
                                            </p>
                                        </div>
                                        <% } %>
                                </div>

                                <% if (copies !=null && !copies.isEmpty()) { int pageSize=5; String
                                    pageParam=request.getParameter("page"); int currentPage=1; try { if (pageParam
                                    !=null && !pageParam.trim().isEmpty()) { currentPage=Integer.parseInt(pageParam); if
                                    (currentPage < 1) currentPage=1; } } catch (NumberFormatException e) {
                                    currentPage=1; } int totalCopies=copies.size(); int totalPages=(int)
                                    Math.ceil((double) totalCopies / pageSize); if (currentPage> totalPages &&
                                    totalPages > 0) {
                                    currentPage = totalPages;
                                    }

                                    int startIndex = (currentPage - 1) * pageSize;
                                    int endIndex = Math.min(startIndex + pageSize, totalCopies);
                                    %>
                                    <div class="card">
                                        <div class="card-header">
                                            <h3 class="card-title">Các bản sao</h3>
                                        </div>
                                        <div class="table-responsive">
                                            <table class="balanced-table">
                                                <thead>
                                                    <tr>
                                                        <th>Mã bản sao</th>
                                                        <th>Tình trạng</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <% for (int i=startIndex; i < endIndex; i++) { DocumentCopy
                                                        copy=copies.get(i); String status=copy.getStatus(); boolean
                                                        isAvailable=status !=null &&
                                                        status.trim().equalsIgnoreCase("AVAILABLE"); String
                                                        badgeClass="badge " + (isAvailable ? "badge-available"
                                                        : "badge-unavailable" ); %>
                                                        <tr>
                                                            <td>
                                                                <%= copy.getBarcode() %>
                                                            </td>
                                                            <td>
                                                                <span class="<%= badgeClass %>"
                                                                    style="display: inline-block;">
                                                                    <%= status %>
                                                                </span>
                                                            </td>
                                                        </tr>
                                                        <% } %>
                                                </tbody>
                                            </table>
                                        </div>

                                        <% if (totalPages> 1) { %>
                                            <div class="pagination">
                                                <% String documentId=String.valueOf(document.getId()); String
                                                    baseUrl=request.getContextPath() + "/reader/document-detail?id=" +
                                                    documentId; %>

                                                    <% if (currentPage> 1) { %>
                                                        <a href="<%= baseUrl %>&page=<%= currentPage - 1 %>"
                                                            class="pagination-btn">← Trước</a>
                                                        <% } else { %>
                                                            <button type="button"
                                                                class="pagination-btn pagination-btn-disabled"
                                                                disabled>← Trước</button>
                                                            <% } %>

                                                                <span class="pagination-info">Trang <%= currentPage %> /
                                                                        <%= totalPages %></span>

                                                                <% if (currentPage < totalPages) { %>
                                                                    <a href="<%= baseUrl %>&page=<%= currentPage + 1 %>"
                                                                        class="pagination-btn">Sau →</a>
                                                                    <% } else { %>
                                                                        <button type="button"
                                                                            class="pagination-btn pagination-btn-disabled"
                                                                            disabled>Sau →</button>
                                                                        <% } %>
                                            </div>
                                            <% } %>
                                    </div>
                                    <% } %>
                            </div>
                        </div>

                        <div class="alert alert-info">
                            <strong>📌 Lưu ý:</strong> Để mượn tài liệu, vui lòng đến trực tiếp thư viện và xuất trình
                            thẻ bạn đọc.
                        </div>

                        <% } else { %>
                            <div class="alert alert-error">Không tìm thấy thông tin tài liệu!</div>
                            <% } %>
            </div>
        </body>

        </html>