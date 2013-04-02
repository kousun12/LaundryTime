package com.robertkcheung.laundrytime;

public class Machine {
	public String status;
	public int machineNumber;
	public String time;
	
	public Machine(int num, String stat, String t){
		machineNumber = num;
		status = stat;
		time = t;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getMachineNumber() {
		return machineNumber;
	}

	public void setMachineNumber(int machineNumber) {
		this.machineNumber = machineNumber;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	

}
