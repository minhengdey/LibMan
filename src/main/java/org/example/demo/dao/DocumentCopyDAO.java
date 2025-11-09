package org.example.demo.dao;

import org.example.demo.model.DocumentCopy;
import org.example.demo.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentCopyDAO {
    public List<DocumentCopy> findByDocumentId(String documentId) {
        List<DocumentCopy> copies = new ArrayList<>();
        String sql1 = "SELECT dc.*, d.* FROM tbldocumentcopy dc INNER JOIN tbldocument d ON dc.document_id = d.id WHERE dc.document_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql1)) {

            stmt.setString(1, documentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                copies.add(extractCopyFromResultSet(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return copies;
    }

    public DocumentCopy findByCode(String barcode) {
        String sql1 = "SELECT dc.*, d.* FROM tbldocumentcopy dc INNER JOIN tbldocument d ON dc.document_id = d.id WHERE dc.barcode = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql1)) {

            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractCopyFromResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean updateStatus(String barcode, String status) {
        String sql1 = "UPDATE tbldocumentcopy SET status = ? WHERE barcode = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql1)) {

            stmt.setString(1, status);
            stmt.setString(2, barcode);
            return stmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public DocumentCopy extractCopyFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String isbn = rs.getString("ISBN");
        String name = rs.getString("name");
        String author = rs.getString("author");
        String publisher = rs.getString("publisher");
        int yearOfPublication = rs.getInt("year_of_publication");
        String genre = rs.getString("genre");
        String description = rs.getString("description");
        String barcode = rs.getString("barcode");
        String status = rs.getString("status");

        return new DocumentCopy(id, isbn, name, author, publisher, yearOfPublication, genre, description, barcode, status);
    }
}