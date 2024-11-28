package com.rekha.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.rekha.ecommerce.entity.Blog;

public interface BolgRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

}
