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
    private LibrarianDAO librarianDAO = new LibrarianDAO();

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
        String sql1 = "INSERT INTO tblloanslip (borrow_date, due_date, status, return_date, fine, notes, reader_id, librarian_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(loanSlip.getBorrowDate()));
            stmt.setTimestamp(2, Timestamp.valueOf(loanSlip.getDueDate()));
            stmt.setString(3, loanSlip.getStatus());
            stmt.setTimestamp(4, loanSlip.getReturnDate() != null ? Timestamp.valueOf(loanSlip.getReturnDate()) : null);
            stmt.setFloat(5, loanSlip.getFine() != null ? loanSlip.getFine() : 0);
            stmt.setString(6, loanSlip.getNotes());
            stmt.setInt(7, loanSlip.getReader().getId());
            stmt.setInt(8, loanSlip.getLibrarian().getId());

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
        String status = rs.getString("status");
        LocalDateTime returnDate = rs.getTimestamp("return_date") != null ? rs.getTimestamp("return_date").toLocalDateTime() : null;
        float fine = rs.getFloat("fine");
        String notes = rs.getString("notes");
        int readerId = rs.getInt("reader_id");
        int librarianId = rs.getInt("librarian_id");

        return new LoanSlip(id, borrowDate, dueDate, status, returnDate, fine, notes, readerDAO.findByUserId(readerId), librarianDAO.findLibrarianById(librarianId));
    }
}
