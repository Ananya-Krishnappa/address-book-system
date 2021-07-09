/**
 * Purpose:Address book JDBC connection
 * @author Ananya K
 * @version 1.0
 * @since 08/07/2021
 * 
 */
package com.bridgelabz.addressbooksystem.service.impl;

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
}
