package spms.servlets;

import java.io.IOException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.controls.*;
import spms.vo.Member;

@SuppressWarnings("serial")
@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet {
  @Override
  protected void service(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html; charset=UTF-8");
    String servletPath = request.getServletPath();
    try {
    	Controller controller = null;
	    Map<String, Object> model = new HashMap<String, Object>();	
    	
	    //model 안에 memberDao 주입
    	ServletContext sc = this.getServletContext();
    	model.put("memberDao",sc.getAttribute("memberDao"));	    
    	
	      if ("/member/list.do".equals(servletPath)) {
	        controller = new MemberListController();
	      } else if ("/member/add.do".equals(servletPath)) {
	    	  controller = new MemberAddController();
	        if (request.getParameter("email") != null) {
	        	model.put("member", new Member()
	            .setEmail(request.getParameter("email"))
	            .setPassword(request.getParameter("password"))
	            .setName(request.getParameter("name")));
	        }
	      } else if ("/member/update.do".equals(servletPath)) {
	    	  controller = new MemberUpdateController();
	        if (request.getParameter("email") != null) {
	        	model.put("member", new Member()
	            .setNo(Integer.parseInt(request.getParameter("no")))
	            .setEmail(request.getParameter("email"))
	            .setName(request.getParameter("name")));
	        }else if(request.getParameter("email") == null){
	        	model.put("no", Integer.parseInt(request.getParameter("no")));
	        }
	      } else if ("/member/delete.do".equals(servletPath)) {
	    	  controller = new MemberDeleteController();
	    	  model.put("no", Integer.parseInt(request.getParameter("no")));
	      } else if ("/auth/login.do".equals(servletPath)) {
	    	  controller = new LogInController();
	    	  if (request.getParameter("email") != null) {
	    		  if (request.getParameter("password") != null) {
	    			  model.put("loginInfo", new Member()
	    					  .setEmail(request.getParameter("email"))
	    					  .setPassword(request.getParameter("password")));
	    			  model.put("session", request.getSession());
		    	  }
	    	  }
	      } else if ("/auth/logout.do".equals(servletPath)) {
	    	  controller = new LogOutController();
	    	  model.put("session", request.getSession());
	      }
	      
	      
	      //컨트롤로 호출을 통해 View 이름을 리턴받음
	      String viewUrl = controller.execute(model);
	      System.out.println("viewUrl is " + viewUrl);
	      //Map -> request.attribute로 옮겨야함
	      
	      for(String key : model.keySet()){
	    	  request.setAttribute(key, model.get(key));
	      }
	      
	      if (viewUrl.startsWith("redirect:")) {
	        response.sendRedirect(viewUrl.substring(9)); //redirect:를 잘라내고 그 뒤에꺼를 redirect 시킬려고 substring을 사용한다.
	        return;
	        
	      } else {
	        RequestDispatcher rd = request.getRequestDispatcher(viewUrl);
	        rd.include(request, response);
	      }
      
    } catch (Exception e) {
      e.printStackTrace();
      request.setAttribute("error", e);
      RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
      rd.forward(request, response);
    }
  }
}
