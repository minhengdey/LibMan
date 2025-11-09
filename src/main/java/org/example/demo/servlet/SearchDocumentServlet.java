package org.example.demo.servlet;

import org.example.demo.dao.DocumentDAO;
import org.example.demo.model.Document;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/reader/search")
public class SearchDocumentServlet extends HttpServlet {
    private DocumentDAO documentDAO = new DocumentDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("keyword");
        String ajax = request.getParameter("ajax");

        List<Document> documents;

        if (keyword == null || keyword.trim().isEmpty()) {
            documents = documentDAO.findAll();
            keyword = "";
        } else {
            documents = documentDAO.searchByTitle(keyword);
        }

        boolean isAjax = "1".equals(ajax) || "true".equalsIgnoreCase(ajax)
                || "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        if (isAjax) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();

            StringBuilder sb = new StringBuilder();
            sb.append('{');
            sb.append("\"count\":").append(documents != null ? documents.size() : 0).append(',');
            sb.append("\"keyword\":\"").append(jsonEscape(keyword)).append('\"').append(',');
            sb.append("\"documents\":[");
            if (documents != null) {
                boolean first = true;
                for (Document doc : documents) {
                    if (!first)
                        sb.append(',');
                    first = false;
                    sb.append('{');
                    sb.append("\"id\":").append(doc.getId() != null ? doc.getId() : "null").append(',');
                    sb.append("\"name\":\"").append(jsonEscape(doc.getName())).append('\"').append(',');
                    sb.append("\"author\":\"").append(jsonEscape(doc.getAuthor())).append('\"').append(',');
                    sb.append("\"publisher\":\"").append(jsonEscape(doc.getPublisher())).append('\"').append(',');
                    sb.append("\"yearOfPublication\":")
                            .append(doc.getYearOfPublication() != null ? doc.getYearOfPublication() : "null");
                    sb.append('}');
                }
            }
            sb.append(']');
            sb.append('}');

            out.print(sb);
            out.flush();
            return;
        }

        request.setAttribute("documents", documents);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/reader/search.jsp").forward(request, response);
    }

    private String jsonEscape(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
