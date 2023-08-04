package com.wanted.preonboarding.board.application.mapper;

import com.wanted.preonboarding.board.application.dto.BoardRequest;
import com.wanted.preonboarding.board.entity.Board;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {
    public Board dtoToEntity(BoardRequest dto) {
        return dto.toEntity();
    }
}
