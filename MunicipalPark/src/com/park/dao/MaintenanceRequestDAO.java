package com.park.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.park.bean.MaintenanceRequest;
import com.park.util.DBUtil;

public class MaintenanceRequestDAO {

    public int generateRequestID() {
        String query = "SELECT MAINT_REQ_ID_SEQ.NEXTVAL FROM dual";

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Unable to generate Request ID");
    }

    public boolean insertRequest(MaintenanceRequest request) {
        try {
            Connection connection = DBUtil.getDBConnection();
            String sql = "INSERT INTO MAINT_REQ_TBL VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, request.getRequestID());
            ps.setString(2, request.getFacilityID());
            ps.setString(3, request.getReportedBy());
            ps.setString(4, request.getIssueDescription());
            ps.setString(5, request.getPriority());
            ps.setDate(6, request.getReportedDate());
            ps.setDate(7, null);
            ps.setString(8, "OPEN");

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateRequestStatus(int requestID, String status, Date resolvedDate) {
        try {
            Connection connection = DBUtil.getDBConnection();
            String query =
                "UPDATE MAINT_REQ_TBL SET STATUS = ?, RESOLVED_DATE = ? WHERE REQUEST_ID = ?";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, status);
            ps.setDate(2, resolvedDate);
            ps.setInt(3, requestID);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<MaintenanceRequest> findActiveRequestsByFacility(String facilityID) {
        List<MaintenanceRequest> list = new ArrayList<>();

        try {
            Connection connection = DBUtil.getDBConnection();
            String query =
                "SELECT * FROM MAINT_REQ_TBL WHERE FACILITY_ID = ? " +
                "AND STATUS IN ('OPEN','IN_PROGRESS')";

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, facilityID);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MaintenanceRequest req = new MaintenanceRequest();

                req.setRequestID(rs.getInt("REQUEST_ID"));
                req.setFacilityID(rs.getString("FACILITY_ID"));
                req.setReportedBy(rs.getString("REPORTED_BY"));
                req.setIssueDescription(rs.getString("ISSUE_DESCRIPTION"));
                req.setPriority(rs.getString("PRIORITY"));
                req.setReportedDate(rs.getDate("REPORTED_DATE"));
                req.setResolvedDate(rs.getDate("RESOLVED_DATE"));
                req.setStatus(rs.getString("STATUS"));

                list.add(req);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public MaintenanceRequest findRequest(int requestID) {

        MaintenanceRequest req = null;
        String sql = "SELECT * FROM MAINTENANCE_REQUEST_TBL WHERE REQUEST_ID = ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, requestID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                req = new MaintenanceRequest();
                req.setRequestID(rs.getInt("REQUEST_ID"));
                req.setFacilityID(rs.getString("FACILITY_ID"));
                req.setIssueDescription(rs.getString("ISSUE_DESCRIPTION"));
                req.setPriority(rs.getString("PRIORITY"));
                req.setStatus(rs.getString("STATUS"));
                req.setReportedDate(rs.getDate("REPORTED_DATE"));
                req.setResolvedDate(rs.getDate("RESOLVED_DATE"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return req;
    }


}
