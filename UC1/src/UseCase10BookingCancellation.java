import java.util.*;

// Reservation Class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Status: " + (isCancelled ? "Cancelled" : "Active");
    }
}

// Booking System
class BookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Stack<String>> roomPool = new HashMap<>();
    private Map<String, Reservation> reservations = new HashMap<>();
    private Stack<String> rollbackStack = new Stack<>();

    private int counter = 100;

    public BookingSystem() {
        // Initialize inventory
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);

        // Initialize room IDs
        roomPool.put("Standard", new Stack<>());
        roomPool.put("Deluxe", new Stack<>());

        roomPool.get("Standard").push("S1");
        roomPool.get("Standard").push("S2");

        roomPool.get("Deluxe").push("D1");
        roomPool.get("Deluxe").push("D2");
    }

    // Book room
    public Reservation bookRoom(String guestName, String roomType) {
        if (!inventory.containsKey(roomType) || inventory.get(roomType) <= 0) {
            System.out.println("Booking Failed: No rooms available.");
            return null;
        }

        String roomId = roomPool.get(roomType).pop();
        inventory.put(roomType, inventory.get(roomType) - 1);

        String reservationId = "RES" + (++counter);
        Reservation res = new Reservation(reservationId, guestName, roomType, roomId);

        reservations.put(reservationId, res);

        System.out.println("Booking Successful: " + res);
        return res;
    }

    // Cancel booking
    public void cancelBooking(String reservationId) {

        // Validate existence
        if (!reservations.containsKey(reservationId)) {
            System.out.println("Cancellation Failed: Reservation not found.");
            return;
        }

        Reservation res = reservations.get(reservationId);

        // Prevent duplicate cancellation
        if (res.isCancelled()) {
            System.out.println("Cancellation Failed: Already cancelled.");
            return;
        }

        // Step 1: Record for rollback
        rollbackStack.push(res.getRoomId());

        // Step 2: Restore inventory
        String roomType = res.getRoomType();
        inventory.put(roomType, inventory.get(roomType) + 1);

        // Step 3: Return room to pool (LIFO)
        roomPool.get(roomType).push(res.getRoomId());

        // Step 4: Mark as cancelled
        res.cancel();

        System.out.println("Cancellation Successful for " + reservationId);
    }

    public void displayInventory() {
        System.out.println("\nInventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }

    public void displayReservations() {
        System.out.println("\nReservations:");
        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }
}

// Main Class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Book rooms
        Reservation r1 = system.bookRoom("Arun", "Standard");
        Reservation r2 = system.bookRoom("Priya", "Deluxe");

        system.displayInventory();

        // Cancel booking
        if (r1 != null) {
            system.cancelBooking(r1.getReservationId());
        }

        // Try invalid cancellation
        system.cancelBooking("RES999");

        // Try duplicate cancellation
        if (r1 != null) {
            system.cancelBooking(r1.getReservationId());
        }

        system.displayInventory();
        system.displayReservations();
    }
}