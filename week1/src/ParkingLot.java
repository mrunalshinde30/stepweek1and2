import java.util.*;

class ParkingSpot {

    String licensePlate;
    long entryTime;
    boolean occupied;

    ParkingSpot() {
        occupied = false;
    }
}

public class ParkingLot {

    ParkingSpot[] spots;
    int capacity = 500;
    int occupiedCount = 0;
    int totalProbes = 0;

    public ParkingLot() {
        spots = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++)
            spots[i] = new ParkingSpot();
    }

    // Hash function
    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % capacity;
    }

    // Park vehicle
    public void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (spots[index].occupied) {
            index = (index + 1) % capacity; // linear probing
            probes++;
        }

        spots[index].licensePlate = licensePlate;
        spots[index].entryTime = System.currentTimeMillis();
        spots[index].occupied = true;

        occupiedCount++;
        totalProbes += probes;

        System.out.println("Assigned spot #" + index + " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (spots[index].occupied) {

            if (spots[index].licensePlate.equals(licensePlate)) {

                long exitTime = System.currentTimeMillis();
                long duration = (exitTime - spots[index].entryTime) / 3600000;

                double fee = duration * 5.0;

                spots[index].occupied = false;
                occupiedCount--;

                System.out.println("Spot #" + index + " freed, Fee: $" + fee);
                return;
            }

            index = (index + 1) % capacity;
        }

        System.out.println("Vehicle not found");
    }

    // Parking statistics
    public void getStatistics() {

        double occupancy = (occupiedCount * 100.0) / capacity;
        double avgProbes = totalProbes / (double) occupiedCount;

        System.out.println("Occupancy: " + occupancy + "%");
        System.out.println("Average Probes: " + avgProbes);
    }


    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}