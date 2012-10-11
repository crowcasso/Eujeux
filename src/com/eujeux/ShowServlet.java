package com.eujeux;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ShowServlet extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/html");
		resp.getWriter().println("<html>");
		setHeader(resp.getWriter());
		
		PrintWriter pw = resp.getWriter();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(AGame.class);
		query.setFilter("user == creator && name == gameName");
		query.declareParameters("String creator, String gameName");
		List<AGame> games = null;
		try {
			games = (List<AGame>) query.execute(req.getParameter("creator"),req.getParameter("game"));
		} finally {
			query.closeAll();
		}
		
		if (games.isEmpty()) {
			pw.println("no such game");
		} else {
			for (AGame game: games) {
				pw.println("<p>" + game.getName() + 
						" (v" + game.getMajorVersion() + "." + game.getMinorVersion() + ")" +
						" by " + game.getUser() + " " +
						"<a href=\"/serve?blobKey=" + game.getBlobKey().getKeyString() + "\">the game</a>" + 
						"</p> <br/>");
			}
		}

		
		resp.getWriter().println("</html>");
	}
	
	private void setHeader(PrintWriter pw) {
		pw.println("<head>");
		pw.println("<link type=\"text/css\" rel=\"stylesheet\" href=\"/stylesheets/main.css\" />");
		pw.println("</head>");
	}
}
