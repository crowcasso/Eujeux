package com.eujeux;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.http.*;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(UploadServlet.class.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

		try {
			resp.setContentType("text/plain");

			AGame aGame = new AGame();
			aGame.setUser(user.getNickname());
			aGame.setName(req.getParameter("name"));
			aGame.setMajorVersion(Integer.parseInt(req.getParameter("major")));
			aGame.setMinorVersion(Integer.parseInt(req.getParameter("minor")));
			aGame.setDate(new Date());

			Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
			List<BlobKey> blobKeyList = blobs.get("blob");
			BlobKey blobKey = blobKeyList.get(0);
			aGame.setBlobKey(blobKey);

			if (aGame.getBlobKey() != null) {
				// persist the game, if one exists
				PersistenceManager pm = PMF.get().getPersistenceManager();
				try {
					pm.makePersistent(aGame);
				} finally {
					pm.close();
				}
			} else {
				log.warning("blob key was not found.");
			}
		} catch (Exception ex) {

		}

		resp.sendRedirect("/eujeux");
	}
}
