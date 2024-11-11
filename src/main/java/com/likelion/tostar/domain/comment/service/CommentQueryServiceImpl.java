package com.likelion.tostar.domain.comment.service;

import com.likelion.tostar.domain.comment.dto.CommentRequestDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * GET 요청은 여기에 작성
 */
@Service
public class CommentQueryServiceImpl implements CommentQueryService {

    @Override
    public List<CommentRequestDTO> getCommentsByArticleId(Long articleId) {
        return null;
    }
}
