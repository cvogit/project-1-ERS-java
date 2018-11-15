package com.revature.project.one.Dao;

import java.util.List;

import com.revature.project.one.Jdbc.UserJdbc;
import com.revature.project.one.Models.User;

public interface UserDao {
	UserDao currentImplementation = new UserJdbc();
	
	int insertUser(User pUser);
	
	User getUser(int pId);
	User getUser(String pUsername);
	List<User> getAllUsers();
	List<User> getSetOfUsers(int pLimit, int pOffset);
	
	int updateUser(User pUser);
}
