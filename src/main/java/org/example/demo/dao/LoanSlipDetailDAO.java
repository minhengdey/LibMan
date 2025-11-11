package org.example.demo.dao;

import org.example.demo.model.DocumentCopy;
import org.example.demo.model.LoanSlip;
import org.example.demo.model.LoanSlipDetail;
import org.example.demo.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanSlipDetailDAO {
    private LoanSlipDAO loanSlipDAO = new LoanSlipDAO();
    private DocumentCopyDAO documentCopyDAO = new DocumentCopyDAO();

    public List<LoanSlipDetail> findByLoanSlipId(int loanSlipId) {
        List<LoanSlipDetail> details = new ArrayList<>();
        String sql = "SELECT lsd.*, dc.barcode " +
                "FROM tblloanslipdetail lsd " +
                "JOIN tblloanslip ls ON lsd.loan_slip_id = ls.id " +
                "JOIN tbldocumentcopy dc ON lsd.document_copy_id = dc.barcode " +
                "WHERE lsd.loan_slip_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, loanSlipId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LoanSlipDetail detail = extractLoanSlipDetailFromResultSet(rs);
                detail.setLoanSlip(loanSlipDAO.findById(loanSlipId));
                detail.setDocumentCopy(documentCopyDAO.findByCode(rs.getString("dc.barcode")));
                details.add(detail);
            }
            return details;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return details;
    }

    public boolean insert(LoanSlipDetail detail) {
        String sql = "INSERT INTO tblloanslipdetail (loan_slip_id, document_copy_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, detail.getLoanSlip().getId());
            stmt.setString(2, detail.getDocumentCopy().getBarcode());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    detail.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public LoanSlipDetail extractLoanSlipDetailFromResultSet(ResultSet rs) throws SQLException {
        LoanSlipDetail loanSlipDetail = new LoanSlipDetail();
        loanSlipDetail.setId(rs.getInt("lsd.id"));

        return loanSlipDetail;
    }
}
