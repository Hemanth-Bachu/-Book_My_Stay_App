/**
 * UseCase2RoomInitialization
 *
 * This class demonstrates the initialization of different room types
 * in the "Book My Stay" Hotel Booking application (version 2.1).
 * It introduces object-oriented concepts such as abstraction, inheritance,
 * and polymorphism, and prints room details with static availability.
 *
 * Version: 2.1
 * Author: YourName
 */

// Abstract class for all rooms
abstract class Room {
    private String roomType;
    private int numberOfBeds;
    private double pricePerNight;

    public Room(String roomType, int numberOfBeds, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.pricePerNight = pricePerNight;
    }

    // Abstract method to get description (to be implemented by concrete classes)
    public abstract String getDescription();

    // Getters
    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }
}

// Concrete room classes
class SingleRoom extends Room {
    public SingleRoom(double pricePerNight) {
        super("Single Room", 1, pricePerNight);
    }

    @Override
    public String getDescription() {
        return "A cozy room for one guest.";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom(double pricePerNight) {
        super("Double Room", 2, pricePerNight);
    }

    @Override
    public String getDescription() {
        return "A comfortable room for two guests.";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom(double pricePerNight) {
        super("Suite Room", 3, pricePerNight);
    }

    @Override
    public String getDescription() {
        return "A luxurious suite with premium amenities.";
    }
}

// Main application entry
public class UseCase2RoomInitialization {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("Book My Stay - Hotel Booking System (v2.1)");
        System.out.println("Initializing Room Types and Availability");
        System.out.println("=========================================\n");

        // Initialize room objects
        Room single = new SingleRoom(50.0);
        Room doubleRoom = new DoubleRoom(90.0);
        Room suite = new SuiteRoom(150.0);

        // Static availability
        int availableSingle = 5;
        int availableDouble = 3;
        int availableSuite = 2;

        // Print details
        printRoomDetails(single, availableSingle);
        printRoomDetails(doubleRoom, availableDouble);
        printRoomDetails(suite, availableSuite);
    }

    // Helper method to print room info
    private static void printRoomDetails(Room room, int availability) {
        System.out.println("Room Type: " + room.getRoomType());
        System.out.println("Description: " + room.getDescription());
        System.out.println("Beds: " + room.getNumberOfBeds());
        System.out.println("Price per Night: $" + room.getPricePerNight());
        System.out.println("Available Rooms: " + availability);
        System.out.println("-----------------------------------------\n");
    }
}