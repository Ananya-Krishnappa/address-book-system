package com.bridgelabz.addressbooksystem.service;

import java.util.List;

import com.bridgelabz.addressbooksystem.dto.Contact;
import com.bridgelabz.addressbooksystem.exception.AddressBookException;

public interface IAddressBookService {
	public Contact addContactToAddressBook(Contact contact) throws AddressBookException;

	public int deleteContactByName(String name) throws AddressBookException;

	public List<Contact> addMultipleContactsToAddressBook(List<Contact> contactList) throws AddressBookException;

	public List<Contact> searchPersonByCity(String city) throws AddressBookException;
}
