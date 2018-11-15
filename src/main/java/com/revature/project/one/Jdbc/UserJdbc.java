package com.revature.project.one.Jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.project.one.Dao.UserDao;
import com.revature.project.one.Models.User;
import com.revature.project.one.Utilities.ConnectionUtil;

public class UserJdbc implements UserDao {

	/**
	 * Create a new User entry
	 * 
	 * @param {User}
	 * @return {integer}
	 */
	public int insertUser(User pUser) {
		try (Connection tConnection = ConnectionUtil.getConnection()) {
			PreparedStatement tPStatement = tConnection.prepareStatement(
				"INSERT INTO users (username, password, first_name, last_name, email, role_id, active) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?);"
				, new String[] {"user_id"}
				);
			
			tPStatement.setString(1, pUser.getUsername());
			tPStatement.setString(2, pUser.getPassword());
			tPStatement.setString(3, pUser.getFirstname());
			tPStatement.setString(4, pUser.getLastname());
			tPStatement.setString(5, pUser.getEmail());
			tPStatement.setInt(6, pUser.getRoleId());
			tPStatement.setBoolean(7, pUser.isActive());
			
			tPStatement.executeUpdate();
			
			ResultSet result = tPStatement.getGeneratedKeys();
			if (result.next()) {
				int id = result.getInt("user_id");
				pUser.setId(id);
				return id;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Fetch a User with their id
	 * 
	 * @param {integer}
	 * @return {User}
	 */
	public User getUser(int pId) {
		try (Connection tConnection = ConnectionUtil.getConnection()) {
			PreparedStatement tPStatement = tConnection.prepareStatement(
					"SELECT * FROM users WHERE user_id = ?;");
			
			tPStatement.setInt(1, pId);
			ResultSet tResult = tPStatement.executeQuery();
			if (tResult.next()) {
				return userFromResultSet(tResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Fetch a User with their username
	 * 
	 * @param {String}
	 * @return {User}
	 */
	public User getUser(String pUsername) {
		try (Connection tConnection = ConnectionUtil.getConnection()) {
			PreparedStatement tPStatement = tConnection.prepareStatement(
					"SELECT * FROM users WHERE username = ?;");
			
			tPStatement.setString(1, pUsername);
			ResultSet tResult = tPStatement.executeQuery();
			if (tResult.next()) {
				return userFromResultSet(tResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Fetch a list of User (10)
	 * 
	 * @return {List}
	 */
	public List<User> getAllUsers() {
		try (Connection tConnection = ConnectionUtil.getConnection()) {
			PreparedStatement tPStatement = tConnection.prepareStatement(
					"SELECT * FROM users "
					+ "LIMIT 10;");

			ResultSet 	tResult   = tPStatement.executeQuery();
			List<User> 	tUserList = new ArrayList<>();
			
			while (tResult.next()) {
				tUserList.add(userFromResultSet(tResult));
			}
			
			return tUserList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Fetch a list of User
	 * 
	 * @param {integer, integer}
	 * @return {List}
	 */
	public List<User> getSetOfUsers(int pLimit, int pOffset) {
		try (Connection tConnection = ConnectionUtil.getConnection()) {
			PreparedStatement tPStatement = tConnection.prepareStatement(
					"SELECT * FROM users "
					+ "LIMIT ? "
					+ "OFFSET ?;");
			
			tPStatement.setInt(1, pLimit);
			tPStatement.setInt(2, pOffset);

			ResultSet 	tResult   = tPStatement.executeQuery();
			List<User> 	tUserList = new ArrayList<>();
			
			while (tResult.next()) {
				tUserList.add(userFromResultSet(tResult));
			}
			
			return tUserList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int updateUser(User pUser) {
		return 0;
	}
	
	/**
	 * Create a User object from a ResultSet
	 * 
	 * @param {ResultSet}
	 * @return {User}
	 */
	public User userFromResultSet(ResultSet pResultSet) throws SQLException {
		return new User(pResultSet.getInt("user_id"), 
				pResultSet.getString("username"), 
				pResultSet.getString("password"),
				pResultSet.getString("first_name"),
				pResultSet.getString("last_name"), 
				pResultSet.getString("email"), 
				pResultSet.getInt("role_id"),
				pResultSet.getBoolean("active")
				);
	}
}
