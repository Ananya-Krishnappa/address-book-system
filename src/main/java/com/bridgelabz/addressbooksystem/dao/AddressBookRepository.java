package com.bridgelabz.addressbooksystem.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
		int contactId = -1;
		try (Connection connection = JdbcConnectionFactory.getJdbcConnection()) {
			String query = String.format(
					"insert into contact(addressbook_id,first_name,last_name,address,city,state,zip,phone_num,email,created_date) "
							+ "values('%d','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
					contact.getAddressBookId(), contact.getFirstName(), contact.getLastName(), contact.getAddress(),
					contact.getCity(), contact.getState(), contact.getZip(), contact.getPhoneNumber(),
					contact.getEmail(), contact.getCreatedDate());
			Statement statement = connection.createStatement();
			int rowAffected = statement.executeUpdate(query, statement.RETURN_GENERATED_KEYS);
			if (rowAffected == 1) {
				ResultSet result = statement.getGeneratedKeys();
				if (result.next()) {
					contactId = result.getInt(1);
				}
			}
			contact.setId(contactId);
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
					"insert into contact(addressbook_id,first_name,last_name,address,city,state,zip,phone_num,email,created_date) "
							+ "values(?,?,?,?,?,?,?,?,?,?)");
			for (int i = 0; i < contactList.size(); i++) {
				pstmt.setInt(1, contactList.get(i).getAddressBookId());
				pstmt.setString(2, contactList.get(i).getFirstName());
				pstmt.setString(3, contactList.get(i).getLastName());
				pstmt.setString(4, contactList.get(i).getAddress());
				pstmt.setString(5, contactList.get(i).getCity());
				pstmt.setString(6, contactList.get(i).getState());
				pstmt.setString(7, contactList.get(i).getZip());
				pstmt.setString(8, contactList.get(i).getPhoneNumber());
				pstmt.setString(9, contactList.get(i).getEmail());
				pstmt.setDate(10, Date.valueOf(contactList.get(i).getCreatedDate()));
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
			contact.setAddressBookId(rs.getInt("addressbook_id"));
			contact.setAddress(rs.getString("address"));
			contact.setCity(rs.getString("city"));
			contact.setEmail(rs.getString("email"));
			contact.setFirstName(rs.getString("first_name"));
			contact.setLastName(rs.getString("last_name"));
			contact.setPhoneNumber(rs.getString("phone_num"));
			contact.setState(rs.getString("state"));
			contact.setZip(rs.getString("zip"));
			contact.setCreatedDate(rs.getDate("created_date").toLocalDate());
			contactList.add(contact);
		}
		return contactList;
	}

	/**
	 * Function to search by city
	 * 
	 * @param city
	 * @return List<Contact>
	 * @throws AddressBookException
	 */
	public List<Contact> searchPersonByCity(String city) throws AddressBookException {
		try (Connection connection = JdbcConnectionFactory.getJdbcConnection()) {
			String query = "select * from contact WHERE city = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, city);
			ResultSet rs = preparedStatement.executeQuery();
			List<Contact> contactList = mapResultSetToContactList(rs);
			return contactList;
		} catch (SQLException e) {
			LOG.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			throw new AddressBookException("SQL State: " + e.getSQLState() + " " + e.getMessage());
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

	/**
	 * Function to search a person by created date
	 * 
	 * @param startDate
	 * @param endDate
	 * @return List<Contact>
	 * @throws AddressBookException
	 */
	public List<Contact> searchByCreatedDate(LocalDate startDate, LocalDate endDate) throws AddressBookException {
		try (Connection connection = JdbcConnectionFactory.getJdbcConnection()) {
			String query = "select * from contact WHERE created_date between ? and ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setDate(1, Date.valueOf(startDate));
			preparedStatement.setDate(2, Date.valueOf(endDate));
			ResultSet rs = preparedStatement.executeQuery();
			List<Contact> contactList = mapResultSetToContactList(rs);
			return contactList;
		} catch (SQLException e) {
			LOG.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			throw new AddressBookException("SQL State: " + e.getSQLState() + " " + e.getMessage());
		} catch (Exception e) {
			throw new AddressBookException(e.getMessage());
		}
	}

}
