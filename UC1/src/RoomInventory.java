/**
 * UseCase3InventorySetup
 *
 * This class demonstrates centralized room inventory management
 * in the "Book My Stay" Hotel Booking System (version 3.1).
 * It replaces scattered availability variables with a HashMap,
 * providing a single source of truth and constant-time lookups.
 *
 * Version: 3.1
 * Author: YourName
 */

import java.util.HashMap;
import java.util.Map;

// Inventory manager class
class RoomInventory {
    private Map<String, Integer> inventory;

    // Constructor initializes room availability
    public RoomInventory() {
        inventory = new HashMap<>();
    }

    // Register a new room type with initial count
    public void registerRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Retrieve availability for a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability for a room type (positive or negative delta)
    public void updateAvailability(String roomType, int delta) {
        int current = inventory.getOrDefault(roomType, 0);
        int updated = current + delta;
        if (updated < 0) updated = 0; // prevent negative availability
        inventory.put(roomType, updated);
    }

    // Display current inventory
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        System.out.println("----------------------");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " available");
        }
        System.out.println();
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

// Main application
public class UseCase3InventorySetup {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("Book My Stay - Hotel Booking System (v3.1)");
        System.out.println("Centralized Room Inventory Setup");
        System.out.println("=========================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Register room types
        inventory.registerRoomType("Single Room", 5);
        inventory.registerRoomType("Double Room", 3);
        inventory.registerRoomType("Suite Room", 2);

        // Display initial inventory
        inventory.displayInventory();

        // Simulate updates
        System.out.println("Booking one Single Room and one Suite Room...\n");
        inventory.updateAvailability("Single Room", -1);
        inventory.updateAvailability("Suite Room", -1);

        // Display updated inventory
        inventory.displayInventory();

        // Add new room type
        System.out.println("Adding new room type: Deluxe Room...\n");
        inventory.registerRoomType("Deluxe Room", 4);
        inventory.displayInventory();
    }
}