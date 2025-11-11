package org.example.demo.dao;

import org.example.demo.model.Reader;
import org.example.demo.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReaderDAO {
    public Reader findByUserId(int userId) {
        String sql = "SELECT r.*, u.* FROM tblreader r JOIN tbluser u ON r.user_id = u.id WHERE r.user_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractReaderFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Reader extractReaderFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String fullName = rs.getString("full_name");
        String address = rs.getString("address");
        LocalDateTime dob = rs.getTimestamp("dob").toLocalDateTime();
        String email = rs.getString("email");
        String phoneNumber = rs.getString("phone_number");
        String role = rs.getString("role");
        String notes = rs.getString("notes");

        return new Reader(id, fullName, address, dob, email, phoneNumber, role, notes);
    }
}
