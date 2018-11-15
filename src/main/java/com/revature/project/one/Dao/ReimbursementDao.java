package com.revature.project.one.Dao;

import java.sql.Timestamp;
import java.util.List;

import com.revature.project.one.Jdbc.ReimbursementJdbc;
import com.revature.project.one.Models.Reimbursement;
import com.revature.project.one.Models.prettyReimbursement;

public interface ReimbursementDao {
	ReimbursementDao currentImplementation = new ReimbursementJdbc();
	
	int insertReimbursement(Reimbursement pReimbursement);
	
	Reimbursement getReimbursement(int pId);
	List<prettyReimbursement> getReimbursementsFromUserId(int pId, int pLimit, int pOffset, int pStatus);
	List<prettyReimbursement> getSetOfReimbursements(int pStatus, int pLimit, int pOffset);
	
	boolean updateReimbursement(int pId, Timestamp tCurrentTime, int pResolverId, int pStatusId);
}
