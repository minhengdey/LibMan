package org.example.demo.dao;

import org.example.demo.model.LoanSlip;
import org.example.demo.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LoanSlipDAO {
    private ReaderDAO readerDAO = new ReaderDAO();

    public LoanSlip findById(int id) {
        String sql1 = "SELECT ls.* FROM tblloanslip ls WHERE ls.id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql1)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return extractLoanSlipFromResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<LoanSlip> findByReaderId(int readerId) {
        List<LoanSlip> loanSlips = new ArrayList<>();
        String sql1 = "SELECT ls.* FROM tblloanslip ls WHERE ls.reader_id = ? ORDER BY ls.borrow_date DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql1)) {

            stmt.setInt(1, readerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                loanSlips.add(extractLoanSlipFromResultSet(rs));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return loanSlips;
    }

    public boolean insert(LoanSlip loanSlip) {
        String sql1 = "INSERT INTO tblloanslip (borrow_date, due_date, notes, reader_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(loanSlip.getBorrowDate()));
            stmt.setTimestamp(2, Timestamp.valueOf(loanSlip.getDueDate()));
            stmt.setString(3, loanSlip.getNotes());
            stmt.setInt(4, loanSlip.getReader().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    loanSlip.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private LoanSlip extractLoanSlipFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        LocalDateTime borrowDate = rs.getTimestamp("borrow_date").toLocalDateTime();
        LocalDateTime dueDate = rs.getTimestamp("due_date").toLocalDateTime();
        String notes = rs.getString("notes");
        int readerId = rs.getInt("reader_id");

        return new LoanSlip(id, borrowDate, dueDate, notes, readerDAO.findByUserId(readerId));
    }
}
