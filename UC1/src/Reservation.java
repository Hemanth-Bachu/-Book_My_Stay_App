/**
 * UseCase6RoomAllocationService
 *
 * This class demonstrates reservation confirmation and room allocation
 * in the "Book My Stay" Hotel Booking System (version 6.0).
 *
 * Booking requests are processed in FIFO order, inventory is updated,
 * and double-booking is prevented using a Set for allocated room IDs.
 *
 * Version: 6.0
 * Author: YourName
 */

import java.util.*;

// Reservation class
class Reservation {
    private String guestName;
    private String requestedRoomType;

    public Reservation(String guestName, String requestedRoomType) {
        this.guestName = guestName;
        this.requestedRoomType = requestedRoomType;
    }

    public String getGuestName() { return guestName; }
    public String getRequestedRoomType() { return requestedRoomType; }

    @Override
    public String toString() {
        return guestName + " → " + requestedRoomType;
    }
}

// Centralized inventory manager
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void registerRoomType(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public boolean decrementAvailability(String roomType) {
        int current = inventory.getOrDefault(roomType, 0);
        if (current > 0) {
            inventory.put(roomType, current - 1);
            return true;
        }
        return false;
    }
}

// Booking request queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void submitRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Booking request submitted: " + reservation);
    }

    public Reservation processNextRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Room allocation service
class RoomAllocationService {
    private RoomInventory inventory;
    private Map<String, Set<String>> allocatedRooms; // roomType → assigned roomIDs
    private int roomIdCounter;

    public RoomAllocationService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
        this.roomIdCounter = 100; // Starting room ID
    }

    // Allocate a room for a reservation
    public void allocateRoom(Reservation reservation) {
        String type = reservation.getRequestedRoomType();

        // Check availability
        if (inventory.getAvailability(type) <= 0) {
            System.out.println("No available rooms for " + type + " → Reservation FAILED for " +
                    reservation.getGuestName());
            return;
        }

        // Generate unique room ID
        String roomId = generateUniqueRoomId(type);

        // Decrement inventory
        boolean decremented = inventory.decrementAvailability(type);
        if (!decremented) {
            System.out.println("Inventory conflict occurred for " + type + " → Reservation FAILED");
            return;
        }

        // Confirm allocation
        allocatedRooms.computeIfAbsent(type, k -> new HashSet<>()).add(roomId);
        System.out.println("Reservation CONFIRMED: " + reservation.getGuestName() +
                " assigned Room ID: " + roomId + " (" + type + ")");
    }

    // Generate a unique room ID
    private String generateUniqueRoomId(String roomType) {
        return roomType.replaceAll("\\s", "") + "-" + (roomIdCounter++);
    }

    // Display all allocated rooms
    public void displayAllocatedRooms() {
        System.out.println("\nAllocated Rooms:");
        System.out.println("----------------");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " → " + entry.getValue());
        }
    }
}

// Main application
public class UseCase6RoomAllocationService {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("Book My Stay - Hotel Booking System (v6.0)");
        System.out.println("Reservation Confirmation & Room Allocation");
        System.out.println("=========================================\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();
        inventory.registerRoomType("Single Room", 2);
        inventory.registerRoomType("Double Room", 1);
        inventory.registerRoomType("Suite Room", 1);

        // Initialize booking queue
        BookingRequestQueue queue = new BookingRequestQueue();
        queue.submitRequest(new Reservation("Alice", "Single Room"));
        queue.submitRequest(new Reservation("Bob", "Double Room"));
        queue.submitRequest(new Reservation("Charlie", "Suite Room"));
        queue.submitRequest(new Reservation("Diana", "Single Room"));
        queue.submitRequest(new Reservation("Eve", "Double Room")); // Exceeds availability

        // Initialize allocation service
        RoomAllocationService allocator = new RoomAllocationService(inventory);

        // Process booking requests in FIFO order
        while (!queue.isEmpty()) {
            Reservation r = queue.processNextRequest();
            allocator.allocateRoom(r);
        }

        // Display final allocations
        allocator.displayAllocatedRooms();
    }
}