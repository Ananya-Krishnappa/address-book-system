package com.bridgelabz.addressbooksystem.service;

import com.bridgelabz.addressbooksystem.dto.AddressBook;
import com.bridgelabz.addressbooksystem.exception.AddressBookException;

public interface IAddressBookService {
	public AddressBook addContactToAddressBook(AddressBook addressBook) throws AddressBookException;
}
