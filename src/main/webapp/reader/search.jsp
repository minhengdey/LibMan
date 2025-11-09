<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ page import="java.util.*, org.example.demo.model.*" %>
        <!DOCTYPE html>
        <html lang="vi">

        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Tìm kiếm tài liệu - LibMan</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        </head>

        <body>
            <!-- Header -->
            <header>
                <div class="header-content">
                    <div class="logo">
                        <div class="logo-icon">📚</div>
                        <span>LibMan</span>
                    </div>
                    <div class="user-info">
                        <span class="user-name">
                            Xin chào
                        </span>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-logout" style="text-decoration: none;">
                            Đăng nhập
                        </a>
                    </div>
                </div>
            </header>

            <!-- Main Content -->
            <div class="container">
                <div class="card">
                    <div class="card-header">
                        <h2 class="card-title">🔍 Tìm kiếm tài liệu</h2>
                    </div>

                    <!-- Search Form -->
                    <form action="${pageContext.request.contextPath}/reader/search" method="get" class="search-box">
                        <input type="text" name="keyword" class="form-control"
                            placeholder="Nhập tên tài liệu, tác giả, hoặc từ khóa..."
                            value='<%= request.getAttribute("keyword") != null ? request.getAttribute("keyword") : "" %>'
                            autofocus>
                        <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                    </form>

                    <!-- Search Results -->
                    <div id="results">
                        <% 
                            List<Document> allDocuments = (List<Document>) request.getAttribute("documents");
                            String keyword = (String) request.getAttribute("keyword");
                            
                            // Phân trang - xử lý ở JSP
                            int pageSize = 12;
                            String pageParam = request.getParameter("page");
                            int currentPage = 1;
                            try {
                                if (pageParam != null && !pageParam.trim().isEmpty()) {
                                    currentPage = Integer.parseInt(pageParam);
                                    if (currentPage < 1) currentPage = 1;
                                }
                            } catch (NumberFormatException e) {
                                currentPage = 1;
                            }
                            
                            int totalDocuments = (allDocuments != null) ? allDocuments.size() : 0;
                            int totalPages = (int) Math.ceil((double) totalDocuments / pageSize);
                            if (currentPage > totalPages && totalPages > 0) {
                                currentPage = totalPages;
                            }
                            
                            int startIndex = (currentPage - 1) * pageSize;
                            int endIndex = Math.min(startIndex + pageSize, totalDocuments);
                            
                            if (keyword != null && !keyword.trim().isEmpty()) {
                        %>
                                <div style="margin-bottom: 0.3rem; color: var(--text-light);">
                                    Tìm thấy <strong><%= totalDocuments %></strong> kết quả cho "<strong><%= keyword %></strong>"
                                </div>
                        <% } 
                            if (allDocuments != null && !allDocuments.isEmpty()) { 
                        %>
                                    <div class="document-grid">
                                        <% for (int i = startIndex; i < endIndex; i++) { 
                                            Document doc = allDocuments.get(i);
                                        %>
                                            <div class="document-card"
                                                onclick="location.href='${pageContext.request.contextPath}/reader/document-detail?id=<%= doc.getId() %>'">
                                                <div class="document-body">
                                                    <h3 class="document-title">
                                                        <%= doc.getName() %>
                                                    </h3>
                                                    <p class="document-author"><strong>Tác giả:</strong>
                                                        <%= doc.getAuthor() != null ? doc.getAuthor() : "Không rõ" %>
                                                    </p>
                                                    <p class="document-author"><strong>NXB:</strong>
                                                        <%= doc.getPublisher() != null ? doc.getPublisher() : "Không rõ" %>
                                                    </p>
                                                    <p class="document-author"><strong>Năm:</strong>
                                                        <%= doc.getYearOfPublication() %>
                                                    </p>
                                                </div>
                                            </div>
                                        <% } %>
                                    </div>
                                    
                                    <% if (totalPages > 1) { %>
                                    <div class="pagination">
                                        <% 
                                            String baseUrl = request.getContextPath() + "/reader/search";
                                            if (keyword != null && !keyword.trim().isEmpty()) {
                                                baseUrl += "?keyword=" + java.net.URLEncoder.encode(keyword, "UTF-8");
                                            } else {
                                                baseUrl += "?keyword=";
                                            }
                                        %>
                                        
                                        <% if (currentPage > 1) { %>
                                            <a href="<%= baseUrl %>&page=<%= currentPage - 1 %>" class="pagination-btn">
                                                ← Trước
                                            </a>
                                        <% } else { %>
                                            <button type="button" class="pagination-btn pagination-btn-disabled" disabled>
                                                ← Trước
                                            </button>
                                        <% } %>
                                        
                                        <span class="pagination-info">
                                            Trang <%= currentPage %> / <%= totalPages %>
                                        </span>
                                        
                                        <% if (currentPage < totalPages) { %>
                                            <a href="<%= baseUrl %>&page=<%= currentPage + 1 %>" class="pagination-btn">
                                                Sau →
                                            </a>
                                        <% } else { %>
                                            <button type="button" class="pagination-btn pagination-btn-disabled" disabled>
                                                Sau →
                                            </button>
                                        <% } %>
                                    </div>
                                    <% } %>
                        <% } else if (keyword != null && !keyword.trim().isEmpty()) { %>
                                        <div class="alert alert-info">
                                            Không tìm thấy kết quả nào cho từ khóa "<strong><%= keyword %></strong>"
                                        </div>
                        <% } %>
                    </div>
                </div>
            </div>
            <script>
                (function () {
                    const input = document.querySelector('input[name="keyword"]');
                    const results = document.getElementById('results');
                    const contextPath = '${pageContext.request.contextPath}';
                    const pageSize = 12;
                    let allDocuments = [];
                    let currentKeyword = '';
                    let currentPage = 1;

                    function debounce(fn, delay) {
                        let t;
                        return function () {
                            const args = arguments;
                            clearTimeout(t);
                            t = setTimeout(function () { fn.apply(null, args); }, delay);
                        }
                    }

                    function renderLoading() {
                        results.innerHTML = '<div style="color:var(--text-light);">Đang tải kết quả…</div>';
                    }

                    function escapeHtml(s) {
                        if (!s) return '';
                        return s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g, '&#x27;');
                    }

                    function getCurrentPageFromUrl() {
                        const urlParams = new URLSearchParams(window.location.search);
                        const page = urlParams.get('page');
                        return page ? parseInt(page) : 1;
                    }

                    function renderPagination(totalPages, currentPage, keyword) {
                        if (totalPages <= 1) return '';
                        
                        let baseUrl = contextPath + '/reader/search';
                        if (keyword && keyword.length) {
                            baseUrl += '?keyword=' + encodeURIComponent(keyword);
                        } else {
                            baseUrl += '?keyword=';
                        }
                        
                        let html = '<div class="pagination">';
                        
                        // Button Trước
                        if (currentPage > 1) {
                            html += '<a href="' + baseUrl + '&page=' + (currentPage - 1) + '" class="pagination-btn">← Trước</a>';
                        } else {
                            html += '<button type="button" class="pagination-btn pagination-btn-disabled" disabled>← Trước</button>';
                        }
                        
                        // Thông tin trang
                        html += '<span class="pagination-info">Trang ' + currentPage + ' / ' + totalPages + '</span>';
                        
                        // Button Sau
                        if (currentPage < totalPages) {
                            html += '<a href="' + baseUrl + '&page=' + (currentPage + 1) + '" class="pagination-btn">Sau →</a>';
                        } else {
                            html += '<button type="button" class="pagination-btn pagination-btn-disabled" disabled>Sau →</button>';
                        }
                        
                        html += '</div>';
                        return html;
                    }

                    function renderResults(documents, page, keyword) {
                        if (!documents || documents.length === 0) {
                            if (keyword && keyword.length) {
                                return '<div class="alert alert-info">Không tìm thấy kết quả nào cho từ khóa "<strong>' + escapeHtml(keyword) + '</strong>"</div>';
                            }
                            return '';
                        }

                        const totalDocuments = documents.length;
                        const totalPages = Math.ceil(totalDocuments / pageSize);
                        
                        // Validate page
                        if (page > totalPages && totalPages > 0) {
                            page = totalPages;
                        }
                        
                        const startIndex = (page - 1) * pageSize;
                        const endIndex = Math.min(startIndex + pageSize, totalDocuments);
                        const pageDocuments = documents.slice(startIndex, endIndex);

                        let html = '';
                        
                        // Thông báo số kết quả
                        if (keyword && keyword.length) {
                            html += '<div style="margin-bottom: 1.5rem; color: var(--text-light);">Tìm thấy <strong>' + totalDocuments + '</strong> kết quả cho "<strong>' + escapeHtml(keyword) + '</strong>"</div>';
                        }

                        // Danh sách documents
                        html += '<div class="document-grid">';
                        pageDocuments.forEach(doc => {
                            const link = contextPath + '/reader/document-detail?id=' + encodeURIComponent(doc.id);
                            html += '<div class="document-card" onclick="location.href=\'' + link + '\'">';
                            html += '<div class="document-body">';
                            html += '<h3 class="document-title">' + escapeHtml(doc.name) + '</h3>';
                            html += '<p class="document-author"><strong>Tác giả:</strong> ' + escapeHtml(doc.author || 'Không rõ') + '</p>';
                            html += '<p class="document-author"><strong>NXB:</strong> ' + escapeHtml(doc.publisher || 'Không rõ') + '</p>';
                            html += '<p class="document-author"><strong>Năm:</strong> ' + (doc.yearOfPublication != null ? escapeHtml(String(doc.yearOfPublication)) : '') + '</p>';
                            html += '</div></div>';
                        });
                        html += '</div>';

                        // Phân trang
                        html += renderPagination(totalPages, page, keyword);

                        return html;
                    }

                    async function doSearch(q) {
                        try {
                            renderLoading();
                            currentKeyword = q || '';
                            currentPage = 1; // Reset về trang 1 khi search mới
                            
                            const url = contextPath + '/reader/search?ajax=1&keyword=' + encodeURIComponent(q || '');
                            const res = await fetch(url, { headers: { 'X-Requested-With': 'XMLHttpRequest' } });
                            if (!res.ok) throw new Error('HTTP ' + res.status);
                            const data = await res.json();

                            allDocuments = data.documents || [];
                            
                            // Cập nhật URL mà không reload trang
                            const newUrl = contextPath + '/reader/search?keyword=' + encodeURIComponent(q || '');
                            window.history.pushState({ keyword: q, page: 1 }, '', newUrl);
                            
                            results.innerHTML = renderResults(allDocuments, currentPage, data.keyword || '');
                            
                            // Thêm event listener cho các link phân trang sau khi render
                            attachPaginationListeners();
                        } catch (e) {
                            results.innerHTML = '<div class="alert alert-error">Lỗi tải kết quả</div>';
                            console.error(e);
                        }
                    }
                    
                    function attachPaginationListeners() {
                        // Khi click vào pagination link, sẽ reload trang (server-side sẽ xử lý)
                        // Không cần làm gì thêm vì link sẽ tự động reload
                    }

                    const debounced = debounce(function (e) { doSearch(e.target.value); }, 300);

                    if (input) {
                        input.addEventListener('input', debounced);
                    }
                    
                    // Khi trang load, nếu có keyword trong input nhưng chưa có kết quả, không làm gì
                    // Vì server đã render sẵn kết quả rồi
                })();
            </script>

        </body>

        </html>