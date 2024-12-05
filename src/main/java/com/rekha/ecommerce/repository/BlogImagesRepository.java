package com.rekha.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rekha.ecommerce.entity.Blog;
import com.rekha.ecommerce.entity.BlogImages;

@Repository
public interface BlogImagesRepository extends JpaRepository<BlogImages, Long>, JpaSpecificationExecutor<BlogImages>{

	@Query(value = "SELECT * FROM tb_blog_images WHERE blog_id IN (:blogIds)", nativeQuery = true)
	List<BlogImages> getImagesByBlogId(@Param("blogIds") List<Long> blogIds);

}
