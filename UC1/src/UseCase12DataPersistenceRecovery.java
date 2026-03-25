import java.io.*;
import java.util.*;

// Reservation Class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType;
    }
}

// Wrapper class to persist system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("System state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No previous data found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) ois.readObject();
            System.out.println("System state loaded successfully.");
            return state;
        } catch (Exception e) {
            System.out.println("Error loading system state. Starting fresh.");
            return null;
        }
    }
}

// Booking System
class BookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();
    private List<Reservation> history = new ArrayList<>();
    private int counter = 100;

    public BookingSystem() {
        // Try loading previous state
        SystemState state = PersistenceService.load();

        if (state != null) {
            inventory = state.inventory;
            history = state.bookingHistory;
        } else {
            // Initialize fresh state
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 1);
        }
    }

    public void bookRoom(String guestName, String roomType) {
        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            System.out.println("Booking Failed: No rooms available.");
            return;
        }

        inventory.put(roomType, available - 1);

        String id = "RES" + (++counter);
        Reservation res = new Reservation(id, guestName, roomType);
        history.add(res);

        System.out.println("Booking Successful: " + res);
    }

    public void displayState() {
        System.out.println("\n--- Inventory ---");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }

        System.out.println("\n--- Booking History ---");
        for (Reservation r : history) {
            System.out.println(r);
        }
    }

    public void saveState() {
        SystemState state = new SystemState(inventory, history);
        PersistenceService.save(state);
    }
}

// Main Class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Simulate operations
        system.bookRoom("Arun", "Standard");
        system.bookRoom("Priya", "Deluxe");

        // Display current state
        system.displayState();

        // Save before shutdown
        system.saveState();

        System.out.println("\n--- Restart the program to see recovery ---");
    }
}