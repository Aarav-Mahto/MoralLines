package com.story.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.story.entity.AllStory;
import com.story.helper.Helper;
import com.story.service.StoryService;

@Controller
public class PageController {

	@Autowired
	private StoryService storyService;

	@Autowired
	Helper helper;
	
	@GetMapping({"/","/hindi-story", "/hindi-story/"})
	public String homePage() {	
		return "index";
	}

	
	@PostMapping("/hindi-story/load20RecordsByCategory")
	@ResponseBody
	public ResponseEntity<Map<Long, Map<String, String>>> load20RecordsByCategory(@RequestParam("category")String category,@RequestParam("startIndex") Long startIndex){
		long totalRecords = 20;
        
		List<AllStory> allStories = new ArrayList<>();
		if(category.trim().equalsIgnoreCase("AllNewStory")) {
			allStories = storyService.next20StoryFromId(startIndex, totalRecords);
		}
		else {
			allStories = storyService.next20StoryByCategory(category, startIndex, totalRecords);
		}
		if(allStories.size()!=0) {
			Map<Long, Map<String, String>> map = new HashMap<>();
			for(AllStory a : allStories) {
				Map<String, String> tempMap = new HashMap<>();
				tempMap.put("storyId", a.getStoryId());
				tempMap.put("storyTitle", a.getTitle());
				tempMap.put("storyDesc", a.getDescription());
				String imagePath = File.separator + "StoryWebsite_Image" + File.separator + a.getImg();
				tempMap.put("imgFullPath", imagePath);
				tempMap.put("imgName", a.getImg().toString());
				map.put(a.getId(), tempMap);
			}
			return new ResponseEntity<>(map,HttpStatus.OK);
		}
		else {
			return null;
		}
	}

	@GetMapping("/hindi-story/view/{myId}")
	public String singlePage() {
		return "viewPage";
	}

	@PostMapping("/hindi-story/loadMainContent")
	@ResponseBody
	public ResponseEntity<Map<String, String>> loadMainContent(@RequestParam("storyId")String storyId){
		AllStory allStories = storyService.findStoryById(storyId);
		Map<String, String> map = new HashMap<>();
		if(allStories != null) {
			map.put("sno", String.valueOf(allStories.getId()));
			map.put("storyId", allStories.getStoryId());
			map.put("storyTitle", allStories.getTitle());
			map.put("storyDesc", allStories.getDescription());
			String imagePath = File.separator + "StoryWebsite_Image" + File.separator + allStories.getImg();
			map.put("imgFullPath", imagePath);
			map.put("story", allStories.getStory());
			map.put("category", allStories.getCategory());
			return new ResponseEntity<>(map,HttpStatus.OK);
		}
		else {
			return null;
		}
	}

	@PostMapping("/hindi-story/navigationTabs")
	@ResponseBody
	public ResponseEntity<Map<Long, Map<String, String>>> navigationTabs(@RequestParam("what") String categories) {
		//String path = helper.getImageUrl();
        
        Map<Long, Map<String, String>> map = new HashMap<>();
		List<AllStory> allStories = storyService.allStories();
		if(allStories != null) {
			for (AllStory allStory : allStories) {
				Map<String, String> tmpMap = new HashMap<>();
				tmpMap.put("storyId", allStory.getStoryId());
				tmpMap.put("storyTitle", allStory.getTitle());
				tmpMap.put("storyDesc", allStory.getDescription());
				String imagePath = File.separator + "StoryWebsite_Image" + File.separator + allStory.getImg();
				tmpMap.put("imgFullPath", imagePath);
				tmpMap.put("imgName", allStory.getImg().toString());
				map.put(allStory.getId(),tmpMap);
			}
		}
		else {
			map=null;
		}

		return new ResponseEntity<>(map,HttpStatus.OK);
	}

	@PostMapping("/hindi-story/recommendation")
	@ResponseBody
	public ResponseEntity<Map<Long, Map<String, String>>> recommendation(@RequestParam("category")String category){
		long totalRecords = 5;
		long startIndex = 0;

		List<AllStory> allStories = storyService.next20StoryByCategory(category, startIndex, totalRecords);
		if(allStories.size()!=0) {
			Map<Long, Map<String, String>> map = new HashMap<>();
			for(AllStory a : allStories) {
				Map<String, String> tempMap = new HashMap<>();
				tempMap.put("storyId", a.getStoryId());
				tempMap.put("storyTitle", a.getTitle());
				tempMap.put("storyDesc", a.getDescription());
				tempMap.put("storyImg", a.getImg());
				String imagePath = File.separator + "StoryWebsite_Image" + File.separator + a.getImg();
				tempMap.put("imgFullPath", imagePath);
				tempMap.put("imgName", a.getImg().toString());
				map.put(a.getId(), tempMap);
			}
			return new ResponseEntity<>(map,HttpStatus.OK);
		}
		else {
			return null;
		}
	}
	
	
	@GetMapping("/contactUs")
	public String contactUs() {
		return "contact_us";
	}
	@GetMapping("/terms")
	public String Terms() {
		return "terms";
	}
	@GetMapping("/policy")
	public String policy() {
		return "policy";
	}
	@GetMapping("/disclimer")
	public String disclimer() {
		return "disclimer";
	}
	
	@GetMapping(value = "/hindi-story/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
	public String sitemapPage(Model model) {
    	List<Map<String, String>> list = storyService.sitemapData();
    	model.addAttribute("sitemap",list);
		return "/sitemap";
	}
}
