package com.revature.project.one.Services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.revature.project.one.Models.User;

public interface UserService {

	UserService currentImplementation = new UserServiceImpl();

	User findById(int id);

	User findByJwt(String pJwt);

	List<User> findAll();

	User login(String tUsername, String tPassword);

	boolean create(HttpServletRequest req, HttpServletResponse resp);

	List<User> findSet(int pLimit, int pOffset);
}
