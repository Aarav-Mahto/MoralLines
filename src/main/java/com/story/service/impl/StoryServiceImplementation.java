package com.story.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.story.entity.AllStory;
import com.story.entity.RegisterUser;
import com.story.repository.AllStoryRepository;
import com.story.repository.UserRepository;
import com.story.service.StoryService;

@Service
public class StoryServiceImplementation implements StoryService{

	private static final Logger logger = LoggerFactory.getLogger(StoryServiceImplementation.class);
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AllStoryRepository allStoryRepository;

	public StoryServiceImplementation() {
		super();
	}


	@Override
	public boolean saveUser(RegisterUser registerUser) {
		try {
			userRepository.save(registerUser);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public RegisterUser getUserById(String id) {
		Optional<RegisterUser> userOptional = userRepository.findById(id);
		RegisterUser registerUser = userOptional.orElse(null); //// Get the user if found, or null if not found
		return registerUser;
	}
	@Override
	public boolean findUserId(String id) {
		return userRepository.existsById(id);
	}

	
	//Story CRUD Operation =================================================================================
	@Override
	public boolean saveStory(AllStory allStory) {
		try {
			allStoryRepository.save(allStory);
			return true;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean findStoryId(String storyId) {
		return (allStoryRepository.isIdExist(storyId) == 0);
	}
	@Override
	public AllStory findStoryById(String storyId) {
		Optional<AllStory> storyOptional = allStoryRepository.findStoryOfId(storyId);
		AllStory allStory = storyOptional.orElse(null);
		return allStory;
	}
	@Override
	public AllStory findStoryByIdAndCategory(String storyId, String category) {
		try {
			if(category.trim().equals("")) {
				Optional<AllStory> optional = allStoryRepository.findStoryOfId(storyId);
				AllStory allStory = optional.orElse(null);
				return allStory;
			}
			else {
				AllStory allStory = allStoryRepository.findByIdAndCategory(storyId,category);
				return allStory;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateStory(AllStory allStory) {
		Optional<AllStory> oldData = allStoryRepository.findStoryOfId(allStory.getStoryId());
		if(oldData.isPresent()) {
			AllStory existing = oldData.get();
			existing.setTitle(allStory.getTitle());
			existing.setDescription(allStory.getDescription());
			existing.setStory(allStory.getStory());
			existing.setCategory(allStory.getCategory());
			existing.setImg(allStory.getImg());
			existing.setEntryDate(allStory.getEntryDate());
			existing.setEnteredBy(allStory.getEnteredBy());
			existing.setUpdatedDate(allStory.getUpdatedDate());
			existing.setUpdatedBy(allStory.getUpdatedBy());

			allStoryRepository.save(existing);
			return true;
		}
		else {
			return false;
		}
	}
	@Override
	public boolean deleteById(String storyId) {
		try {
			allStoryRepository.deleteStoryOfId(storyId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public List<AllStory> allStories() {
		try {
			List<AllStory> allStories = allStoryRepository.findAll();
			return allStories;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<AllStory> next20StoryFromId(Long index, Long totalRecords) {
		long startIndex = index+1;
		List<AllStory> list = allStoryRepository.findNext20Records(startIndex);
		return list.stream()
	               .limit(totalRecords)  // Programmatic limiting
	               .collect(Collectors.toList());
	}

	@Override
	public List<AllStory> next20StoryByCategory(String category, Long index, Long totalRecords) {
		long startIndex = index+1;
		List<AllStory> list = allStoryRepository.findNext20RecordsByCategory(category, startIndex);
		return list.stream()
	               .limit(totalRecords)  // Programmatic limiting
	               .collect(Collectors.toList());
	}


	@Override
	public List<AllStory> showDB(Long lastIndex, Long limit) {
		lastIndex = lastIndex+1;
		try {
			List<AllStory> list = allStoryRepository.find50Records(lastIndex);
			if(list != null) {
				return list.stream()
			               .limit(limit)  // Programmatic limiting
			               .collect(Collectors.toList());
			}
		} catch (Exception e) {
			logger.warn("Error Occured while fetching 50 records in showDB() function");
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	@Override
	public List<AllStory> selectAllStoriesByCategory(String category) {
		List<AllStory> list = allStoryRepository.selectAllStoryByCategory(category);
        return list;
	}


	@Override
	public List<Map<String, String>> sitemapData() {
		return allStoryRepository.sitemap();
	}

}
