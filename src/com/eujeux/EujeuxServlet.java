package com.eujeux;

/**
 * Servlet that shows a single user's games to that user.
 * 
 * @author J. Hollingsworth
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class EujeuxServlet extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// get a user
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		if (user == null) {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
			return;
		} 
		
		resp.setContentType("text/html");
		resp.getWriter().println("<html>");
		setHeader(resp.getWriter());
		
		PrintWriter pw = resp.getWriter();
		
		pw.println("<body>");
		pw.println("<p>Hello, " + user.getNickname() + 
				"! (You can <a href=\"" + userService.createLoginURL(req.getRequestURI()) +
				"\">sign out</a>.)</p>");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(AGame.class);
		query.setFilter("user == creator");
		query.declareParameters("String creator");
		List<AGame> games = null;
		try {
			games = (List<AGame>) query.execute(user.getNickname());
		} finally {
			query.closeAll();
		}
		
		if (games.isEmpty()) {
			pw.println("<p>" + user.getNickname() + " has no games.</p>");
		} else {
			pw.println("<p>" + user.getNickname() + "'s games:");
			for (AGame game: games) {
				pw.println("<p><b><a href=\"show?game=" + game.getName() + 
						"&creator=" + game.getUser() + "\">" + game.getName() + "</a></b></p>");
			}
		}
		
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		String blobStr = blobstoreService.createUploadUrl("/upload");
		
		pw.println("<form action=\"" + blobStr + "\")\" method=\"post\" enctype=\"multipart/form-data\">");
		pw.println("<input name=\"name\" type=\"text\" value=\"Name\"> <br/>");
		pw.println("<input name=\"major\" type=\"number\" value=\"1\" min=\"0\" max=\"100\">");
		pw.println("<input name=\"minor\" type=\"number\" value=\"0\" min=\"0\" max=\"100\"> <br/>");
		pw.println("<input name=\"blob\" type=\"file\" size=\"30\"> <br/>");
		pw.println("<input name=\"Submit\" type=\"submit\" value=\"Submit\"> <br/>");
		pw.println("</form>");
		
		pw.println("</body>");
		
		resp.getWriter().println("</html>");
	}
	
	private void setHeader(PrintWriter pw) {
		pw.println("<head>");
		pw.println("<link type=\"text/css\" rel=\"stylesheet\" href=\"/stylesheets/main.css\" />");
		pw.println("</head>");
	}
}
