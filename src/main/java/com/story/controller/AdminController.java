package com.story.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.story.entity.AllStory;
import com.story.entity.RegisterUser;
import com.story.helper.Helper;
import com.story.security.JWTFetchingData;
import com.story.service.StoryService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController{

	public static final Logger logger = Logger.getLogger(AdminController.class.getName());

	@Autowired
	Helper helper;

	@Autowired
	HttpSession session;

    public HttpSession getSession() {
        return session;
    }
	
    @Autowired
    private JWTFetchingData jwtFetchingData;
    
	@Autowired
	private StoryService storyService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/indexHeader")
    public String getIndexHeader(Model model) {
        return "home";
    }

//	@GetMapping("/login")
//	public String loginPage(Model model) {
//		helper.removeSession(session);
//		RegisterUser registerUser = new RegisterUser();
//		model.addAttribute("registerClass",registerUser);
//		return "authenticate";
//	}
//	@PostMapping("/loginData")
//	public String redirectLogin(@RequestParam("username_email") String username, @RequestParam("password_pass") String password) {
//		RegisterUser user1  = storyService.getUserAndPassword(username, password);
//		if(user1 != null) {
//			System.out.println("Email: "+user1.getEmail()+"\tName: "+user1.getName()+"\tUser Type: "+user1.getUser_type());
//		}
//		else {
//			System.out.println("User Not Found");
//		}
//		return "redirect:/dashboard";
//	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		//removeSession();  //Don't apply this method here
		
		Map<String, String> userCredential = jwtFetchingData.getUserInfoSession();
		if(userCredential != null)
			helper.setUserInfoSession(session, userCredential.get("name"), userCredential.get("email"), userCredential.get("accessType"));
		
		AllStory allStory = new AllStory();
		model.addAttribute("allStory",allStory);
		RegisterUser registerUser = new RegisterUser();
		model.addAttribute("registerClass",registerUser);
		return "adminPannel";
	}

	@PostMapping("/btnNavigateAjax")
	public String btnNavigate(@RequestParam("view") String view, Model model) {
		helper.removeSession(session);
		session.setAttribute("view", view);
		if(view.equals("insert") || view.equals("update") || view.equals("delete") || view.equals("viewDatabase")) {
			AllStory allStory = new AllStory();
			model.addAttribute("allStory",allStory);
			if(view.equals("update")) {
				session.setAttribute("updateView", "searchEntry");
			}
			if(view.equals("delete")) {
				session.setAttribute("deleteView", "searchEntry");
			}
			if(view.equals("viewDatabase")) {
				return "redirect:/showDB/"+0;
			}
		}
		else if(view.equals("register")) {
			RegisterUser registerUser = new RegisterUser();
			model.addAttribute("registerClass",registerUser);
		}
		return "adminPannel";
	}
//==========Showing Database =====================================	
	@PostMapping("/handleShowDB")
	@ResponseBody
	public ResponseEntity<String> handleShowDBPage() {
		String redirectUrl = "/showDB";
        return ResponseEntity.ok(redirectUrl);
	}
	
	@GetMapping("/showDB")
	public String showDBPage(Model model) {
		session.setAttribute("view", "viewDatabase");
		long lastIndex = 0;
		long limit = 10000;
		List<AllStory> list = storyService.showDB(lastIndex, limit);
		Map<String, String> map = new LinkedHashMap<>();
		map.put("aarav", "Aarav Mahto");
		model.addAttribute("name",map);
		if(list != null) {
			model.addAttribute("list",list);
		}
		else {
			model.addAttribute("list","");
		}
		return "adminPannel";
	}
	
	@PostMapping("/showStory")
	@ResponseBody
	public String showAbout(@RequestParam("sid") String storyId) {
		if(storyId != null || !storyId.equals("")) {
			AllStory allAtory = storyService.findStoryById(storyId);
			if(allAtory != null) {
				String story = allAtory.getStory();
				return story;
			}
			else {
				return "Story Not Found or not available, Refresh the page!";
			}
		}
		else {
			return "Story Id Not Matched!";
		}
	}
//==============================C R U D===============================================
	@PostMapping("/registerUser")
	public String registerUser(@ModelAttribute("registerUser") RegisterUser registerUser,Model model) {
		helper.removeSession(session);
		String editorEmail = (String) session.getAttribute("user_email") != null ? (String) session.getAttribute("user_email") : "aaravmahto";

		RegisterUser user1 = new RegisterUser(registerUser.getEmail().trim(),
											  registerUser.getName().trim(),
											  registerUser.getPhone(),
											  registerUser.getUser_type().trim(),
											  registerUser.getPassword().trim(),
											  registerUser.getConfirmPassword().trim(),
											  helper.currentDate(),
											  editorEmail);

		session.setAttribute("view", "register");

		if(helper.isNullOrEmpty(user1.getEmail(),user1.getName(),user1.getPhone(),user1.getPassword(),user1.getConfirmPassword())) {
			helper.setColorMsg(session,"red", "Failled! Fields are Empty.");
			model.addAttribute("registerClass",user1);
			return "adminPannel";
		}

		try {
			boolean checkUserId = storyService.findUserId(user1.getEmail());
			if(checkUserId) {
				helper.setColorMsg(session,"red", "This Email Id already Exists!");
				//RegisterUser rData = new RegisterUser(registerUser.getEmail(),registerUser.getName(),registerUser.getPhone(),registerUser.getPassword(),registerUser.getUser_type(),registerUser.getConfirmPassword());
				model.addAttribute("registerClass",user1);
				return "adminPannel";
			}
			
			if(user1.getPassword().equals(user1.getConfirmPassword())) {
				String encodedPassword = passwordEncoder.encode(user1.getPassword());
				user1.setPassword(encodedPassword);
				
				boolean save = storyService.saveUser(user1);
				if(save) {
					helper.setColorMsg(session,"darkgreen", "User Registration Successfully!");
					return "redirect:/dashboard";
				}
				else {
					helper.setColorMsg(session,"red", "Failled! Database Error.");
					return "redirect:/dashboard";
				}
			}
			else {
				helper.setColorMsg(session,"red", "Password & Confirm Password Must be Same");
				return "redirect:/dashboard";
			}
			
		} catch (Exception e) {
			helper.setColorMsg(session,"red", "Failled! Database Error.");
			return "redirect:/dashboard";
		}
	}

	@PostMapping("/insertData")
	public String insertData(@ModelAttribute("allStory") AllStory allStory,Model model) {
		helper.removeSession(session);
		session.setAttribute("view", "insert");
		if(!(helper.isNullOrEmpty(allStory.getStoryId(),allStory.getTitle(),allStory.getDescription(),allStory.getDesc_for_search() ,allStory.getStory()))) {
			String editorEmail = (String) session.getAttribute("user_email") != null ? (String) session.getAttribute("user_email") : "aaravmahto";

			AllStory allStory1 = new AllStory(
				allStory.getStoryId(),
				allStory.getTitle(),
				allStory.getDescription(),
				allStory.getDesc_for_search(),
				allStory.getStory(),
				allStory.getPartFile().getOriginalFilename(),
				allStory.getCategory(),
				 helper.currentDate(),
				 editorEmail,
				 null,
				 null,
				 allStory.getPartFile()
			  );

			try {
				//Finding If Duplicate Id
				boolean checkId = storyService.findStoryId(allStory1.getStoryId());
				if(!(checkId)) {
					model.addAttribute("allStory",allStory1);
					helper.setColorMsg(session,"red", allStory1.getStoryId()+" Id Already Exists!!");
					return "adminPannel";
				}
				//Image Uploading
				if(helper.uploadImage(allStory.getPartFile()) == null) {
					model.addAttribute("allStory",allStory1);
					helper.setColorMsg(session,"red", "Image Uploading Error");
					return "adminPannel";
				}
				//Inserting Data
				boolean saveStory = storyService.saveStory(allStory1);
				if(saveStory) {
					helper.setColorMsg(session,"darkgreen", "Story Saved Successfully!");
					return "redirect:/dashboard";
				}
				else {
					model.addAttribute("allStory",allStory);
					helper.setColorMsg(session,"red", "Failled! Database Error.");
					return "adminPannel";
				}
				

			} catch (Exception e) {
				model.addAttribute("allStory",allStory);
				helper.setColorMsg(session,"red", "Failled! Database Exceptions.");
				return "adminPannel";
			}
		}
		else {
			model.addAttribute("storyClass",allStory);
			helper.setColorMsg(session,"red", "Failled! Error During Image Uploading (or may be any mandatory field is Empty)");
			return "adminPannel";
		}
	}

	@PostMapping("/findData")
	public String findData(@ModelAttribute("allStory1") AllStory allStory1, Model model) {
		helper.removeSession(session);
		AllStory allStory= storyService.findStoryByIdAndCategory(allStory1.getStoryId(), allStory1.getCategory());
		if(allStory == null) {
			helper.setColorMsg(session,"red", "Not Found!");
			session.setAttribute("view", "update");
			model.addAttribute("allStory",allStory1);
			return "adminPannel";
		}
		session.setAttribute("id", allStory.getStoryId());
		session.setAttribute("img", allStory.getImg());
		session.setAttribute("entryDate", allStory.getEntryDate());
		session.setAttribute("enteredBy", allStory.getEnteredBy());
		session.setAttribute("view", "updateEntry");
		helper.setColorMsg(session,"darkgreen", "Edit Data!");
		model.addAttribute("allStory",allStory);
		return "adminPannel";
	}

	@PostMapping("/updateEntry")
	public String updateEntry(@RequestParam("imageDeleteKarnaHai") String imageDeleteKarnaHai,@ModelAttribute("allStory") AllStory allStory, Model model) {
		helper.removeSession(session);
		//Confirmation to delete old image
		MultipartFile file = allStory.getPartFile();
		if(imageDeleteKarnaHai.trim().equalsIgnoreCase("no")) {
			if(file.isEmpty()) {
				allStory.setImg((String)session.getAttribute("img"));
			} else {
				if(helper.uploadImage(allStory.getPartFile()) != null) {
					allStory.setImg(file.getOriginalFilename());
				}
				else {
					helper.setColorMsg(session, "red"," Failled! Error During Updating Image");
					session.setAttribute("view", "updateEntry");
					model.addAttribute("allStory",allStory);
					return "adminPannel";
				}
			}

		}
		else {
			//Checking If user Selected new Image
			if(allStory.getPartFile().isEmpty()) {
				helper.setColorMsg(session,"red", "You have not selected any Image!");
				session.setAttribute("view", "updateEntry");
				model.addAttribute("allStory",allStory);
				return "adminPannel";
			}
			else {
				if(helper.deleteFile((String)session.getAttribute("img")) && helper.uploadImage(allStory.getPartFile()) != null) {
					allStory.setImg(file.getOriginalFilename());
				}
				else {
					helper.setColorMsg(session, "red"," Failled! Error During Updating Image");
					session.setAttribute("view", "updateEntry");
					model.addAttribute("allStory",allStory);
					return "adminPannel";
				}
			}
		}
		//=========================================================================================

		allStory.setStoryId((String)session.getAttribute("id"));
		allStory.setUpdatedDate(helper.currentDate());
		String editorEmail = (String) session.getAttribute("user_email") != null ? (String) session.getAttribute("user_email") : "aaravmahto";

		allStory.setUpdatedBy(editorEmail);
		allStory.setEntryDate((Date)session.getAttribute("entryDate"));
		allStory.setEnteredBy((String)session.getAttribute("enteredBy"));

		if(!(helper.isNullOrEmpty(allStory.getTitle(),allStory.getDescription(),allStory.getStory()))) {
			try {
				boolean update = storyService.updateStory(allStory);
				if(update) {
					helper.setColorMsg(session,"darkgreen", allStory.getStoryId()+" Updated Successfully");
					session.setAttribute("view", "update");
					return "redirect:/dashboard";
				}
				else {
					helper.setColorMsg(session,"red", allStory.getStoryId()+" Failled! Some Error");
					session.setAttribute("view", "updateEntry");
					model.addAttribute("allStory",allStory);
					return "adminPannel";
				}
			} catch (Exception e) {
				e.printStackTrace();
				helper.setColorMsg(session,"red", allStory.getStoryId()+" Failled! Some Exception");
				session.setAttribute("view", "updateEntry");
				model.addAttribute("allStory",allStory);
				return "adminPannel";
			}
		}
		else {
			helper.setColorMsg(session,"red", allStory.getStoryId()+" all fields are mandatory");
			session.setAttribute("view", "updateEntry");
			model.addAttribute("allStory",allStory);
			return "adminPannel";
		}
	}

	@PostMapping("/findToDelete")
	public String findToDelete(@ModelAttribute("allStory1") AllStory allStory1, Model model) {
		helper.removeSession(session);
		AllStory allStory= storyService.findStoryByIdAndCategory(allStory1.getStoryId().trim(), allStory1.getCategory().trim());
		if(allStory == null) {
			helper.setColorMsg(session,"red", "Not Found!");
			session.setAttribute("view", "delete");
			model.addAttribute("allStory",allStory1);
			return "adminPannel";
		}
		session.setAttribute("deleteId", allStory1.getStoryId());
		session.setAttribute("imageName", allStory.getImg());
		helper.setColorMsg(session,"yellow", "Delete Entry");
		model.addAttribute("allStory",allStory);
		session.setAttribute("view", "deleteEntry");
		return "adminPannel";
	}

	@PostMapping("/deleteEntry")
	public String deleteEntry(@RequestParam("imageDeleteKarnaHai") String imageDeleteKarnaHai, @ModelAttribute("allStory") AllStory allStory, Model model) {
		helper.removeSession(session);
		allStory.setStoryId((String)session.getAttribute("deleteId"));
		String imgString = (String)session.getAttribute("imageName");
		boolean deleteFile;

		if(imageDeleteKarnaHai.trim().equalsIgnoreCase("no")) {
			deleteFile=true;
		} else {
			deleteFile = helper.deleteFile(imgString);
		}

		if(deleteFile) {
			boolean deleted = storyService.deleteById(allStory.getStoryId());
			if(deleted) {
				helper.setColorMsg(session,"darkgreen", allStory.getStoryId()+" Deleted Permanentlly");
				session.setAttribute("view", "delete");
				return "redirect:/dashboard";
			}
			else {
				helper.setColorMsg(session,"red"," Failled! Error Occured");
				session.setAttribute("view", "delete");
				model.addAttribute("allStory",allStory);
				return "adminPannel";
			}
		}
		else {
			helper.setColorMsg(session, "red"," Failled! Error During Deleting Image");
			session.setAttribute("view", "delete");
			model.addAttribute("allStory",allStory);
			return "adminPannel";
		}
	}

	@PostMapping("/next50Entries")
	public String next50Entries(@ModelAttribute("allStory") AllStory allStory, Model model) {
		helper.removeSession(session);
		model.addAttribute("allStory",allStory);
		session.setAttribute("view", "viewdatabase");
		return "adminPannel";
	}
	
	@PostMapping("/selectStoryByCategory")
	@ResponseBody
	public ResponseEntity<Map<Long, Map<String, String>>> selectStoryByCategory(@RequestParam("category")String category){    
		List<AllStory> allStories = new ArrayList<>();
		if(category.trim().equalsIgnoreCase("AllNewStory")) {
			long lastIndex = 0;
			long limit = 10000;
			allStories = storyService.showDB(lastIndex, limit);
		}
		else {
			allStories = storyService.selectAllStoriesByCategory(category);
		}
		if(allStories.size()!=0) {
			Map<Long, Map<String, String>> map = new HashMap<>();
			for(AllStory a : allStories) {
				Map<String, String> tempMap = new HashMap<>();
				tempMap.put("storyId", a.getStoryId());
				tempMap.put("storyTitle", a.getTitle());
				tempMap.put("storyDesc", a.getDescription());
				tempMap.put("storyDescForSearch", a.getDesc_for_search());
				tempMap.put("imgName", a.getImg().toString());
				tempMap.put("category", a.getCategory());
				String entryDate = (a.getEntryDate() !=null)?a.getEntryDate().toString():"";
				tempMap.put("entryDate", entryDate);
				tempMap.put("enteredBy", a.getEnteredBy());
				String updatedDate = (a.getUpdatedDate() !=null)?a.getUpdatedDate().toString():"";
				tempMap.put("updatedDate", updatedDate);
				tempMap.put("updatedBy", a.getUpdatedBy());
				map.put(a.getId(), tempMap);
			}
			return new ResponseEntity<>(map,HttpStatus.OK);
		}
		else {
			return null;
		}
	}

}
