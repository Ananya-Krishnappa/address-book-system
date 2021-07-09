package com.bridgelabz.addressbooksystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bridgelabz.addressbooksystem.dto.Contact;
import com.bridgelabz.addressbooksystem.exception.AddressBookException;
import com.bridgelabz.addressbooksystem.utils.JdbcConnectionFactory;

public class AddressBookRepository {
	private static final Logger LOG = LogManager.getLogger(AddressBookRepository.class);

	private static AddressBookRepository addressBookRepository;

	private AddressBookRepository() {

	}

	public static AddressBookRepository getInstance() {
		if (addressBookRepository == null) {
			addressBookRepository = new AddressBookRepository();
		}
		return addressBookRepository;
	}

	/**
	 * Function to add contact to address book
	 * 
	 * @param addressBook
	 * @return AddressBook
	 * @throws AddressBookException
	 */
	public Contact addContactToAddressBook(Contact contact) throws AddressBookException {
		int addressBookId = -1;
		try (Connection connection = JdbcConnectionFactory.getJdbcConnection()) {
			String query = String.format(
					"insert into contact(first_name,last_name,address,city,state,zip,phone_num,email) "
							+ "values('%s','%s','%s','%s','%s','%s','%s','%s',%s)",
					contact.getFirstName(), contact.getLastName(), contact.getAddress(), contact.getCity(),
					contact.getState(), contact.getZip(), contact.getPhoneNumber(), contact.getEmail(),
					contact.getAddressBookId());
			Statement statement = connection.createStatement();
			int rowAffected = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if (result.next()) {
					addressBookId = result.getInt(1);
				}
			}
			contact.setId(addressBookId);
			return contact;
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	/**
	 * delete contact by name
	 * 
	 * @param name
	 * @return int
	 * @throws AddressBookException
	 */
	public int deleteContactByName(String name) throws AddressBookException {
		try (Connection connection = JdbcConnectionFactory.getJdbcConnection()) {
			String query = "delete from contact WHERE first_name = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			int resultSet = preparedStatement.executeUpdate();
			return resultSet;
		} catch (SQLException e) {
			LOG.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			throw new AddressBookException("SQL State: " + e.getSQLState() + " " + e.getMessage());
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}
}
