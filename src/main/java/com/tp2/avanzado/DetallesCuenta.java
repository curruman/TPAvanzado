package com.tp2.avanzado;

import java.util.ArrayList;
import java.util.List;


public class DetallesCuenta {
	private double amount;
	private String creditpolicy;
	private String id;
	private List<Positions> positions = new ArrayList<>();
	
	public List<Positions> getPositions() {
		return positions;
	}
	public void setPositions(List<Positions> positions) {
		this.positions = positions;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getCreditpolicy() {
		return creditpolicy;
	}
	public void setCreditpolicy(String creditpolicy) {
		this.creditpolicy = creditpolicy;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}
