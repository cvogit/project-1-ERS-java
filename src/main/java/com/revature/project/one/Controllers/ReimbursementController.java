package com.revature.project.one.Controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.revature.project.one.Models.User;
import com.revature.project.one.Models.prettyReimbursement;
import com.revature.project.one.Services.ReimbursementService;
import com.revature.project.one.Utilities.JwtUtil;
import com.revature.project.one.Utilities.ResponseMapper;

@SuppressWarnings("serial")
public class ReimbursementController extends HttpServlet {
	private ReimbursementService sReimbursementService = ReimbursementService.currentImplementation;
	
	/***
	 * Map requests to the correct methods
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		String method = req.getMethod();
		switch (method) {
		case "GET":
			processGet(req, resp);
			break;
		case "POST":
			processPost(req, resp);
			break;
		case "PUT":
			processPut(req, resp);
			break;
		default:
			resp.setStatus(404);
			break;
		}
	}
	
	/***
	 * Return reimbursements
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	private void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String uri = req.getRequestURI();
		String context = "ERS";
		uri = uri.substring(context.length() + 2, uri.length());
		String[] uriArray = uri.split("/");

		if (uriArray.length == 1) {
			User tUser = JwtUtil.getUserFromJwt(req, resp);
		
			if(!JwtUtil.jwtVerify(tUser, resp))
				return;
			if(!JwtUtil.isRequestFromManager(req, resp))
				return;
			
			// Get a list of reimbursements
			List<prettyReimbursement> tReimbursementList;
			int tLimit = 30;
			int tOffset = 0;
			int tStatus = 0;
			
			if(req.getParameterMap().containsKey("limit"))
				tLimit = Integer.parseInt(req.getParameter("limit"));
			if(req.getParameterMap().containsKey("offset"))
				tLimit = Integer.parseInt(req.getParameter("offset"));
			if(req.getParameterMap().containsKey("status"))
				tStatus = Integer.parseInt(req.getParameter("status"));

			tReimbursementList = sReimbursementService.findSet(tLimit, tOffset, tStatus);
			ResponseMapper.convertAndAttach(tReimbursementList, resp);
			return;
		} else if (uriArray.length == 3) {
			try {
				if("users".equals(uriArray[1])) {
					int tUserId = Integer.parseInt(uriArray[2]);
					User tUser = JwtUtil.getUserFromJwt(req, resp);
					
					if(!JwtUtil.jwtVerify(tUser, resp))
						return;
					if(!JwtUtil.isManagerOrSelfAccess(tUser, resp, tUserId))
						return;

					// Get a list of reimbursements
					List<prettyReimbursement> tReimbursementList;
					int tLimit = 30;
					int tOffset = 0;
					int tStatus = 0;
					
					if(req.getParameterMap().containsKey("limit"))
						tLimit = Integer.parseInt(req.getParameter("limit"));
					if(req.getParameterMap().containsKey("offset"))
						tLimit = Integer.parseInt(req.getParameter("offset"));
					if(req.getParameterMap().containsKey("status"))
						tStatus = Integer.parseInt(req.getParameter("status"));
					
		        	tReimbursementList = sReimbursementService.findByUserId(tUserId, tLimit, tOffset, tStatus);
					ResponseMapper.convertAndAttach(tReimbursementList, resp);	
					return;
				}
			} catch (NumberFormatException e) {
				JwtUtil.attachMessage(resp, 400, "Invalid path, expect number type trailing.");
				return;
			}
		} else {
			resp.setStatus(404);
			return;
		}
	}
	
	/***
	 * 
	 * @param req
	 * @param resp
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private void processPost(HttpServletRequest req, HttpServletResponse resp) throws JsonParseException, JsonMappingException, IOException {
		String uri = req.getRequestURI();
		String context = "ERS";
		uri = uri.substring(context.length() + 2, uri.length());
		String[] uriArray = uri.split("/");
		
		if (uriArray.length == 1) {
			User tUser = JwtUtil.getUserFromJwt(req, resp);
			
			if(!JwtUtil.jwtVerify(tUser, resp))
				return;
			
			if(sReimbursementService.create(req, resp)) {
				JwtUtil.attachMessage(resp, 200, "Success");
				return;
			} 
			
			JwtUtil.attachMessage(resp, 300, "Server errors unknown.");
			return;
		} else if (uriArray.length == 2) {
//			int tUserId = Integer.parseInt(uriArray[1]);
//			User tUser = JwtUtil.getUserFromJwt(req, resp);
//			if(!JwtUtil.jwtVerify(tUser, resp))
//				return;
//				
			return;
		} else {
			resp.setStatus(404);
			return;
		}
	}
	
	/***
	 * 
	 * @param req
	 * @param resp
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private void processPut(HttpServletRequest req, HttpServletResponse resp) throws JsonParseException, JsonMappingException, IOException {
		String uri = req.getRequestURI();
		String context = "ERS";
		uri = uri.substring(context.length() + 2, uri.length());
		String[] uriArray = uri.split("/");
		
		if (uriArray.length == 1) {
			JwtUtil.attachMessage(resp, 400, "Invalid Path.");
			return;
		} else if (uriArray.length == 2) {
			try {
				int tReimbursementId = Integer.parseInt(uriArray[1]);
				User tUser = JwtUtil.getUserFromJwt(req, resp);
				
				if(!JwtUtil.jwtVerify(tUser, resp))
					return;
				if(!JwtUtil.isRequestFromManager(req, resp))
					return;
				if(JwtUtil.isManagerUpdateSelfReimbursement(tUser, resp, tReimbursementId))
					return;
				
				if(sReimbursementService.update(req, resp, tReimbursementId))
					JwtUtil.attachMessage(resp, 200, "Success");
				else
					JwtUtil.attachMessage(resp, 304, "Cannot update reimbursement.");
				
				return;
			} catch (NumberFormatException e) {
				JwtUtil.attachMessage(resp, 400, "Invalid path, expect number type trailing.");
			}
		}
	}
}
