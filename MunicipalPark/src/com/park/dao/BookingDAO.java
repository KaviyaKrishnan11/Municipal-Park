package com.park.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.park.bean.Booking;
import com.park.util.DBUtil;

public class BookingDAO {
    public int generateBookingID() {
        String query = "SELECT BOOKING_ID_SEQ.NEXTVAL FROM dual";

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Unable to generate Booking ID");
    }
    public boolean insertBooking(Booking booking) {
        String query =
            "INSERT INTO BOOKING_TBL " +
            "(BOOKING_ID, FACILITY_ID, CITIZEN_ID, BOOKING_DATE, TIME_SLOT, STATUS) " +
            "VALUES (?,?,?,?,?,?)";

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, booking.getBookingID());
            ps.setString(2, booking.getFacilityID());
            ps.setString(3, booking.getCitizenID());
            ps.setDate(4, new Date(booking.getBookingDate().getTime()));
            ps.setString(5, booking.getTimeSlot());
            ps.setString(6, booking.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean cancelBooking(int bookingID) {
        String query =
            "UPDATE BOOKING_TBL SET STATUS = 'CANCELLED' WHERE BOOKING_ID = ?";

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, bookingID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public Booking findBooking(int bookingID) {
        Booking booking = null;
        String query = "SELECT * FROM BOOKING_TBL WHERE BOOKING_ID = ?";

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                booking = new Booking();
                booking.setBookingID(rs.getInt("BOOKING_ID"));
                booking.setFacilityID(rs.getString("FACILITY_ID"));
                booking.setCitizenID(rs.getString("CITIZEN_ID"));
                booking.setBookingDate(rs.getDate("BOOKING_DATE"));
                booking.setTimeSlot(rs.getString("TIME_SLOT"));
                booking.setStatus(rs.getString("STATUS"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }
    public List<Booking> findConflicts(String facilityID, Date bookingDate, String timeSlot) {
        List<Booking> list = new ArrayList<>();

        String query =
            "SELECT * FROM BOOKING_TBL " +
            "WHERE FACILITY_ID = ? AND BOOKING_DATE = ? " +
            "AND TIME_SLOT = ? AND STATUS = 'BOOKED'";

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, facilityID);
            ps.setDate(2, bookingDate);
            ps.setString(3, timeSlot);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingID(rs.getInt("BOOKING_ID"));
                booking.setFacilityID(rs.getString("FACILITY_ID"));
                booking.setCitizenID(rs.getString("CITIZEN_ID"));
                booking.setBookingDate(rs.getDate("BOOKING_DATE"));
                booking.setTimeSlot(rs.getString("TIME_SLOT"));
                booking.setStatus(rs.getString("STATUS"));

                list.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Booking> findActiveBookingsByFacility(String facilityID) {
        List<Booking> list = new ArrayList<>();

        String query =
            "SELECT * FROM BOOKING_TBL WHERE FACILITY_ID = ? AND STATUS = 'BOOKED'";

        try (Connection connection = DBUtil.getDBConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, facilityID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingID(rs.getInt("BOOKING_ID"));
                booking.setFacilityID(rs.getString("FACILITY_ID"));
                booking.setCitizenID(rs.getString("CITIZEN_ID"));
                booking.setBookingDate(rs.getDate("BOOKING_DATE"));
                booking.setTimeSlot(rs.getString("TIME_SLOT"));
                booking.setStatus(rs.getString("STATUS"));

                list.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
