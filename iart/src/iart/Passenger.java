package iart;

import org.graphstream.graph.Node;

import java.time.LocalTime;
import java.util.Comparator;

public class Passenger {
    private int id;     //Podemos pôr nome, dunno
    private String pickupSpot;  //nodeID
    private double distanceToAirport;
    private LocalTime takeoffTime;

    public Passenger(int id, String pickup, LocalTime takeoff){
        Node n1 = RoadMap.getGraph().getNode(pickup);
        Node n2 = RoadMap.getGraph().getNode(RoadMap.airport);

        this.id = id;
        this.pickupSpot = pickup;
        this.distanceToAirport = RoadMap.heuristic(n1, n2);
        this.takeoffTime = takeoff;
    }

    public String getPickupSpot() {
        return pickupSpot;
    }

    public int compareTo(Passenger p){
        if (this.distanceToAirport < p.distanceToAirport){
            return -1;
        }
        else if (this.distanceToAirport == p.distanceToAirport){
            return 0;
        }
        else return 1;
    }

    /**
     * Sorts by distance to airport (straight line)
     */
    public static class PassengerComparator implements Comparator<Passenger> {

        @Override
        public int compare(Passenger p1, Passenger p2){ return p1.compareTo(p2); }
    }

}
