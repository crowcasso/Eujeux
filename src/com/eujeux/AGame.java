package com.eujeux;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;

@PersistenceCapable
public class AGame {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String name;
	
	@Persistent
	private int majorVersion;
	
	@Persistent
	private int minorVersion;
	
	@Persistent
	private Date date;
	
	@Persistent
	private String user;
	
	@Persistent
	private BlobKey blobKey;
	
	public AGame() { }
	public AGame(String name, String user, BlobKey blobKey) {
		this.name = name;
		this.user = user;
		this.blobKey = blobKey;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public BlobKey getBlobKey() {
		return blobKey;
	}
	
	public void setBlobKey(BlobKey blobKey) {
		this.blobKey = blobKey;
	}
	
	public int getMajorVersion() {
		return majorVersion;
	}
	
	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}
	
	public int getMinorVersion() {
		return minorVersion;
	}
	
	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
}