package com.revature.project.one.Services;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.project.one.Dao.ReimbursementDao;
import com.revature.project.one.Models.Reimbursement;
import com.revature.project.one.Models.User;
import com.revature.project.one.Models.prettyReimbursement;
import com.revature.project.one.Utilities.JwtUtil;

public class ReimbursementServiceImpl implements ReimbursementService {
	private ReimbursementDao sReimbursement = ReimbursementDao.currentImplementation;
	
	@Override
	public Reimbursement findById(int pId) {
		return sReimbursement.getReimbursement(pId);
	}

	@Override
	public List<prettyReimbursement> findByUserId(int pId, int pLimit, int pOffset, int pStatus) {
		return sReimbursement.getReimbursementsFromUserId(pId, pLimit, pOffset, pStatus);
	}

	@Override
	public List<prettyReimbursement> findSet(int pLimit, int pOffset, int pStatus) {
		return sReimbursement.getSetOfReimbursements(pLimit, pOffset, pStatus);
	}

	@Override
	public boolean create(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map<String, String[]> rRequestMap = req.getParameterMap();
		User sUser = JwtUtil.getUserFromJwt(req, resp);
		if( rRequestMap.containsKey("amount") && 
			rRequestMap.containsKey("description") && 	
			rRequestMap.containsKey("type_id")) {
			
			String tReceipt ;
			if(rRequestMap.containsKey("receipt"))
				tReceipt = req.getParameter("receipt");
			else 
				tReceipt = null;
			
			Timestamp tCurrentTime = new Timestamp(System.currentTimeMillis());
			
			Reimbursement newReimbursement = new Reimbursement( 
					Integer.parseInt(req.getParameter("amount")), 
					tCurrentTime, 
					req.getParameter("description"), 
					tReceipt, 
					sUser.getId(), 
					1, 
					Integer.parseInt(req.getParameter("type_id")));
			
			sReimbursement.insertReimbursement(newReimbursement);
			return true;
		}
		return false;
	}

	@Override
	public boolean update(HttpServletRequest req, HttpServletResponse resp, int pId) throws IOException {
		Map<String, String[]> rRequestMap = req.getParameterMap();
		User sUser = JwtUtil.getUserFromJwt(req, resp);
		if( rRequestMap.containsKey("status") ) {
			Timestamp tCurrentTime = new Timestamp(System.currentTimeMillis());
			return sReimbursement.updateReimbursement(pId, tCurrentTime, sUser.getId(), Integer.parseInt(req.getParameter("status")));
		}
		return false;
	}

}
