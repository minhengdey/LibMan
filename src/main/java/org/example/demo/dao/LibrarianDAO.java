package org.example.demo.dao;

import org.example.demo.model.Librarian;
import org.example.demo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibrarianDAO {
    public Librarian authenticate(String username, String password) {
        String sqlLib = "SELECT l.*, u.* FROM tbllibrarian l JOIN tbluser u ON l.user_id = u.id WHERE l.username = ? AND l.password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlLib)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extracLibrarianFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Librarian findLibrarianById(int librarianId) {
        String sql = "SELECT l.*, u.* " +
                "FROM tbllibrarian l " +
                "JOIN tbluser u ON l.user_id = u.id " +
                "WHERE l.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, librarianId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Librarian librarian = extracLibrarianFromResultSet(rs);
                librarian.setId(librarianId);
                return librarian;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Librarian extracLibrarianFromResultSet(ResultSet rs) throws SQLException {
        Librarian librarian = new Librarian();
        try {
            librarian.setId(rs.getInt("id"));
        } catch (SQLException e) {}
        librarian.setFullName(rs.getString("full_name"));
        librarian.setAddress(rs.getString("address"));
        librarian.setDob(rs.getTimestamp("dob").toLocalDateTime());
        librarian.setEmail(rs.getString("email"));
        librarian.setPhoneNumber(rs.getString("phone_number"));
        librarian.setRole(rs.getString("role"));
        librarian.setNotes(rs.getString("notes"));
        librarian.setUsername(rs.getString("username"));
        librarian.setPassword(rs.getString("password"));

        return librarian;
    }
}
