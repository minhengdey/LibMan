<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng nhập - LibMan</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>

    <body>
        <div class="login-container">
            <div class="login-card">
                <div class="login-header">
                    <div class="logo-icon" style="width: 80px; height: 80px; margin: 0 auto 1rem; font-size: 3rem;">
                        📚
                    </div>
                    <h1>LibMan</h1>
                    <p>Hệ thống quản lý thư viện</p>
                </div>

                <div class="login-body">
                    <% if (request.getAttribute("error") !=null) { %>
                        <div class="alert alert-error">
                            <%= request.getAttribute("error") %>
                        </div>
                        <% } %>

                            <% if (request.getAttribute("success") !=null) { %>
                                <div class="alert alert-success">
                                    <%= request.getAttribute("success") %>
                                </div>
                                <% } %>

                                    <form action="${pageContext.request.contextPath}/login" method="post">
                                        <div class="form-group">
                                            <label class="form-label">Tên đăng nhập</label>
                                            <input type="text" name="username" class="form-control"
                                                placeholder="Nhập tên đăng nhập" required autofocus>
                                        </div>

                                        <div class="form-group">
                                            <label class="form-label">Mật khẩu</label>
                                            <input type="password" name="password" class="form-control"
                                                placeholder="Nhập mật khẩu" required>
                                        </div>

                                        <button type="submit" class="btn btn-primary" style="width: 100%;">
                                            Đăng nhập
                                        </button>
                                    </form>

                                    <div
                                        style="text-align: center; margin-top: 1.5rem; padding-top: 1.5rem; border-top: 1px solid var(--border);">
                                        <p style="color: var(--text-light); margin-bottom: 0.5rem;">
                                            Bạn đọc tìm kiếm tài liệu?
                                        </p>
                                        <a href="${pageContext.request.contextPath}/reader/search"
                                            class="btn btn-primary"
                                            style="display:inline-block; width:100%; text-align:center;">
                                            Tìm kiếm tài liệu
                                        </a>
                                    </div>
                </div>
            </div>
        </div>
    </body>

    </html>