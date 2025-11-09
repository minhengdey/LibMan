package org.example.demo.servlet;

import org.example.demo.dao.DocumentCopyDAO;
import org.example.demo.dao.DocumentDAO;
import org.example.demo.model.Document;
import org.example.demo.model.DocumentCopy;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/reader/document-detail")
public class DocumentDetailServlet extends HttpServlet {
    private DocumentDAO documentDAO = new DocumentDAO();
    private DocumentCopyDAO copyDAO = new DocumentCopyDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                String documentId = idParam;
                Document document = documentDAO.findById(documentId);
                List<DocumentCopy> copies = copyDAO.findByDocumentId(documentId);

                request.setAttribute("document", document);
                request.setAttribute("copies", copies);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        request.getRequestDispatcher("/reader/document-detail.jsp").forward(request, response);
    }
}
