package iart;

public class Transfer {
	private final int CAPACITY = 7;
	private int ocupation;

	public Transfer(int ocupation){
		this.ocupation = ocupation;
	}

	public Transfer(){
		this.ocupation = 0;
	}

	public int getOcupation() {
		return ocupation;
	}

	public void setOcupation(int ocupation) {
		this.ocupation = ocupation;
	}

	public boolean isFull(){
		return ocupation == CAPACITY;
	}

	public void addPassenger(){
		if (!this.isFull()){
			ocupation++;
		}
	}
}
