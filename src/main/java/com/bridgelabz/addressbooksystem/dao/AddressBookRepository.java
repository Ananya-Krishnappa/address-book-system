package com.bridgelabz.addressbooksystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bridgelabz.addressbooksystem.dto.Contact;
import com.bridgelabz.addressbooksystem.exception.AddressBookException;
import com.bridgelabz.addressbooksystem.exception.JdbcConnectorException;
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

	/**
	 * Function to add multiple contacts to address book using batch execution
	 * 
	 * @param contactList
	 * @return List<Contact>
	 * @throws AddressBookException
	 * @throws JdbcConnectorException
	 * @throws SQLException
	 */
	public List<Contact> addMultipleContactsToAddressBook(List<Contact> contactList)
			throws AddressBookException, JdbcConnectorException, SQLException {
		Connection connection = JdbcConnectionFactory.getJdbcConnection();
		try {
			connection.setAutoCommit(false);
			PreparedStatement pstmt = connection.prepareStatement(
					"insert into contact(first_name,last_name,address,city,state,zip,phone_num,email) "
							+ "values(?,?,?,?,?,?,?,?)");
			for (int i = 0; i < contactList.size(); i++) {
				pstmt.setString(1, contactList.get(i).getFirstName());
				pstmt.setString(2, contactList.get(i).getLastName());
				pstmt.setString(3, contactList.get(i).getAddress());
				pstmt.setString(4, contactList.get(i).getCity());
				pstmt.setString(5, contactList.get(i).getState());
				pstmt.setString(6, contactList.get(i).getZip());
				pstmt.setString(7, contactList.get(i).getPhoneNumber());
				pstmt.setString(8, contactList.get(i).getEmail());
				pstmt.addBatch();
			}
			try {
				pstmt.executeBatch();
			} catch (SQLException e) {
				LOG.error("Error message: " + e.getMessage());
				throw new AddressBookException(e.getMessage());
			}
			connection.commit();
			Statement stmt = connection.createStatement();
			ResultSet rs = null;
			rs = stmt.executeQuery("SELECT * from contact");
			List<Contact> newContactList = mapResultSetToContactList(rs);
			return newContactList;
		} catch (Exception e) {
			connection.rollback();
			throw new AddressBookException(e.getMessage());
		} finally {
			connection.close();
		}
	}

	/**
	 * Function to map result set to contact list
	 * 
	 * @param rs
	 * @return List<Contact>
	 * @throws SQLException
	 */
	private List<Contact> mapResultSetToContactList(ResultSet rs) throws SQLException {
		List<Contact> contactList = new ArrayList<Contact>();
		while (rs.next()) {
			Contact contact = new Contact();
			contact.setId(rs.getInt("id"));
			contact.setAddress(rs.getString("address"));
			contact.setCity(rs.getString("city"));
			contact.setEmail(rs.getString("email"));
			contact.setFirstName(rs.getString("first_name"));
			contact.setLastName(rs.getString("last_name"));
			contact.setPhoneNumber(rs.getString("phone_num"));
			contact.setState(rs.getString("state"));
			contact.setZip(rs.getString("zip"));
			contactList.add(contact);
		}
		return contactList;
	}
}
