package com.bridgelabz.addressbooksystem.dto;

import java.time.LocalDate;

public class Contact {
	int id;
	int addressBookId;
	String firstName;
	String lastName;
	String address;
	String city;
	String state;
	String zip;
	String phoneNumber;
	String email;
	LocalDate createdDate;

	public Contact() {

	}

	public Contact(int id, int addressBookId, String firstName, String lastName, String address, String city,
			String state, String zip, String phoneNumber, String email, LocalDate createdDate) {
		super();
		this.id = id;
		this.addressBookId = addressBookId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.createdDate = createdDate;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public int getAddressBookId() {
		return addressBookId;
	}

	public void setAddressBookId(int addressBookId) {
		this.addressBookId = addressBookId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", addressBookId=" + addressBookId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", address=" + address + ", city=" + city + ", state=" + state + ", zip=" + zip
				+ ", phoneNumber=" + phoneNumber + ", email=" + email + ", createdDate=" + createdDate + "]";
	}
}
