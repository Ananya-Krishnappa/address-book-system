package com.bridgelabz.addressbooksystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
import com.bridgelabz.addressbooksystem.exception.JdbcConnectorException;
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
		contact.setCreatedDate(LocalDate.now());
		Contact newContact = new Contact(1, contact.getAddressBookId(), contact.getFirstName(), contact.getLastName(),
				contact.getAddress(), contact.getCity(), contact.getState(), contact.getZip(), contact.getPhoneNumber(),
				contact.getEmail(), contact.getCreatedDate());
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

	@Test
	public void givenMultipleContacts_whenCalledCreateContactInAddressBook_shouldDoBatchInserts()
			throws AddressBookException, JdbcConnectorException, SQLException {
		List<Contact> contactList = new ArrayList<Contact>();
		Contact contact = createContact(1, "goa", "Gurgoan", "983635242", "qwe@123.gmail.com", "Rocky", "Hari",
				"Karnataka", "1267", LocalDate.now());
		Contact contact1 = createContact(1, "uyrg", "kerala", "9836399242", "kjhg@123.gmail.com", "Warner", "Kay",
				"Kerala", "127", LocalDate.now());
		contactList.add(contact);
		contactList.add(contact1);

		List<Contact> expectedContactList = new ArrayList<Contact>(contactList);
		AtomicInteger index = new AtomicInteger();
		expectedContactList.stream().map(c -> setIdToContact(c, index)).collect(Collectors.toList());
		Mockito.when(mockAddressBookRepository.addMultipleContactsToAddressBook(Mockito.anyList()))
				.thenReturn(expectedContactList);
		List<Contact> actualContactList = mockAddressBookService.addMultipleContactsToAddressBook(contactList);
		assertEquals(expectedContactList.size(), actualContactList.size());
		Mockito.verify(mockAddressBookRepository).addMultipleContactsToAddressBook(Mockito.anyList());
	}

	/**
	 * Function to create contact
	 * 
	 * @param address
	 * @param city
	 * @param phoneNum
	 * @param email
	 * @param firstName
	 * @param lastName
	 * @param state
	 * @param zip
	 * @return Contact
	 */
	private Contact createContact(int addressBookId, String address, String city, String phoneNum, String email,
			String firstName, String lastName, String state, String zip, LocalDate createdDate) {
		Contact contact = new Contact();
		contact.setAddressBookId(addressBookId);
		contact.setAddress(address);
		contact.setCity(city);
		contact.setEmail(email);
		contact.setFirstName(firstName);
		contact.setLastName(lastName);
		contact.setPhoneNumber(phoneNum);
		contact.setState(state);
		contact.setZip(zip);
		contact.setCreatedDate(createdDate);
		return contact;
	}

	/**
	 * Function to set id to contact
	 * 
	 * @param contact
	 * @param index
	 * @return Contact
	 */
	private Contact setIdToContact(Contact contact, AtomicInteger index) {
		contact.setId(index.getAndIncrement());
		return contact;
	}

	@Test
	public void givenAContact_whenCalledAdd_shouldMakeDuplicateNameCheckBeforeAdding() throws AddressBookException {
		Contact contact = createContact(1, "goa", "Gurgoan", "983635242", "qwe@123.gmail.com", "Rocky", "Hari",
				"Karnataka", "1267", LocalDate.now());
		Exception exception = assertThrows(AddressBookException.class, () -> {
			addressBookService.addContactToAddressBook(contact);
		});
		String expectedMessage = "Duplicate entry 'Rocky-Hari' for key 'contact.unique_name'";
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void givenAddressBook_whenSearchByCity_shouldReturnValidResultFromAddressBook() throws AddressBookException {
		List<Contact> result = addressBookService.searchPersonByCity("Banglore");
		assertTrue(result.size() > 0);
	}

	@Test
	public void givenAddressBook_whenSearchByNonExistentCity_shouldReturnEmptyList() throws AddressBookException {
		List<Contact> result = addressBookService.searchPersonByCity("koppa");
		assertTrue(result.isEmpty());
	}

	@Test
	public void givenContactsInAddressBook_whenSearchByCreatedDateRange_shouldReturnValidResult()
			throws AddressBookException {
		List<Contact> result = addressBookService.searchByCreatedDate(LocalDate.of(2021, 1, 1), LocalDate.now());
		assertTrue(result.size() == 5);
	}
}
