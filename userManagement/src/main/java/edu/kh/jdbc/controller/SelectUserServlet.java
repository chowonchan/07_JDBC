package edu.kh.jdbc.controller;

import java.io.IOException;
import java.util.List;

import edu.kh.jdbc.dto.User;
import edu.kh.jdbc.service.UserService;
import edu.kh.jdbc.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/selectUser")
public class SelectUserServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		try {
			String userNo = req.getParameter("userNo");
	
			
			UserService service = new UserServiceImpl();
			
			User user = service.selectUser(userNo);
			
			req.setAttribute("user", user);
		
			
			String path = "/WEB-INF/views/selectUser.jsp";
			req.getRequestDispatcher(path).forward(req, resp);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

}
