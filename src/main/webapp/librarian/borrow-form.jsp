<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="org.example.demo.model.*" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Cho mượn tài liệu - LibMan</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <% boolean hasReader=request.getAttribute("readerCard") !=null; %>

            <body data-has-reader="<%= hasReader %>">
                <!-- Header -->
                <header>
                    <div class="header-content">
                        <div class="logo">
                            <div class="logo-icon">📚</div>
                            <span>LibMan</span>
                        </div>
                        <div class="user-info">
                            <span class="user-name">
                                Xin chào, <%= ((User)session.getAttribute("user")).getFullName() %>
                            </span>
                            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
                                Đăng xuất
                            </a>
                        </div>
                    </div>
                </header>

                <!-- Navigation -->
                <nav>
                    <div class="nav-content">
                        <ul class="nav-menu">
                            <li><a href="${pageContext.request.contextPath}/librarian/borrow" class="active">Cho
                                    mượn</a>
                            </li>
                        </ul>
                    </div>
                </nav>

                <!-- Main Content -->
                <div class="container">
                    <div class="card">
                        <div class="card-header">
                            <h2 class="card-title">📤 Cho mượn tài liệu</h2>
                        </div>

                        <% if (request.getAttribute("error") !=null) { %>
                            <div class="alert alert-error">
                                <%= request.getAttribute("error") %>
                            </div>
                            <% } %>

                                <!-- Step 1: Find Reader -->
                                <form action="${pageContext.request.contextPath}/librarian/borrow" method="get">
                                    <div class="form-group">
                                        <label class="form-label">Bước 1: Quét hoặc nhập mã bạn đọc</label>
                                        <div style="display: flex; gap: 1rem;">
                                            <input type="text" name="readerCode" class="form-control"
                                                placeholder="Quét thẻ hoặc nhập mã bạn đọc"
                                                value="<%= request.getParameter(" readerCode") !=null ?
                                                request.getParameter("readerCode") : "" %>"
                                            required autofocus>
                                            <button type="submit" class="btn btn-primary">Tìm bạn đọc</button>
                                        </div>
                                    </div>
                                </form>

                                <% ReaderCard reader=(ReaderCard) request.getAttribute("readerCard"); if (reader !=null)
                                    { %>
                                    <!-- Reader Info -->
                                    <div
                                        style="margin: 2rem 0; padding: 1.5rem; background: var(--bg-blue); border-radius: 8px;">
                                        <h3 style="color: var(--dark-blue); margin-bottom: 1rem;">Thông tin bạn đọc
                                        </h3>
                                        <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 1rem;">
                                            <div>
                                                <strong>Mã bạn đọc:</strong>
                                                <%= reader.getReader().getId() %>
                                            </div>
                                            <div>
                                                <strong>Họ tên:</strong>
                                                <%= reader.getReader().getFullName() %>
                                            </div>
                                            <div>
                                                <strong>Trạng thái:</strong>
                                                <%= reader.getStatus() !=null ? reader.getStatus() : "" %>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Step 2: Add Documents -->
                                    <form action="${pageContext.request.contextPath}/librarian/borrow" method="post">
                                        <input type="hidden" name="readerCode" value="<%= reader.getCardId() %>">

                                        <div class="form-group">
                                            <label class="form-label">Bước 2: Quét mã tài liệu cần mượn</label>
                                            <div id="documentList">
                                                <%-- If the servlet returned copyCodes (e.g., on validation error),
                                                    re-render them --%>
                                                    <% String[] copyCodes=request.getParameterValues("copyCodes"); if
                                                        (copyCodes !=null && copyCodes.length> 0) {
                                                        for (int i = 0; i < copyCodes.length; i++) { %>
                                                            <div class="document-item"
                                                                style="display: flex; gap: 1rem; margin-bottom: 1rem;">
                                                                <input type="text" name="copyCodes" class="form-control"
                                                                    placeholder="Nhập barcode của bản sao (ví dụ: BC123456)"
                                                                    value="<%= copyCodes[i] != null ? copyCodes[i] : "" %>"
                                                                    <%=(i==0) ? "required" : "" %> >
                                                                <% if (i==0) { %>
                                                                    <button type="button"
                                                                        class="btn btn-secondary btn-sm"
                                                                        onclick="addDocumentField()">
                                                                        ➕ Thêm
                                                                    </button>
                                                                    <% } else { %>
                                                                        <button type="button"
                                                                            class="btn btn-danger btn-sm"
                                                                            onclick="removeDocumentField(this)">
                                                                            ✕ Xóa
                                                                        </button>
                                                                        <% } %>
                                                            </div>
                                                            <% } } else { %>
                                                                <div class="document-item"
                                                                    style="display: flex; gap: 1rem; margin-bottom: 1rem;">
                                                                    <input type="text" name="copyCodes"
                                                                        class="form-control"
                                                                        placeholder="Nhập barcode của bản sao (ví dụ: BC123456)"
                                                                        required>
                                                                    <button type="button"
                                                                        class="btn btn-secondary btn-sm"
                                                                        onclick="addDocumentField()">
                                                                        ➕ Thêm
                                                                    </button>
                                                                </div>
                                                                <% } %>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="form-label">Ghi chú (nếu có)</label>
                                            <textarea name="notes" class="form-control" rows="2"
                                                placeholder="Nhập ghi chú cho phiếu mượn (tuỳ chọn)"><%= request.getParameter("notes") != null ? request.getParameter("notes") : "" %></textarea>
                                        </div>

                                        <div style="display: flex; gap: 1rem; justify-content: flex-end;">
                                            <a href="${pageContext.request.contextPath}/librarian/borrow"
                                                class="btn btn-secondary">
                                                Hủy
                                            </a>
                                            <button type="submit" class="btn btn-success">
                                                ✓ Hoàn tất cho mượn
                                            </button>
                                        </div>
                                    </form>
                                    <% } %>
                    </div>

                    <!-- Instructions -->
                    <div class="alert alert-info">
                        <strong>📌 Hướng dẫn:</strong>
                        <ol style="margin: 0.5rem 0 0 1.5rem; padding: 0;">
                            <li>Quét hoặc nhập mã bạn đọc để tìm thông tin</li>
                            <li>Quét mã từng tài liệu cần mượn (hoặc nhập thủ công)</li>
                            <li>Click "Thêm" để thêm tài liệu khác</li>
                            <li>Click "Hoàn tất cho mượn" để tạo phiếu mượn</li>
                        </ol>
                    </div>
                </div>

                <script>
                    function addDocumentField() {
                        const container = document.getElementById('documentList');
                        const newField = document.createElement('div');
                        newField.className = 'document-item';
                        newField.style.cssText = 'display: flex; gap: 1rem; margin-bottom: 1rem;';
                        newField.innerHTML = `
                    <input type="text" name="copyCodes" class="form-control"
                           placeholder="Nhập barcode của bản sao (ví dụ: BC123456)" required>
                <button type="button" class="btn btn-danger btn-sm" onclick="removeDocumentField(this)">
                    ✕ Xóa
                </button>
            `;
                        container.appendChild(newField);
                        newField.querySelector('input').focus();
                    }

                    function removeDocumentField(button) {
                        button.parentElement.remove();
                    }

                    // Auto-focus on copy code input after page load
                    document.addEventListener('DOMContentLoaded', function () {
                        const readerCodeInput = document.querySelector('input[name="readerCode"]');
                        const copyCodeInput = document.querySelector('input[name="copyCodes"]');
                        var _hasReader = document.body.dataset.hasReader === 'true';
                        if (_hasReader) {
                            if (copyCodeInput) copyCodeInput.focus();
                        } else {
                            if (readerCodeInput) readerCodeInput.focus();
                        }
                    });
                </script>
            </body>

        </html>