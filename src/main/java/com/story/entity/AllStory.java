package com.story.entity;

import java.sql.Date;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "allStory")
public class AllStory {

	@Id
	@Column(nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name="storyId", nullable = false, unique = true)
	private String storyId;
	@Column(name = "storyTitle", columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
	private String title;
	@Column(name = "description", columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
	private String description;
	@Column(name = "search_Desc",columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
	private String desc_for_search;
	@Column(name = "fullStory",columnDefinition = "LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
	private String story;
	@Column(name = "imageName")
	private String img;
	@Column(name = "category",columnDefinition = "TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci")
	private String category;

	private Date entryDate;
	private String enteredBy;
	private Date updatedDate;
	private String updatedBy;
	@Transient
	private MultipartFile partFile;

	// Auto-generated constructor stub
	public AllStory() {
		super();
	}

	public AllStory(String storyId, String title, String description, String story, String category, MultipartFile partFile) {
		super();
		this.storyId = storyId;
		this.title = title;
		this.description = description;
		this.story = story;
		this.category = category;
		this.partFile = partFile;
	}


	public AllStory(String storyId, String title, String description, String desc_for_search, String story, String img, String category, Date entryDate,
			String enteredBy, Date updatedDate, String updatedBy, MultipartFile partFile) {
		super();
		this.storyId = storyId;
		this.title = title;
		this.description = description;
		this.desc_for_search = desc_for_search;
		this.story = story;
		this.img = img;
		this.category = category;
		this.entryDate = entryDate;
		this.enteredBy = enteredBy;
		this.updatedDate = updatedDate;
		this.updatedBy = updatedBy;
		this.partFile = partFile;
	}

	public String getDesc_for_search() {
		return desc_for_search;
	}

	public void setDesc_for_search(String desc_for_search) {
		this.desc_for_search = desc_for_search;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStoryId() {
		return storyId;
	}

	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public MultipartFile getPartFile() {
		return partFile;
	}

	public void setPartFile(MultipartFile partFile) {
		this.partFile = partFile;
	}

}
