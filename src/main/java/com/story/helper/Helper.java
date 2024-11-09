package com.story.helper;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

public interface Helper {

	public Date currentDate();
	public String uploadImage(MultipartFile partFile);
	public boolean deleteFile(String fileName);
	public boolean isNullOrEmpty(String... fields);
	public void setColorMsg(HttpSession session, String c, String msg);
	public void removeSession(HttpSession session);
	public String getImageUrl();
	public void setUserInfoSession(HttpSession session, String name, String email, String accessType);
}
