import java.util.*;

// Reservation Class (Basic Model)
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double amount;

    public Reservation(String reservationId, String guestName, String roomType, double amount) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.amount = amount;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Amount: Rs." + amount;
    }
}

// Booking History (Stores confirmed bookings)
class BookingHistory {

    private List<Reservation> history = new ArrayList<>();

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Get all reservations
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Reporting Service
class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("No booking history available.");
            return;
        }

        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n--- Booking Summary Report ---");

        int totalBookings = reservations.size();
        double totalRevenue = 0;

        for (Reservation r : reservations) {
            totalRevenue += r.getAmount();
        }

        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: Rs." + totalRevenue);
    }
}

// Main Class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulating confirmed bookings
        Reservation r1 = new Reservation("RES101", "Arun", "Deluxe", 2500);
        Reservation r2 = new Reservation("RES102", "Priya", "Suite", 4000);
        Reservation r3 = new Reservation("RES103", "Rahul", "Standard", 1500);

        // Add to booking history
        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Admin views booking history
        reportService.showAllBookings(history.getAllReservations());

        // Admin generates summary report
        reportService.generateSummary(history.getAllReservations());

        // Ensure no modification
        System.out.println("\nBooking history remains unchanged after reporting.");
    }
}
