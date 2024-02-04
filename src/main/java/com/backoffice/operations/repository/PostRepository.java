package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {

    List<Post> findByCategoryId(String categoryId);

}
