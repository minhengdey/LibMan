package org.example.demo.dao;

import org.example.demo.model.Reader;
import org.example.demo.model.ReaderCard;
import org.example.demo.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReaderCardDAO {
    public ReaderCard findByCode(String cardId) {
        String sql = "SELECT u.*, c.card_id, c.issue_date, c.expiration_date, c.status " +
                "FROM tblreadercard c " +
                "JOIN tblreader r ON c.reader_id = r.user_id " +
                "JOIN tbluser u ON r.user_id = u.id " +
                "WHERE c.card_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cardId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String fullName = rs.getString("full_name");
                String address = rs.getString("address");
                LocalDateTime dob = rs.getTimestamp("dob").toLocalDateTime();
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String role = rs.getString("role");
                String notes = rs.getString("notes");
                LocalDateTime issueDate = rs.getTimestamp("issue_date").toLocalDateTime();
                LocalDateTime expirationDate = rs.getTimestamp("expiration_date").toLocalDateTime();
                String status = rs.getString("status");

                return new ReaderCard(cardId, issueDate, expirationDate, status, new Reader(id, fullName, address, dob, email, phoneNumber, role, notes));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
