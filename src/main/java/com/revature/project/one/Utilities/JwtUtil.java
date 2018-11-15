package com.revature.project.one.Utilities;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.project.one.Models.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.revature.project.one.Models.Reimbursement;
import com.revature.project.one.Services.ReimbursementService;
import com.revature.project.one.Services.UserService;

/**
 * Utilities functions for working with requests when JWT is require
 * 
 * @author Calvin Vo
 *
 */
public class JwtUtil {
	private static UserService sUserService = UserService.currentImplementation;
	private static ReimbursementService sReimbursementService = ReimbursementService.currentImplementation;

	
	/**
	 * Create a Jwt and attach user is as private claim
	 * 
	 * @param User
	 * @return String
	 * @throws IOException 
	 * @throws SQLException
	 */
	public static String createJwt(User pUser) throws IOException {
		String tJwt;
		Algorithm algorithm = Algorithm.HMAC256("secret");
		tJwt = JWT.create()
		.withClaim("id", pUser.getId())
		.withIssuer("auth0")
		.sign(algorithm);
		
		return tJwt;
	}
	
	/**
	 * Verify Jwt is good and have a match user
	 * 
	 * @param req
	 * @return Boolean
	 * @throws IOException 
	 * @throws SQLException
	 */
	public static boolean jwtVerify(User pUser, HttpServletResponse resp) throws IOException {
		User tUser = pUser;
		
		if(tUser == null)
			return false;
		if(!JwtUtil.isUserActive(tUser, resp))
			return false;
		return true;
	}
	
	/**
	 * Get a user with jwt
	 * 
	 * @param req
	 * @return User
	 * @throws IOException 
	 * @throws SQLException
	 */
	public static User getUserFromJwt(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if(req.getHeader("Authorization") != null) {
			String[] tToken = req.getHeader("Authorization").split(" ");
			if(tToken.length == 2) {
				String tJwt = tToken[1];
				User tUser = null;
				tUser = sUserService.findByJwt(tJwt);
				
				if(tUser != null)
					return tUser;
			}
		}

		Map<String, String> tMap = new HashMap<String, String>();
		tMap.put("message", "Invalid permission, user need to be login to use this resource.");
		ResponseMapper.convertAndAttach(tMap, resp);	
		resp.setStatus(400);
		
		return null;
	}
	
	public static Boolean isUserActive(User pUser, HttpServletResponse resp) throws IOException {
		if( pUser.isActive() )
			return true;
		
		resp.setStatus(400);
		Map<String, String> tMap = new HashMap<String, String>();
		tMap.put("message", "Account is not active.");
		ResponseMapper.convertAndAttach(tMap, resp);
		return false;
	}
	
	public static Boolean isRequestFromManager(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User tUser = getUserFromJwt(req, resp);
		if(tUser != null) {
			if(tUser.getRoleId() == 2) {
				return true;
			}
		}
		
		Map<String, String> tMap = new HashMap<String, String>();
		tMap.put("message", "Invalid permission, manager authorization require.");
		ResponseMapper.convertAndAttach(tMap, resp);	
		resp.setStatus(400);
		return false;
	}
	
	public static Boolean isRequestFromSelf(HttpServletRequest req, HttpServletResponse resp, int pId) throws IOException {
		User tUser = getUserFromJwt(req, resp);
		if(tUser != null) {
			if(tUser.getId() == pId)
				return true;
		}
		
		Map<String, String> tMap = new HashMap<String, String>();
		tMap.put("message", "Invalid permission, resources reserved for owner.");
		ResponseMapper.convertAndAttach(tMap, resp);	
		resp.setStatus(400);
		return false;
	}
	
	public static Boolean isManagerUpdateSelfReimbursement(User pUser, HttpServletResponse resp, int pId) throws IOException {
		Reimbursement tReimbursement = sReimbursementService.findById(pId);
		if(tReimbursement.getAuthor_id() != pUser.getId()) {
			return false;
		}
		
		Map<String, String> tMap = new HashMap<String, String>();
		tMap.put("message", "You cannot modify your own ticket.");
		ResponseMapper.convertAndAttach(tMap, resp);	
		resp.setStatus(400);
		return true;
	}
	
	public static Boolean isManagerOrSelfAccess(User pUser, HttpServletResponse resp, int pUserId) throws IOException {
		if(pUser.getRoleId() == 2 ||
			pUser.getId() == pUserId)
			return true;
		
		Map<String, String> tMap = new HashMap<String, String>();
		tMap.put("message", "Access impermissible to such a fool as yourself.");
		ResponseMapper.convertAndAttach(tMap, resp);	
		resp.setStatus(400);
		return false;
	}
	
	public static void attachMessage(HttpServletResponse resp, int pId, String pMessage) throws IOException {
		Map<String, String> tMap = new HashMap<String, String>();
		tMap.put("message", pMessage);
		ResponseMapper.convertAndAttach(tMap, resp);	
		resp.setStatus(pId);
	}
}
