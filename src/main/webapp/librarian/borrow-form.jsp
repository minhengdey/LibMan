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
                <header>
                    <div class="header-content">
                        <div class="logo">
                            <div class="logo-icon">📚</div>
                            <span>LibMan</span>
                        </div>
                        <div class="user-info">
                            <span class="user-name">Xin chào, <%= ((User) session.getAttribute("user")).getFullName() %>
                            </span>
                            <a href="${pageContext.request.contextPath}/logout" class="btn-logout">Đăng xuất</a>
                        </div>
                    </div>
                </header>

                <nav>
                    <div class="nav-content">
                        <ul class="nav-menu">
                            <li><a href="${pageContext.request.contextPath}/librarian/borrow" class="active">Cho
                                    mượn</a></li>
                        </ul>
                    </div>
                </nav>

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

                                <form action="${pageContext.request.contextPath}/librarian/borrow" method="get">
                                    <div class="form-group">
                                        <label class="form-label">Bước 1: Quét hoặc nhập mã bạn đọc</label>
                                        <div style="display: flex; gap: 1rem;">
                                            <input type="text" name="readerCode" class="form-control"
                                                placeholder="Quét thẻ hoặc nhập mã bạn đọc" value="${param.readerCode}"
                                                required autofocus>
                                            <button type="submit" class="btn btn-primary">Tìm bạn đọc</button>
                                        </div>
                                    </div>
                                </form>

                                <% ReaderCard reader=(ReaderCard) request.getAttribute("readerCard"); if (reader !=null)
                                    { %>
                                    <div
                                        style="margin: 2rem 0; padding: 1.5rem; background: var(--bg-blue); border-radius: 8px;">
                                        <h3 style="color: var(--dark-blue); margin-bottom: 1rem;">Thông tin bạn đọc</h3>
                                        <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 1rem;">
                                            <div><strong>Mã bạn đọc:</strong>
                                                <%= reader.getReader().getId() %>
                                            </div>
                                            <div><strong>Họ tên:</strong>
                                                <%= reader.getReader().getFullName() %>
                                            </div>
                                            <div><strong>Trạng thái:</strong>
                                                <%= reader.getStatus() !=null ? reader.getStatus() : "" %>
                                            </div>
                                        </div>
                                    </div>

                                    <form action="${pageContext.request.contextPath}/librarian/borrow" method="post">
                                        <input type="hidden" name="readerCode" value="<%= reader.getCardId() %>">

                                        <div class="form-group">
                                            <label class="form-label">Bước 2: Quét mã tài liệu cần mượn</label>
                                            <div id="documentList">
                                                <% String[] copyCodes=request.getParameterValues("copyCodes"); if
                                                    (copyCodes !=null && copyCodes.length> 0) {
                                                    java.util.List<DocumentCopy> _copyDetails = (java.util.List
                                                        <DocumentCopy>) request.getAttribute("copyDetails");
                                                            for (int i = 0; i < copyCodes.length; i++) { DocumentCopy
                                                                _cd=null; if (_copyDetails !=null &&
                                                                _copyDetails.size()> i) _cd = _copyDetails.get(i);
                                                                boolean _validated = _cd != null;
                                                                String _docName = _validated && _cd.getDocument() !=
                                                                null ? _cd.getDocument().getName() : null;
                                                                String _docAuthor = _validated && _cd.getDocument() !=
                                                                null ? _cd.getDocument().getAuthor() : null;
                                                                String _status = _validated ? _cd.getStatus() : null;
                                                                %>
                                                                <div class="document-item"
                                                                    style="display: flex; gap: 1rem; margin-bottom: 1rem; align-items: center;">
                                                                    <input type="text" name="copyCodes"
                                                                        class="form-control"
                                                                        placeholder="Nhập barcode của bản sao (ví dụ: BC123456)"
                                                                        value='<%= copyCodes[i] != null ? copyCodes[i] : "" %>'
                                                                        <%=(i==0) ? "required" : "" %>
                                                                    <%= _validated ? "readonly" : "" %> >
                                                                        <!-- Hidden fields to preserve validated copy info so POST can reuse without DB lookup -->
                                                                        <input type="hidden" name="foundBarcode"
                                                                            value="<%= _cd != null ? _cd.getBarcode() : "" %>">
                                                                        <input type="hidden" name="foundStatus"
                                                                            value="<%= _cd != null ? _cd.getStatus() : "" %>">
                                                                        <input type="hidden" name="foundDocId"
                                                                            value="<%= _cd != null && _cd.getDocument() != null && _cd.getDocument().getId() != null ? _cd.getDocument().getId() : "" %>">
                                                                        <input type="hidden" name="foundDocName"
                                                                            value="<%= _docName != null ? _docName : "" %>">
                                                                        <input type="hidden" name="foundDocAuthor"
                                                                            value="<%= _docAuthor != null ? _docAuthor : "" %>">
                                                                        <div class="copy-info"
                                                                            style="min-width:260px; color: var(--dark-blue);">
                                                                            <% if (_validated) { %>
                                                                                <div>
                                                                                    <strong>
                                                                                        <%= _docName !=null ? _docName
                                                                                            : "" %>
                                                                                    </strong><br>
                                                                                    Tác giả: <%= _docAuthor !=null ?
                                                                                        _docAuthor : "" %><br>
                                                                                        Mã: <%= _cd.getBarcode() %> |
                                                                                            Trạng thái: <%= _status
                                                                                                !=null ? _status : "" %>
                                                                                </div>
                                                                                <% } else if (copyCodes[i] !=null &&
                                                                                    !copyCodes[i].trim().isEmpty()) { %>
                                                                                    <span style="color: #c00">Không tìm
                                                                                        thấy hoặc không khả dụng</span>
                                                                                    <% } %>
                                                                        </div>
                                                                        <% if (i==0) { %>
                                                                            <button type="button"
                                                                                class="btn btn-secondary btn-sm btn-add"
                                                                                <%=_validated ? "" : "disabled" %>
                                                                                onclick="addDocumentField()">➕
                                                                                Thêm</button>
                                                                            <% } else { %>
                                                                                <button type="button"
                                                                                    class="btn btn-danger btn-sm"
                                                                                    onclick="removeDocumentField(this)">✕
                                                                                    Xóa</button>
                                                                                <% } %>
                                                                </div>
                                                                <% } } else { %>
                                                                    <div class="document-item"
                                                                        style="display: flex; gap: 1rem; margin-bottom: 1rem; align-items: center;">
                                                                        <input type="text" name="copyCodes"
                                                                            class="form-control"
                                                                            placeholder="Nhập barcode của bản sao (ví dụ: BC123456)"
                                                                            required>
                                                                        <div class="copy-info"
                                                                            style="min-width:260px; color: var(--dark-blue);">
                                                                        </div>
                                                                        <button type="button"
                                                                            class="btn btn-secondary btn-sm btn-add"
                                                                            disabled onclick="addDocumentField()">➕
                                                                            Thêm</button>
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
                                                class="btn btn-secondary">Hủy</a>
                                            <button type="submit" class="btn btn-success">✓ Hoàn tất cho mượn</button>
                                        </div>
                                    </form>
                                    <% } %>
                    </div>

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
                    const ctx = '<%= request.getContextPath() %>';

                    function addDocumentField() {
                        const container = document.getElementById('documentList');
                        const newField = document.createElement('div');
                        newField.className = 'document-item';
                        newField.style.cssText = 'display: flex; gap: 1rem; margin-bottom: 1rem; align-items: center;';
                        newField.innerHTML = `
                <input type="text" name="copyCodes" class="form-control" placeholder="Nhập barcode của bản sao (ví dụ: BC123456)" required>
                <div class="copy-info" style="min-width:260px; color: var(--dark-blue);"></div>
                <button type="button" class="btn btn-danger btn-sm" onclick="removeDocumentField(this)">✕ Xóa</button>
            `;
                        container.appendChild(newField);
                        const input = newField.querySelector('input[name="copyCodes"]');
                        input.focus();
                    }

                    function removeDocumentField(button) { button.parentElement.remove(); }

                    function handleBarcodeEnter(input) {
                        const code = input.value.trim();
                        if (!code) return;
                        const item = input.parentElement;
                        const infoDiv = item.querySelector('.copy-info');
                        infoDiv.textContent = 'Đang tìm...';
                        fetch(ctx + '/librarian/borrow?lookupBarcode=' + encodeURIComponent(code))
                            .then(res => res.json())
                            .then(json => {
                                if (json.success) {
                                    const d = json.data;
                                    infoDiv.innerHTML = `<div><strong>` + escapeHtml(d.document.name || '') + `</strong><br>` + `Tác giả: ` + escapeHtml(d.document.author || '') + `<br>` + `Mã: ` + escapeHtml(d.barcode || '') + ` | Trạng thái: ` + escapeHtml(d.status || '') + `</div>`;
                                    input.readOnly = true;
                                    input.dataset.validated = 'true';

                                    const hb = document.createElement('input'); hb.type = 'hidden'; hb.name = 'foundBarcode'; hb.value = d.barcode || '';
                                    const hs = document.createElement('input'); hs.type = 'hidden'; hs.name = 'foundStatus'; hs.value = d.status || '';
                                    const hdId = document.createElement('input'); hdId.type = 'hidden'; hdId.name = 'foundDocId'; hdId.value = d.document && d.document.id ? d.document.id : '';
                                    const hdName = document.createElement('input'); hdName.type = 'hidden'; hdName.name = 'foundDocName'; hdName.value = d.document && d.document.name ? d.document.name : '';
                                    const hdAuthor = document.createElement('input'); hdAuthor.type = 'hidden'; hdAuthor.name = 'foundDocAuthor'; hdAuthor.value = d.document && d.document.author ? d.document.author : '';
                                    item.appendChild(hb); item.appendChild(hs); item.appendChild(hdId); item.appendChild(hdName); item.appendChild(hdAuthor);
                                    const firstAddBtn = document.querySelector('#documentList .btn-add');
                                    if (firstAddBtn) firstAddBtn.disabled = false;
                                } else {
                                    infoDiv.innerHTML = `<span style="color: #c00">` + escapeHtml(json.message || 'Không tìm thấy') + `</span>`;
                                }
                            })
                            .catch(err => { console.error(err); infoDiv.innerHTML = `<span style="color: #c00">Lỗi khi tìm</span>`; });
                    }

                    function escapeHtml(s) { if (!s) return ''; return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#39;'); }

                    document.addEventListener('keydown', function (e) { if (e.target && e.target.name === 'copyCodes' && e.key === 'Enter') { e.preventDefault(); handleBarcodeEnter(e.target); } });

                    document.addEventListener('DOMContentLoaded', function () { const readerCodeInput = document.querySelector('input[name="readerCode"]'); const copyCodeInput = document.querySelector('input[name="copyCodes"]'); var _hasReader = document.body.dataset.hasReader === 'true'; if (_hasReader) { if (copyCodeInput) copyCodeInput.focus(); } else { if (readerCodeInput) readerCodeInput.focus(); } });
                </script>
            </body>

        </html>