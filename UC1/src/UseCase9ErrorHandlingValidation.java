import java.util.*;

// Custom Exception for Invalid Booking
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation Class
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// Validator Class
class BookingValidator {

    private static final List<String> VALID_ROOM_TYPES =
            Arrays.asList("Standard", "Deluxe", "Suite");

    public static void validate(String guestName, String roomType, int availableRooms)
            throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        // Validate availability
        if (availableRooms <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }
}

// Booking System
class BookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();
    private int counter = 100;

    public BookingSystem() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 0); // intentionally zero to trigger validation
    }

    public Reservation bookRoom(String guestName, String roomType)
            throws InvalidBookingException {

        int available = inventory.getOrDefault(roomType, 0);

        // Fail-fast validation
        BookingValidator.validate(guestName, roomType, available);

        // Update inventory safely
        inventory.put(roomType, available - 1);

        String reservationId = "RES" + (++counter);

        return new Reservation(reservationId, guestName, roomType);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms: " + inventory.get(type));
        }
    }
}

// Main Class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        try {
            // Valid booking
            Reservation r1 = system.bookRoom("Arun", "Standard");
            System.out.println("Booking Successful: " + r1);

            // Invalid room type
            Reservation r2 = system.bookRoom("Priya", "Luxury");
            System.out.println("Booking Successful: " + r2);

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        try {
            // No availability case
            Reservation r3 = system.bookRoom("Rahul", "Suite");
            System.out.println("Booking Successful: " + r3);

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        try {
            // Empty name case
            Reservation r4 = system.bookRoom("", "Deluxe");
            System.out.println("Booking Successful: " + r4);

        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        // System continues running safely
        system.displayInventory();
    }
}