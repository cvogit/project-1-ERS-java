package com.revature.project.one.Controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.revature.project.one.Models.User;
import com.revature.project.one.Services.UserService;
import com.revature.project.one.Utilities.JwtUtil;
import com.revature.project.one.Utilities.ResponseMapper;

@SuppressWarnings("serial")
public class UserController extends HttpServlet {
	private UserService sUserService = UserService.currentImplementation;
			
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
	 * Return users
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

			// Get a list of users
			List<User> tUserList;
			if (req.getParameterMap().containsKey("limit") && req.getParameterMap().containsKey("offset")) {
				tUserList = sUserService.findSet(Integer.parseInt(req.getParameter("limit")), Integer.parseInt(req.getParameter("offset")));
	        } else {
	        	tUserList = sUserService.findAll();
	        }

			ResponseMapper.convertAndAttach(tUserList, resp);	
			resp.setStatus(200);
			return;
		} else if (uriArray.length == 2) {
			if("refresh".equals(uriArray[1])) {
				User tUser = JwtUtil.getUserFromJwt(req, resp);
				if(!JwtUtil.jwtVerify(tUser, resp))
					return;
				
				String tToken = JwtUtil.createJwt(tUser);
				Map<String, String> tMap = new HashMap<String, String>();
				tMap.put("jwt", tToken);
				tMap.put("roles", Integer.toString(tUser.getRoleId()));
				tMap.put("username", tUser.getUsername());
				
				ResponseMapper.convertAndAttach(tMap, resp);
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
			if(sUserService.create(req, resp))
				JwtUtil.attachMessage(resp, 200, "Registration successful, please wait for approval");
			return;
		} else if (uriArray.length == 2) {
			if("login".equals(uriArray[1])) {
				// Get login parameters
				Map<String, String[]> rRequestMap = req.getParameterMap();
				if( rRequestMap.containsKey("username") && 
					rRequestMap.containsKey("password")) {
					try {
						String tUsername = req.getParameter("username");
						String tPassword = req.getParameter("password");
						User tUser = sUserService.login(tUsername, tPassword);

						if(tUser == null)
							return;
						if(!tUser.isActive())
							return;
								
						resp.setStatus(200);
						
						// Give the client a token
						String tToken = JwtUtil.createJwt(tUser);
						
						Map<String, String> tMap = new HashMap<String, String>();
						tMap.put("jwt", tToken);
						tMap.put("roles", Integer.toString(tUser.getRoleId()));
						
						ResponseMapper.convertAndAttach(tMap, resp);
						return;
					} catch (JWTCreationException exception){
						JwtUtil.attachMessage(resp, 300, "Server errors, summon the programmers from their slumbers.");
						return;
					}
				}
			} else  {
				JwtUtil.attachMessage(resp, 400, "Invalid login credentials.");
				return;
			}
		} else {
			JwtUtil.attachMessage(resp, 404, "Invalid Path.");
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
				int tUserId = Integer.parseInt(uriArray[1]);
				User tUser = JwtUtil.getUserFromJwt(req, resp);
				if(!JwtUtil.jwtVerify(tUser, resp))
					return;
				if(!JwtUtil.isRequestFromSelf(req, resp, tUserId))
					return;
				
				//TODO Update user
			} catch (NumberFormatException e) {
				JwtUtil.attachMessage(resp, 400, "Invalid path, expect number type trailing.");
			}
		}
	}
}
