package com.story.service;

import java.util.List;
import java.util.Map;

import com.story.entity.AllStory;
import com.story.entity.RegisterUser;

public interface StoryService {

	public boolean saveUser(RegisterUser registerUser);

	public RegisterUser getUserById(String id);

	public boolean findUserId(String id);


	//Service for AllStory
	public boolean saveStory(AllStory allStory);

	public boolean findStoryId(String storyId);

	public AllStory findStoryById(String storyId);

	public AllStory findStoryByIdAndCategory(String storyId, String category);

	public boolean updateStory(AllStory allStory);

	public boolean deleteById(String storyId);

	public List<AllStory> allStories();

	public List<AllStory> next20StoryFromId(Long startIndex, Long endIndex);

	public List<AllStory> next20StoryByCategory(String category, Long startIndex, Long endIndex);
	
	public List<AllStory> showDB( Long lastIndex, Long limit);
	
	public List<AllStory> selectAllStoriesByCategory(String category);
	
	public List<Map<String, String>> sitemapData();
}



