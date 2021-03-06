/**
 * Purpose:Address book JDBC connection
 * @author Ananya K
 * @version 1.0
 * @since 09/07/2021
 * 
 */
package com.bridgelabz.addressbooksystem.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bridgelabz.addressbooksystem.dao.AddressBookRepository;
import com.bridgelabz.addressbooksystem.dto.Contact;
import com.bridgelabz.addressbooksystem.exception.AddressBookException;
import com.bridgelabz.addressbooksystem.service.IAddressBookService;

public class AddressBookService implements IAddressBookService {
	private static final Logger LOG = LogManager.getLogger(AddressBookService.class);
	private AddressBookRepository addressBookRepository;

	public AddressBookService() {
		addressBookRepository = AddressBookRepository.getInstance();
	}

	public AddressBookService(AddressBookRepository addressBookRepository) {
		this.addressBookRepository = addressBookRepository;
	}

	@Override
	/**
	 * Function to add contacts to address book
	 */
	public Contact addContactToAddressBook(Contact contact) throws AddressBookException {
		try {
			Contact newContact = addressBookRepository.addContactToAddressBook(contact);
			return newContact;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	/**
	 * Function to delete contact by name
	 */
	@Override
	public int deleteContactByName(String name) throws AddressBookException {
		try {
			int result = addressBookRepository.deleteContactByName(name);
			return result;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	/**
	 * Function to add multiple contacts to address book
	 */
	@Override
	public List<Contact> addMultipleContactsToAddressBook(List<Contact> contactList) throws AddressBookException {
		try {
			List<Contact> newcontactList = addressBookRepository.addMultipleContactsToAddressBook(contactList);
			return newcontactList;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	/**
	 * Function to search person by city
	 */
	@Override
	public List<Contact> searchPersonByCity(String city) throws AddressBookException {
		try {
			List<Contact> result = addressBookRepository.searchPersonByCity(city);
			return result;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	/**
	 * Function to search person by created date
	 */
	@Override
	public List<Contact> searchByCreatedDate(LocalDate startDate, LocalDate endDate) throws AddressBookException {
		try {
			List<Contact> result = addressBookRepository.searchByCreatedDate(startDate, endDate);
			return result;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}
}
