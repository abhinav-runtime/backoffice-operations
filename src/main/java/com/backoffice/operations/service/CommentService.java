package com.backoffice.operations.service;

import java.util.List;

import com.backoffice.operations.payloads.CommentDto;

public interface CommentService {
    CommentDto createComment(String postId, CommentDto commentDto);

    List<CommentDto> getCommentsByPostId(String postId);

    CommentDto getCommentById(String postId, String commentId);

    CommentDto updateComment(String postId, String commentId, CommentDto commentRequest);

    void deleteComment(String postId, String commentId);
}
