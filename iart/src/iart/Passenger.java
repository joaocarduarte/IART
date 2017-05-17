package iart;

import java.time.LocalTime;

public class Passenger {
    private int id;     //Podemos pôr nome, dunno
    private String pickupSpot;  //nodeID
    private LocalTime takeoffTime;

    public Passenger(int id, String pickup, LocalTime takeoff){
        this.id = id;

        this.pickupSpot = pickup;
        this.takeoffTime = takeoff;
    }

    public String getPickupSpot() {
        return pickupSpot;
    }

}
