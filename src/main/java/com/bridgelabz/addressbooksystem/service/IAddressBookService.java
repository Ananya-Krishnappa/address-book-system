package com.bridgelabz.addressbooksystem.service;

import com.bridgelabz.addressbooksystem.dto.Contact;
import com.bridgelabz.addressbooksystem.exception.AddressBookException;

public interface IAddressBookService {
	public Contact addContactToAddressBook(Contact contact) throws AddressBookException;
}
