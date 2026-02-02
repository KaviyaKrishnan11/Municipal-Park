package com.park.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.park.bean.Citizen;
import com.park.util.DBUtil;

public class CitizenDAO {

    public Citizen findCitizen(String citizenID) {
        Citizen citizen = null;

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps =
                 connection.prepareStatement(
                     "SELECT * FROM CITIZEN_TBL WHERE CITIZEN_ID = ?")) {

            ps.setString(1, citizenID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                citizen = new Citizen();
                citizen.setCitizenID(rs.getString("CITIZEN_ID"));
                citizen.setFullName(rs.getString("FULL_NAME"));
                citizen.setMobile(rs.getString("MOBILE"));
                citizen.setEmail(rs.getString("EMAIL"));
                citizen.setCity(rs.getString("CITY"));
                citizen.setStatus(rs.getString("STATUS"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return citizen;
    }

    public boolean insertCitizen(Citizen citizen) {

        if (findCitizen(citizen.getCitizenID()) != null) {
            return false;   // already exists → no insert
        }

        String sql =
            "INSERT INTO CITIZEN_TBL " +
            "(CITIZEN_ID, FULL_NAME, MOBILE, EMAIL, CITY, STATUS) " +
            "VALUES (?,?,?,?,?,?)";

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, citizen.getCitizenID());
            ps.setString(2, citizen.getFullName());
            ps.setString(3, citizen.getMobile());
            ps.setString(4, citizen.getEmail());
            ps.setString(5, citizen.getCity());
            ps.setString(6, citizen.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Citizen> viewAllCitizens() {
        List<Citizen> list = new ArrayList<>();

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps =
                 connection.prepareStatement("SELECT * FROM CITIZEN_TBL");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Citizen citizen = new Citizen();
                citizen.setCitizenID(rs.getString("CITIZEN_ID"));
                citizen.setFullName(rs.getString("FULL_NAME"));
                citizen.setMobile(rs.getString("MOBILE"));
                citizen.setEmail(rs.getString("EMAIL"));
                citizen.setCity(rs.getString("CITY"));
                citizen.setStatus(rs.getString("STATUS"));
                list.add(citizen);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateCitizenStatus(String citizenID, String status) {

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps =
                 connection.prepareStatement(
                     "UPDATE CITIZEN_TBL SET STATUS = ? WHERE CITIZEN_ID = ?")) {

            ps.setString(1, status);
            ps.setString(2, citizenID);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
