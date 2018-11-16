package com.revature.project.one.Controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DispatcherServlet extends HttpServlet {
	
	private UserController sUserController = new UserController();
	private ReimbursementController sReimbursementController = new ReimbursementController();

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
		resp.addHeader("Access-Control-Allow-Headers",
				"Origin, Methods, Credentials, X-Requested-With, Content-Type, Accept");
		resp.addHeader("Access-Control-Allow-Credentials", "true");
		resp.setContentType("application/json");
		
		if(req.getMethod().equals("OPTIONS")) {
			resp.setStatus(200);
			return;
		}
		
		String uri = req.getRequestURI();
		String context = "ERS";
		uri = uri.substring(context.length() + 2, uri.length());
		if (uri.startsWith("users")) {
			sUserController.process(req, resp);
		} else if (uri.startsWith("reimbursements")) {
			sReimbursementController.process(req, resp);
		} else {
			resp.setStatus(404);
		}
	}
}
