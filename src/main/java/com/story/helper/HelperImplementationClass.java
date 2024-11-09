package com.story.helper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

import com.story.controller.AdminController;

import jakarta.servlet.http.HttpSession;

public class HelperImplementationClass implements Helper{

	public static final Logger logger = Logger.getLogger(AdminController.class.getName());
	
	@Autowired
	private ResourceLoader resourceLoader;

	@Override
	public void removeSession(HttpSession session) {
		if (session.getAttribute("view") != null) {
	        session.removeAttribute("view");
	    }

	    if (session.getAttribute("sessionMsg") != null) {
	        session.removeAttribute("sessionMsg");
	    }
	}

	@Override
	public Date currentDate() {
		LocalDate localDate = LocalDate.now();
		Date date = Date.valueOf(localDate);
		return date;
	}

	@Override
	public void setColorMsg(HttpSession session, String c, String msg) {
		session.setAttribute("sessionMsg", msg);
		session.setAttribute("color", c);
	}

	@Override
	public String uploadImage(MultipartFile partFile) {
		if(partFile !=null && !partFile.isEmpty()) {
	    	try {
	           
	            Path rootClassPath = Paths.get(resourceLoader.getResource("classpath:/").getURI()).toAbsolutePath().getParent().getParent().getParent();
    	        Path targetPath =rootClassPath.resolve("StoryWebsite_Image");

	            if(Files.exists(targetPath) || Files.isDirectory(targetPath)) {
	            	Path imageFilePath =targetPath.resolve(partFile.getOriginalFilename());
		    		Files.copy(partFile.getInputStream(), imageFilePath, StandardCopyOption.REPLACE_EXISTING);
		    		return partFile.getOriginalFilename();	 
		    	}
	    		else {
	    			logger.warning("Error: The 'StoryWebsite_Image' Folder or directory does not exist or is not a directory.");
	    			return null;
	    		}

	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		return null;
	    	}
	    }
		else {
			return null;
		}
	}

	@Override
	public boolean deleteFile(String imageFileName) {
        try {
        	
        	Path rootClassPath = Paths.get(resourceLoader.getResource("classpath:/").getURI()).toAbsolutePath().getParent().getParent().getParent();
	        Path targetPath =rootClassPath.resolve("StoryWebsite_Image");
	        if(Files.exists(targetPath) || Files.isDirectory(targetPath)) {
            	Path imageFilePath =targetPath.resolve(imageFileName);
            	Files.delete(imageFilePath);
                logger.info("File " + imageFileName + " deleted successfully.");
                return true;
	        }
	        else {
            	logger.warning("File " + imageFileName + " does not exist or is not a file.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	@Override
	public boolean isNullOrEmpty(String... fields) {//Similar to 'String[] fields'
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

	@Override
	public String getImageUrl() {
		try {
			Path rootClassPath = Paths.get(resourceLoader.getResource("classpath:/").getURI()).toAbsolutePath().getParent().getParent().getParent();
	        String projectName = rootClassPath.getFileName().toString();
	        System.out.println(projectName + File.separator + "StoryWebsite_Image");
	        return projectName + File.separator + "StoryWebsite_Image";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void setUserInfoSession(HttpSession session, String name, String email, String accessType) {
		session.setAttribute("user_name", name);
		session.setAttribute("user_email", email);
		session.setAttribute("user_type", accessType);
		
	}

}
