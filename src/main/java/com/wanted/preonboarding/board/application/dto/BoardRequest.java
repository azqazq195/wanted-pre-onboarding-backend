package com.wanted.preonboarding.board.application.dto;

import com.wanted.preonboarding.board.entity.Board;
import jakarta.validation.constraints.NotBlank;

public record BoardRequest(
        @NotBlank String title,
        @NotBlank String content
) {
    public Board toEntity() {
        return new Board(title, content);
    }
}
