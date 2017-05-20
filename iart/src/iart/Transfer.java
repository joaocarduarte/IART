package iart;

import java.util.ArrayList;

public class Transfer {
	public final static int CAPACITY = 7;
	private ArrayList<Passenger> passengers = new ArrayList<>();;

	public Transfer(){}

	public ArrayList<Passenger> getPassengers() {
		return passengers;
	}

	public boolean isFull(){
		return passengers.size() == CAPACITY;
	}

	public void addPassenger(Passenger p){
		if (!this.isFull()){
			passengers.add(p);
		}
	}
}
