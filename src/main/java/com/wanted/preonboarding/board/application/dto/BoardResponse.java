package com.wanted.preonboarding.board.application.dto;

import com.wanted.preonboarding.board.entity.Board;

import java.time.LocalDateTime;

public record BoardResponse(
        Long id,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public BoardResponse(Board board) {
        this(board.getId(), board.getTitle(), board.getContent(), board.getCreatedAt(), board.getModifiedAt());
    }
}