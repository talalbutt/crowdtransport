package com.coctelmental.android.project1886.common;

import com.coctelmental.android.project1886.common.util.JsonHandler;

public class TaxiDriver {

	private String dni;
	private String fullName;
	private String password;
	private String email;
	private String licenceNumber;
	private String carTradeMark;
	private String carModel;
	
	public TaxiDriver() {}
	
	public TaxiDriver(String dni, String fullName, String password, String email, String licenceNumber,
			String carTradeMark, String carModel) {
		this.dni = dni;
		this.fullName = fullName;
		this.password = password;
		this.email = email;
		this.licenceNumber = licenceNumber;
		this.carTradeMark = carTradeMark;
		this.carModel = carModel;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLicenceNumber() {
		return licenceNumber;
	}

	public void setLicenceNumber(String licenceNumber) {
		this.licenceNumber = licenceNumber;
	}

	public String getCarTradeMark() {
		return carTradeMark;
	}

	public void setCarTradeMark(String carTradeMark) {
		this.carTradeMark = carTradeMark;
	}

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	
	public String toString() {
		return String.format("dni: %s\n" +
							 "fullName: %s\n" +
							 "email: %s\n" +
							 "licence: %s\n" +
							 "carTradeMark: %s\n" +
							 "carModel: %s", dni, fullName, email, licenceNumber, carTradeMark, carModel );
	}

	public String toJson() {
		return JsonHandler.toJson(this);	
	}
	
}
