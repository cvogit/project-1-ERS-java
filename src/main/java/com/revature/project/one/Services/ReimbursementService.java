package com.revature.project.one.Services;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.project.one.Models.Reimbursement;
import com.revature.project.one.Models.prettyReimbursement;

public interface ReimbursementService {

	ReimbursementService currentImplementation = new ReimbursementServiceImpl();

	Reimbursement findById(int pId);

	List<prettyReimbursement> findByUserId(int pId, int pLimit, int pOffset, int pStatus);

	List<prettyReimbursement> findSet(int pLimit, int pOffset, int pStatus);

	boolean create(HttpServletRequest req, HttpServletResponse resp) throws IOException;
	boolean update(HttpServletRequest req, HttpServletResponse resp, int pId) throws IOException;
}
