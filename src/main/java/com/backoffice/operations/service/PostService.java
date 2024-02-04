package com.backoffice.operations.service;

import java.util.List;

import com.backoffice.operations.payloads.PostDto;
import com.backoffice.operations.payloads.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto);

    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    PostDto getPostById(String id);

    PostDto updatePost(PostDto postDto, String id);

    void deletePostById(String id);

    List<PostDto> getPostsByCategory(String categoryId);
}
