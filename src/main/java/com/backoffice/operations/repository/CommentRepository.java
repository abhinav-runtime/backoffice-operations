package com.backoffice.operations.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backoffice.operations.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String> {
    List<Comment> findByPostId(String postId);
}
