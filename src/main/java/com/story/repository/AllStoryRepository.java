package com.story.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.story.entity.AllStory;

import jakarta.transaction.Transactional;

@Repository
public interface AllStoryRepository extends JpaRepository<AllStory, Long> {

	//JPQL
		@Query("SELECT COUNT(s) FROM AllStory s WHERE s.storyId = :storyId")
		int isIdExist(@Param("storyId") String storyId);

		@Query("SELECT s FROM AllStory s WHERE s.storyId = :storyId")
	    Optional<AllStory> findStoryOfId(@Param("storyId") String storyId);

		@Query("SELECT s FROM AllStory s WHERE s.storyId = :storyId AND s.category = :c")
		AllStory findByIdAndCategory(@Param("storyId") String storyId, @Param("c") String category);

		@Transactional
		@Modifying
		@Query("DELETE FROM AllStory s WHERE s.storyId = :storyId")
	    void deleteStoryOfId(@Param("storyId") String storyId);

		@Query(value = "SELECT * FROM all_story WHERE id >= :startIndex ORDER BY id ASC", nativeQuery=true)
		List<AllStory> findNext20Records(@Param("startIndex") Long startIndex);

		@Query(value = "SELECT * FROM all_story WHERE category = :category AND id >= :startIndex ORDER BY id ASC", nativeQuery=true)
		List<AllStory> findNext20RecordsByCategory(@Param("category")String category, @Param("startIndex") Long startIndex);

		@Query(value = "SELECT * FROM all_story WHERE id >= :startIndex ORDER BY id ASC", nativeQuery=true)
		List<AllStory> find50Records(@Param("startIndex") Long startIndex);
		
		@Query(value = "SELECT story_id AS story_id, entry_date AS entry_date FROM all_story ORDER BY id ASC", nativeQuery = true)
	    List<Map<String, String>> sitemap();
		
		@Query("SELECT s FROM AllStory s WHERE s.category = :c")
		List<AllStory> selectAllStoryByCategory(@Param("c") String category);
}
