import java.util.*;

// Add-On Service Class
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " (Rs." + cost + ")";
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost
    public double calculateTotalCost(String reservationId) {
        double total = 0;
        for (AddOnService s : getServices(reservationId)) {
            total += s.getCost();
        }
        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> list = getServices(reservationId);

        if (list.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Services for Reservation " + reservationId + ":");
        for (AddOnService s : list) {
            System.out.println("- " + s);
        }
    }
}

// Main Class
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        AddOnServiceManager manager = new AddOnServiceManager();

        // Sample Reservation ID (from previous booking system)
        String reservationId = "RES101";

        // Guest selects add-on services
        manager.addService(reservationId, new AddOnService("Breakfast", 300));
        manager.addService(reservationId, new AddOnService("Airport Pickup", 1000));
        manager.addService(reservationId, new AddOnService("Extra Bed", 700));

        // Display selected services
        manager.displayServices(reservationId);

        // Calculate total add-on cost
        double total = manager.calculateTotalCost(reservationId);
        System.out.println("Total Add-On Cost: Rs." + total);

        // Confirm no impact on booking
        System.out.println("Core booking and inventory remain unchanged.");
    }
}