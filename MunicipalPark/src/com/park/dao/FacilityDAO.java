package com.park.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.park.bean.Facility;
import com.park.util.DBUtil;

public class FacilityDAO {

    public boolean insertFacility(Facility facility) {

        String sql =
            "INSERT INTO FACILITY_TBL " +
            "(FACILITY_ID, FACILITY_NAME, FACILITY_TYPE, OPERATING_HOURS, LOCATION_DESCRIPTION, STATUS) " +
            "VALUES (?,?,?,?,?,?)";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, facility.getFacilityID());
            ps.setString(2, facility.getFacilityName());
            ps.setString(3, facility.getFacilityType());
            ps.setString(4, facility.getOperatingHours());
            ps.setString(5, facility.getLocationDescription());
            ps.setString(6, facility.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Facility findFacility(String facilityID) {

        Facility facility = null;
        String sql = "SELECT * FROM FACILITY_TBL WHERE FACILITY_ID = ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, facilityID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                facility = new Facility();
                facility.setFacilityID(rs.getString("FACILITY_ID"));
                facility.setFacilityName(rs.getString("FACILITY_NAME"));
                facility.setFacilityType(rs.getString("FACILITY_TYPE"));
                facility.setOperatingHours(rs.getString("OPERATING_HOURS"));
                facility.setLocationDescription(rs.getString("LOCATION_DESCRIPTION"));
                facility.setStatus(rs.getString("STATUS"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return facility;
    }

    public List<Facility> viewAllFacilities() {

        List<Facility> list = new ArrayList<>();
        String sql = "SELECT * FROM FACILITY_TBL";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Facility f = new Facility();
                f.setFacilityID(rs.getString("FACILITY_ID"));
                f.setFacilityName(rs.getString("FACILITY_NAME"));
                f.setFacilityType(rs.getString("FACILITY_TYPE"));
                f.setOperatingHours(rs.getString("OPERATING_HOURS"));
                f.setLocationDescription(rs.getString("LOCATION_DESCRIPTION"));
                f.setStatus(rs.getString("STATUS"));
                list.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateFacilityStatus(String facilityID, String status) {

        String sql = "UPDATE FACILITY_TBL SET STATUS = ? WHERE FACILITY_ID = ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, facilityID);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteFacility(String facilityID) {

        String sql = "DELETE FROM FACILITY_TBL WHERE FACILITY_ID = ?";

        try (Connection con = DBUtil.getDBConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, facilityID);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
