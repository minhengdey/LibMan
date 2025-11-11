package org.example.demo.dao;

import org.example.demo.model.Document;
import org.example.demo.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentDAO {
    public List<Document> searchByTitle(String keyword) {
        List<Document> documents = new ArrayList<>();
        String sql1 = "SELECT * FROM tbldocument WHERE name LIKE ? ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql1)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                documents.add(extractDocumentFromResultSet(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return documents;
    }

    public Document findById(String documentId) {
        String sqlById = "SELECT * FROM tbldocument WHERE id = ?";

        try {
            int id = Integer.parseInt(documentId);
            try (Connection conn = DBConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sqlById)) {

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return extractDocumentFromResultSet(rs);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (NumberFormatException nf) {
            nf.printStackTrace();
        }
        return null;
    }

    public List<Document> findAll() {
        List<Document> documents = new ArrayList<>();
        String sql1 = "SELECT * FROM tbldocument ORDER BY name";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql1)) {

            while (rs.next()) {
                documents.add(extractDocumentFromResultSet(rs));
            }
            return documents;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return documents;
    }

    private Document extractDocumentFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String isbn = rs.getString("ISBN");
        String name = rs.getString("name");
        String author = rs.getString("author");
        String publisher = rs.getString("publisher");
        int yearOfPublication = rs.getInt("year_of_publication");
        String genre = rs.getString("genre");
        String description = rs.getString("description");

        return new Document(id,  isbn, name, author, publisher, yearOfPublication, genre, description);
    }
}
