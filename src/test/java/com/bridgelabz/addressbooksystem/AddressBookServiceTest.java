package com.bridgelabz.addressbooksystem;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bridgelabz.addressbooksystem.dao.AddressBookRepository;
import com.bridgelabz.addressbooksystem.dto.Contact;
import com.bridgelabz.addressbooksystem.exception.AddressBookException;
import com.bridgelabz.addressbooksystem.service.impl.AddressBookService;

@ExtendWith(MockitoExtension.class)
public class AddressBookServiceTest {

	@InjectMocks
	private AddressBookService mockAddressBookService;

	private AddressBookService addressBookService;

	@Mock
	AddressBookRepository mockAddressBookRepository;

	@BeforeEach
	public void initialize() {
		mockAddressBookService = new AddressBookService(mockAddressBookRepository);
		addressBookService = new AddressBookService();
	}

	@Test
	public void givenAContact_whenCalledAdd_shouldCreateContactInAddressBook() throws AddressBookException {
		Contact contact = new Contact();
		contact.setFirstName("leo");
		contact.setLastName("Jack");
		contact.setAddress("qatar");
		contact.setCity("jaipur");
		contact.setPhoneNumber("09877654");
		contact.setState("rajasthan");
		contact.setZip("123");
		contact.setAddressBookId(1);
		contact.setEmail("apc@123.com");
		Contact newContact = new Contact(1, contact.getAddressBookId(), contact.getFirstName(), contact.getLastName(),
				contact.getAddress(), contact.getCity(), contact.getState(), contact.getZip(), contact.getPhoneNumber(),
				contact.getEmail());
		Mockito.when(mockAddressBookRepository.addContactToAddressBook(Mockito.any(Contact.class)))
				.thenReturn(newContact);
		Contact result = mockAddressBookService.addContactToAddressBook(contact);
		assertTrue(result.getId() == 1);
		Mockito.verify(mockAddressBookRepository).addContactToAddressBook(Mockito.any(Contact.class));
	}

	@Test
	public void givenAddressBook_whenCalledDeleteByName_shouldDeleteContactInAddressBook() throws AddressBookException {
		Mockito.when(mockAddressBookRepository.deleteContactByName(Mockito.anyString())).thenReturn(1);
		int result = mockAddressBookService.deleteContactByName("leo");
		assertTrue(result == 1);
		Mockito.verify(mockAddressBookRepository).deleteContactByName(Mockito.anyString());
	}
}
