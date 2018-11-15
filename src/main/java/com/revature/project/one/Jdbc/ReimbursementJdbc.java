package com.revature.project.one.Jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.revature.project.one.Dao.ReimbursementDao;
import com.revature.project.one.Models.Reimbursement;
import com.revature.project.one.Models.prettyReimbursement;
import com.revature.project.one.Utilities.ConnectionUtil;

public class ReimbursementJdbc implements ReimbursementDao {

	/**
	 * Create a new Reimbursement entry
	 * 
	 * @param {Reimbursement}
	 * @return {integer}
	 */
	public int insertReimbursement(Reimbursement pReimbursement) {
		try (Connection tConnection = ConnectionUtil.getConnection()) {
			PreparedStatement tPStatement = tConnection.prepareStatement(
					"INSERT INTO reimbursements (amount, submitted, resolved, "
							+ "description, receipt, author, status_id, type_id) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
					new String[] { "reimbursement_id" });

			tPStatement.setInt(1, pReimbursement.getAmount());
			tPStatement.setObject(2, pReimbursement.getSubmitted());
			tPStatement.setObject(3, pReimbursement.getResolved());
			tPStatement.setString(4, pReimbursement.getDescription());
			tPStatement.setString(5, pReimbursement.getReceipt());
			tPStatement.setInt(6, pReimbursement.getAuthor_id());
			tPStatement.setInt(7, pReimbursement.getStatus_id());
			tPStatement.setInt(8, pReimbursement.getType_id());

			tPStatement.executeUpdate();

			ResultSet result = tPStatement.getGeneratedKeys();
			if (result.next()) {
				int id = result.getInt("reimbursement_id");
				pReimbursement.setId(id);
				return id;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Fetch a Reimbursement
	 * 
	 * @param {integer}
	 * @return {Reimbursement}
	 */
	public Reimbursement getReimbursement(int pId) {
		try (Connection tConnection = ConnectionUtil.getConnection()) {
			PreparedStatement tPStatement = tConnection
					.prepareStatement("SELECT * FROM reimbursements WHERE reimbursement_id = ?;");

			tPStatement.setInt(1, pId);
			ResultSet tResult = tPStatement.executeQuery();

			if (tResult.next()) {
				return reimbursementFromResultSet(tResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Fetch a list of Reimbursement
	 * 
	 * @param {integer, integer}
	 * @return {List}
	 */
	public List<prettyReimbursement> getSetOfReimbursements(int pLimit, int pOffset, int pStatus) {
		try (Connection tConnection = ConnectionUtil.getConnection()) {
			PreparedStatement tPStatement;
			if (pStatus == 0) {
				tPStatement = tConnection.prepareStatement(
						"SELECT reimbursements.reimbursement_id, reimbursements.amount, " + 
						"		reimbursements.submitted, reimbursements.resolved, " + 
						"		reimbursements.description, reimbursements.receipt, " + 
						"		users.first_name as AuthorFirst, users.last_name as AuthorLast, " + 
						"		Resolver.first_name as ResolverFirst, Resolver.last_name as ResolverLast, " + 
						"		reimbursement_status.status, reimbursement_type.type " + 
						"		FROM reimbursements " + 
						"INNER JOIN users " + 
						"ON reimbursements.author = users.user_id " + 
						"LEFT JOIN users AS Resolver " + 
						"ON Resolver.user_id = reimbursements.resolver " + 
						"INNER JOIN reimbursement_status " + 
						"ON reimbursement_status.status_id = reimbursements.status_id " + 
						"INNER JOIN reimbursement_type " + 
						"ON reimbursement_type.type_id = reimbursements.type_id " +
						"OFFSET ?" +
						"LIMIT ?"
						);
				
				tPStatement.setInt(1, pOffset);
				tPStatement.setInt(2, pLimit);
			} else {
				tPStatement = tConnection.prepareStatement(
						"SELECT reimbursements.reimbursement_id, reimbursements.amount, " + 
						"		reimbursements.submitted, reimbursements.resolved, " + 
						"		reimbursements.description, reimbursements.receipt, " + 
						"		users.first_name as AuthorFirst, users.last_name as AuthorLast, " + 
						"		Resolver.first_name as ResolverFirst, Resolver.last_name as ResolverLast, " + 
						"		reimbursement_status.status, reimbursement_type.type " + 
						"		FROM reimbursements " + 
						"INNER JOIN users " + 
						"ON reimbursements.author = users.user_id " + 
						"LEFT JOIN users AS Resolver " + 
						"ON Resolver.user_id = reimbursements.resolver " + 
						"INNER JOIN reimbursement_status " + 
						"ON reimbursement_status.status_id = reimbursements.status_id " + 
						"INNER JOIN reimbursement_type " + 
						"ON reimbursement_type.type_id = reimbursements.type_id " +
						"AND reimbursements.status_id=?" +
						"OFFSET ?" +
						"LIMIT ?"
						);
				tPStatement.setInt(1, pStatus);
				tPStatement.setInt(2, pOffset);
				tPStatement.setInt(3, pLimit);
			}
			ResultSet tResult = tPStatement.executeQuery();
			List<prettyReimbursement> tReimbursementList = new ArrayList<>();

			while (tResult.next()) {
				tReimbursementList.add(prettyReimbursementsFromResult(tResult));
			}

			return tReimbursementList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Fetch a list of Reimbursement created by a user
	 * 
	 * @param {integer}
	 * @return {List}
	 */
	@Override
	public List<prettyReimbursement> getReimbursementsFromUserId(int pUserId, int pLimit, int pOffset, int pStatus) {
		try (Connection tConnection = ConnectionUtil.getConnection()) {
			PreparedStatement tPStatement;
			if (pStatus == 0) {
				tPStatement = tConnection.prepareStatement(
						"SELECT reimbursements.reimbursement_id, reimbursements.amount, " + 
						"		reimbursements.submitted, reimbursements.resolved, " + 
						"		reimbursements.description, reimbursements.receipt, " + 
						"		users.first_name as AuthorFirst, users.last_name as AuthorLast, " + 
						"		Resolver.first_name as ResolverFirst, Resolver.last_name as ResolverLast, " + 
						"		reimbursement_status.status, reimbursement_type.type " + 
						"		FROM reimbursements " + 
						"INNER JOIN users " + 
						"ON reimbursements.author = users.user_id " + 
						"LEFT JOIN users AS Resolver " + 
						"ON Resolver.user_id = reimbursements.resolver " + 
						"INNER JOIN reimbursement_status " + 
						"ON reimbursement_status.status_id = reimbursements.status_id " + 
						"INNER JOIN reimbursement_type " + 
						"ON reimbursement_type.type_id = reimbursements.type_id " +
						"WHERE users.user_id=? " +
						"OFFSET ?" +
						"LIMIT ?"
						);
				
				tPStatement.setInt(1, pUserId);
				tPStatement.setInt(2, pOffset);
				tPStatement.setInt(3, pLimit);
			} else {
				tPStatement = tConnection.prepareStatement(
						"SELECT reimbursements.reimbursement_id, reimbursements.amount, " + 
						"		reimbursements.submitted, reimbursements.resolved, " + 
						"		reimbursements.description, reimbursements.receipt, " + 
						"		users.first_name as AuthorFirst, users.last_name as AuthorLast, " + 
						"		Resolver.first_name as ResolverFirst, Resolver.last_name as ResolverLast, " + 
						"		reimbursement_status.status, reimbursement_type.type " + 
						"		FROM reimbursements " + 
						"INNER JOIN users " + 
						"ON reimbursements.author = users.user_id " + 
						"LEFT JOIN users AS Resolver " + 
						"ON Resolver.user_id = reimbursements.resolver " + 
						"INNER JOIN reimbursement_status " + 
						"ON reimbursement_status.status_id = reimbursements.status_id " + 
						"INNER JOIN reimbursement_type " + 
						"ON reimbursement_type.type_id = reimbursements.type_id " +
						"WHERE users.user_id=? " +
						"AND reimbursements.status_id=?" +
						"OFFSET ?" +
						"LIMIT ?"
						);
				tPStatement.setInt(1, pUserId);
				tPStatement.setInt(2, pStatus);
				tPStatement.setInt(3, pOffset);
				tPStatement.setInt(4, pLimit);
			}

			ResultSet tResult = tPStatement.executeQuery();
			List<prettyReimbursement> tReimbursementList = new ArrayList<>();

			while (tResult.next()) {
				tReimbursementList.add(prettyReimbursementsFromResult(tResult));
			}

			return tReimbursementList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean updateReimbursement(int pId, Timestamp pTimeResolved, int pResolverId, int pStatusId) {
		try (Connection tConnection = ConnectionUtil.getConnection()) {
			PreparedStatement tPStatement = tConnection.prepareStatement("UPDATE reimbursements "
					+ "SET resolved=?, resolver=?, status_id=? " + "WHERE reimbursement_id = ?;");

			tPStatement.setObject(1, pTimeResolved);
			tPStatement.setInt(2, pResolverId);
			tPStatement.setInt(3, pStatusId);
			tPStatement.setInt(4, pId);

			int tResult = tPStatement.executeUpdate();

			if (tResult > 0)
				return true;
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Create a Reimbursement object from a ResultSet
	 * 
	 * @param {ResultSet}
	 * @return {Reimbursement}
	 */
	public Reimbursement reimbursementFromResultSet(ResultSet pResultSet) throws SQLException {
		return new Reimbursement(pResultSet.getInt("reimbursement_id"), pResultSet.getInt("amount"),
				pResultSet.getTimestamp("submitted"), pResultSet.getTimestamp("resolved"),
				pResultSet.getString("description"), pResultSet.getString("receipt"), pResultSet.getInt("author"),
				pResultSet.getInt("resolver"), pResultSet.getInt("status_id"), pResultSet.getInt("type_id"));
	}

	/**
	 * Create a Reimbursement object from a ResultSet
	 * 
	 * @param {ResultSet}
	 * @return {Reimbursement}
	 */
	public prettyReimbursement prettyReimbursementsFromResult(ResultSet pResultSet) throws SQLException {
		return new prettyReimbursement(
				pResultSet.getInt("reimbursement_id"),
				pResultSet.getInt("amount"),
				pResultSet.getTimestamp("submitted"),
				pResultSet.getTimestamp("resolved"),
				pResultSet.getString("description"),
				pResultSet.getString("receipt"),
				pResultSet.getString("authorfirst"),
				pResultSet.getString("authorlast"),
				pResultSet.getString("resolverfirst"),
				pResultSet.getString("resolverlast"),
				pResultSet.getString("status"),
				pResultSet.getString("type")
				);
	}
}
