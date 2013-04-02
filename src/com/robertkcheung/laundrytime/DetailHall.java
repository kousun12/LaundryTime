package com.robertkcheung.laundrytime;

import java.util.Vector;

public class DetailHall {
	public Vector<Machine> machines;

	public DetailHall(Vector<Machine> machines) {
		this.machines = machines;
	}
	
	public DetailHall(){
		this.machines = new Vector<Machine>();
	}

	public Vector<Machine> getMachines() {
		return machines;
	}

	public void setMachines(Vector<Machine> machines) {
		this.machines = machines;
	}
	
	public void add(Machine m){
		machines.add(m);
	}

}
