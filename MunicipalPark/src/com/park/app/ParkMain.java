package com.park.app;
import com.park.bean.Citizen;
import com.park.bean.Facility;
import com.park.service.ParkService;
import com.park.util.FacilityUnavailableException;
import com.park.util.ValidationException;

public class ParkMain {
private static ParkService service = new ParkService();
public static void main(String[] args) {
java.util.Scanner sc = new java.util.Scanner(System.in);
System.out.println("--- Municipal Public Park Facility Booking Console ---");
try {
Facility f = new Facility();
f.setFacilityID("FC70");
f.setFacilityName("Skating Rink A");
f.setFacilityType("COURT");
f.setOperatingHours("06:00-20:00");
f.setLocationDescription("Skyline Park – West Block");
f.setStatus("ACTIVE");
boolean ok = service.registerFacility(f);
System.out.println(ok ? "FACILITY REGISTERED" : "FACILITY REGISTRATION FAILED");
} catch (ValidationException e) {
System.out.println("Validation Error: " + e.toString());
} catch (Exception e) {
System.out.println("System Error: " + e.getMessage());
}
try {
Citizen c = new Citizen();
c.setCitizenID("CT7070");
c.setFullName("Meenakshi");
c.setMobile("9998887771");
c.setEmail("meenakshi@example.com");
c.setCity("Chennai");
c.setStatus("ACTIVE");
boolean ok = service.registerCitizen(c);
System.out.println(ok ? "CITIZEN REGISTERED" : "CITIZEN REGISTRATION FAILED");
} catch (ValidationException e) {
System.out.println("Validation Error: " + e.toString());
} catch (Exception e) {
System.out.println("System Error: " + e.getMessage());
}
try {
java.sql.Date d = new java.sql.Date(System.currentTimeMillis());
boolean ok = service.bookFacility("FC70", "CT7070", d, "10:00-11:00");
System.out.println(ok ? "BOOKING CREATED" : "BOOKING FAILED");
} catch (FacilityUnavailableException e) {
System.out.println("Facility Error: " + e.toString());
} catch (ValidationException e) {
System.out.println("Validation Error: " + e.toString());
} catch (Exception e) {
System.out.println("System Error: " + e.getMessage());
}
sc.close();
}
}


