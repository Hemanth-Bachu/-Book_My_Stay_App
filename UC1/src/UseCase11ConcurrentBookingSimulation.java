import java.util.*;

// Booking Request
class BookingRequest {
    String guestName;
    String roomType;

    public BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Thread-safe Booking System
class BookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();

    public BookingSystem() {
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
    }

    // Critical Section (synchronized)
    public synchronized void bookRoom(String guestName, String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            System.out.println(Thread.currentThread().getName() +
                    " booking for " + guestName + " (" + roomType + ")");

            // Simulate processing delay (to expose race conditions if not synchronized)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            inventory.put(roomType, available - 1);

            System.out.println("SUCCESS: " + guestName +
                    " booked " + roomType + " room. Remaining: " + (available - 1));
        } else {
            System.out.println("FAILED: No " + roomType +
                    " rooms available for " + guestName);
        }
    }

    public void displayInventory() {
        System.out.println("\nFinal Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private Queue<BookingRequest> queue;
    private BookingSystem system;

    public BookingProcessor(String name, Queue<BookingRequest> queue, BookingSystem system) {
        super(name);
        this.queue = queue;
        this.system = system;
    }

    @Override
    public void run() {

        while (true) {
            BookingRequest request;

            // Synchronize queue access
            synchronized (queue) {
                if (queue.isEmpty()) {
                    break;
                }
                request = queue.poll();
            }

            if (request != null) {
                system.bookRoom(request.guestName, request.roomType);
            }
        }
    }
}

// Main Class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BookingSystem system = new BookingSystem();

        // Shared booking queue
        Queue<BookingRequest> queue = new LinkedList<>();

        // Simulated concurrent requests
        queue.add(new BookingRequest("Arun", "Standard"));
        queue.add(new BookingRequest("Priya", "Standard"));
        queue.add(new BookingRequest("Rahul", "Standard")); // extra request
        queue.add(new BookingRequest("Neha", "Deluxe"));
        queue.add(new BookingRequest("Kiran", "Deluxe"));   // extra request

        // Multiple threads (guests)
        Thread t1 = new BookingProcessor("Thread-1", queue, system);
        Thread t2 = new BookingProcessor("Thread-2", queue, system);
        Thread t3 = new BookingProcessor("Thread-3", queue, system);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Final inventory
        system.displayInventory();
    }
}