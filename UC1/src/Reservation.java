/**
 * UseCase5BookingRequestQueue
 *
 * This class demonstrates handling booking requests in a
 * first-come-first-served manner in the "Book My Stay"
 * Hotel Booking System (version 5.0).
 *
 * Booking requests are queued fairly without mutating inventory.
 *
 * Version: 5.0
 * Author: YourName
 */

import java.util.LinkedList;
import java.util.Queue;

// Reservation class representing a guest's booking intent
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
        return "Reservation{" +
                "guestName='" + guestName + '\'' +
                ", requestedRoomType='" + requestedRoomType + '\'' +
                '}';
    }
}

// Booking request queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request to the queue
    public void submitRequest(Reservation reservation) {
        queue.offer(reservation); // FIFO insertion
        System.out.println("Booking request submitted: " + reservation.getGuestName() +
                " for " + reservation.getRequestedRoomType());
    }

    // Peek at the next request without removing
    public Reservation peekNextRequest() {
        return queue.peek();
    }

    // Process next request (for allocation in future use case)
    public Reservation processNextRequest() {
        return queue.poll(); // removes from queue
    }

    // Display all pending requests
    public void displayPendingRequests() {
        System.out.println("\nPending Booking Requests (FIFO):");
        System.out.println("--------------------------------");
        for (Reservation r : queue) {
            System.out.println(r.getGuestName() + " → " + r.getRequestedRoomType());
        }
        System.out.println();
    }

    // Check if queue is empty
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Main application
public class UseCase5BookingRequestQueue {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("Book My Stay - Hotel Booking System (v5.0)");
        System.out.println("Booking Request Queue (First-Come-First-Served)");
        System.out.println("=========================================\n");

        // Initialize booking request queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Simulate incoming booking requests
        requestQueue.submitRequest(new Reservation("Alice", "Single Room"));
        requestQueue.submitRequest(new Reservation("Bob", "Double Room"));
        requestQueue.submitRequest(new Reservation("Charlie", "Suite Room"));
        requestQueue.submitRequest(new Reservation("Diana", "Double Room"));

        // Display pending requests
        requestQueue.displayPendingRequests();

        // Peek at next request
        Reservation next = requestQueue.peekNextRequest();
        System.out.println("Next request to process: " + next.getGuestName() +
                " for " + next.getRequestedRoomType() + "\n");

        // Demonstrate processing one request
        Reservation processed = requestQueue.processNextRequest();
        System.out.println("Processed request: " + processed.getGuestName() +
                " for " + processed.getRequestedRoomType() + "\n");

        // Display pending requests after processing
        requestQueue.displayPendingRequests();
    }
}