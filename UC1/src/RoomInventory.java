/**
 * UseCase4RoomSearch
 *
 * This class demonstrates read-only room search functionality
 * in the "Book My Stay" Hotel Booking System (version 4.1).
 * Guests can view available rooms and their details without
 * modifying inventory state.
 *
 * Version: 4.1
 * Author: YourName
 */

import java.util.HashMap;
import java.util.Map;

// Centralized inventory manager (read & write methods)
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register new room type with initial availability
    public void registerRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Read-only: Get current availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Write: Update availability (used by booking, not search)
    public void updateAvailability(String roomType, int delta) {
        int current = inventory.getOrDefault(roomType, 0);
        int updated = current + delta;
        if (updated < 0) updated = 0;
        inventory.put(roomType, updated);
    }

    // Read-only: Get all room types
    public Map<String, Integer> getAllRoomAvailability() {
        return new HashMap<>(inventory); // Defensive copy
    }
}

// Abstract room class
abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.numberOfBeds = beds;
        this.pricePerNight = price;
    }

    public String getRoomType() { return roomType; }
    public int getNumberOfBeds() { return numberOfBeds; }
    public double getPricePerNight() { return pricePerNight; }
    public abstract String getDescription();
}

// Concrete room classes
class SingleRoom extends Room {
    public SingleRoom(double price) { super("Single Room", 1, price); }
    @Override public String getDescription() { return "A cozy room for one guest."; }
}

class DoubleRoom extends Room {
    public DoubleRoom(double price) { super("Double Room", 2, price); }
    @Override public String getDescription() { return "A comfortable room for two guests."; }
}

class SuiteRoom extends Room {
    public SuiteRoom(double price) { super("Suite Room", 3, price); }
    @Override public String getDescription() { return "A luxurious suite with premium amenities."; }
}

// Search service (read-only access)
class RoomSearchService {
    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    // Display available rooms
    public void displayAvailableRooms() {
        System.out.println("Available Rooms for Booking:");
        System.out.println("----------------------------");

        for (Map.Entry<String, Room> entry : roomCatalog.entrySet()) {
            String type = entry.getKey();
            Room room = entry.getValue();
            int available = inventory.getAvailability(type);

            if (available > 0) {
                System.out.println("Room Type: " + type);
                System.out.println("Description: " + room.getDescription());
                System.out.println("Beds: " + room.getNumberOfBeds());
                System.out.println("Price per Night: $" + room.getPricePerNight());
                System.out.println("Available: " + available);
                System.out.println("----------------------------");
            }
        }
    }
}

// Main application
public class UseCase4RoomSearch {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("Book My Stay - Hotel Booking System (v4.1)");
        System.out.println("Room Search & Availability Check");
        System.out.println("=========================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.registerRoomType("Single Room", 5);
        inventory.registerRoomType("Double Room", 3);
        inventory.registerRoomType("Suite Room", 0); // Suite unavailable

        // Initialize room catalog
        Map<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom(50.0));
        roomCatalog.put("Double Room", new DoubleRoom(90.0));
        roomCatalog.put("Suite Room", new SuiteRoom(150.0));

        // Search service
        RoomSearchService searchService = new RoomSearchService(inventory, roomCatalog);

        // Display available rooms (read-only)
        searchService.displayAvailableRooms();
    }
}