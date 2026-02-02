package com.park.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import com.park.bean.Booking;
import com.park.bean.Citizen;
import com.park.bean.Facility;
import com.park.bean.MaintenanceRequest;
import com.park.dao.BookingDAO;
import com.park.dao.CitizenDAO;
import com.park.dao.FacilityDAO;
import com.park.dao.MaintenanceRequestDAO;
import com.park.util.ActiveRecordsExistException;
import com.park.util.DBUtil;
import com.park.util.FacilityUnavailableException;
import com.park.util.ValidationException;

public class ParkService {

    private FacilityDAO facilityDAO = new FacilityDAO();
    private CitizenDAO citizenDAO = new CitizenDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private MaintenanceRequestDAO maintenanceRequestDAO =
            new MaintenanceRequestDAO();
    public boolean registerFacility(Facility facility)
            throws ValidationException {

        if (facility == null ||
            facility.getFacilityID() == null ||
            facility.getFacilityName() == null ||
            facility.getFacilityType() == null ||
            facility.getOperatingHours() == null) {
            throw new ValidationException();
        }

        if (facility.getStatus() == null) {
            facility.setStatus("ACTIVE");
        }

        return facilityDAO.insertFacility(facility);
    }

    public boolean registerCitizen(Citizen citizen)
            throws ValidationException {

        if (citizen == null ||
            citizen.getCitizenID() == null ||
            citizen.getFullName() == null ||
            citizen.getMobile() == null) {
            throw new ValidationException();
        }

        if (citizen.getStatus() == null) {
            citizen.setStatus("ACTIVE");
        }

        return citizenDAO.insertCitizen(citizen);
    }

    public boolean bookFacility(String facilityID, String citizenID,
                                Date bookingDate, String timeSlot)
            throws FacilityUnavailableException, ValidationException {

        if (facilityID == null || citizenID == null ||
            bookingDate == null || timeSlot == null) {
            throw new ValidationException();
        }

        Facility facility = facilityDAO.findFacility(facilityID);
        Citizen citizen = citizenDAO.findCitizen(citizenID);

        if (facility == null || !"ACTIVE".equals(facility.getStatus()) ||
            citizen == null || !"ACTIVE".equals(citizen.getStatus())) {
            throw new FacilityUnavailableException();
        }

        if (!bookingDAO
                .findConflicts(facilityID, bookingDate, timeSlot)
                .isEmpty()) {
            throw new FacilityUnavailableException();
        }

        Booking booking = new Booking();
        booking.setBookingID(bookingDAO.generateBookingID());
        booking.setFacilityID(facilityID);
        booking.setCitizenID(citizenID);
        booking.setBookingDate(bookingDate);
        booking.setTimeSlot(timeSlot);
        booking.setStatus("BOOKED");

        return bookingDAO.insertBooking(booking);
    }

    public boolean cancelBooking(int bookingID)
            throws ValidationException {

        if (bookingID <= 0) {
            throw new ValidationException();
        }

        Booking booking = bookingDAO.findBooking(bookingID);
        if (booking == null) {
            return false;
        }

        if (!"BOOKED".equals(booking.getStatus())) {
            return false;
        }

        return bookingDAO.cancelBooking(bookingID);
    }

    public boolean logMaintenanceRequest(MaintenanceRequest request)
            throws ValidationException {

        if (request == null ||
            request.getFacilityID() == null ||
            request.getIssueDescription() == null ||
            request.getPriority() == null) {
            throw new ValidationException();
        }

        Facility facility =
                facilityDAO.findFacility(request.getFacilityID());
        if (facility == null) {
            throw new ValidationException();
        }

        request.setRequestID(
                maintenanceRequestDAO.generateRequestID());
        request.setStatus("OPEN");
        request.setReportedDate(
                new Date(System.currentTimeMillis()));

        return maintenanceRequestDAO.insertRequest(request);
    }

    public boolean markMaintenanceCompleted(int requestID, Date resolvedDate)
            throws ValidationException {

        if (requestID <= 0 || resolvedDate == null) {
            throw new ValidationException();
        }

        MaintenanceRequest req =
                maintenanceRequestDAO.findRequest(requestID);

        if (req == null ||
            "COMPLETED".equals(req.getStatus()) ||
            "CANCELLED".equals(req.getStatus())) {
            return false;
        }

        return maintenanceRequestDAO
                .updateRequestStatus(requestID,
                                     "COMPLETED",
                                     resolvedDate);
    }

    public boolean removeFacility(String facilityID)
            throws ValidationException, ActiveRecordsExistException {

        if (facilityID == null || facilityID.isBlank()) {
            throw new ValidationException();
        }

        if (!bookingDAO
                .findActiveBookingsByFacility(facilityID)
                .isEmpty()) {
            throw new ActiveRecordsExistException();
        }

        if (!maintenanceRequestDAO
                .findActiveRequestsByFacility(facilityID)
                .isEmpty()) {
            throw new ActiveRecordsExistException();
        }

        return facilityDAO.deleteFacility(facilityID);
    }
}
