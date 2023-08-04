package com.wanted.preonboarding.fixtures;

import com.wanted.preonboarding.board.application.dto.BoardRequest;
import com.wanted.preonboarding.board.application.dto.BoardResponse;
import com.wanted.preonboarding.board.entity.Board;

import java.time.LocalDateTime;

public class BoardFixtures {
    public static Board createBoard() {
        return createBoard("title", "content");
    }

    public static Board createBoard(
            String title,
            String content
    ) {
        return new Board(title, content);
    }

    public static BoardResponse createBoardResponse() {
        return createBoardResponse(1L, "title", "content", LocalDateTime.now(), LocalDateTime.now());
    }

    public static BoardResponse createBoardResponse(Board board) {
        return createBoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getCreatedAt(),
                board.getModifiedAt()
        );
    }

    public static BoardResponse createBoardResponse(
            Long id,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        return new BoardResponse(id, title, content, createdAt, modifiedAt);
    }

    public static BoardRequest createBoardRequest() {
        return createBoardRequest("title", "content");
    }

    public static BoardRequest createBoardRequest(
            String title,
            String content
    ) {
        return new BoardRequest(title, content);
    }
}
