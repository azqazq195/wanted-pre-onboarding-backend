package com.wanted.preonboarding.board.application;

import com.wanted.preonboarding._common.domain.BaseAuditingEntity;
import com.wanted.preonboarding.auth.utils.AuthUtils;
import com.wanted.preonboarding.board.application.dto.BoardRequest;
import com.wanted.preonboarding.board.application.dto.BoardResponse;
import com.wanted.preonboarding.board.application.exception.BoardNotFoundException;
import com.wanted.preonboarding.board.application.exception.UnauthorizedHandleBoardException;
import com.wanted.preonboarding.board.application.mapper.BoardMapper;
import com.wanted.preonboarding.board.entity.Board;
import com.wanted.preonboarding.board.entity.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public BoardResponse create(BoardRequest boardRequest) {
        Board board = boardMapper.dtoToEntity(boardRequest);
        return new BoardResponse(boardRepository.save(board));
    }

    public BoardResponse update(Long id, BoardRequest boardRequest) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        checkOwner(board);

        Board updateValue = boardMapper.dtoToEntity(boardRequest);
        board.update(updateValue);
        return new BoardResponse(boardRepository.save(board));
    }

    public void delete(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        checkOwner(board);

        boardRepository.deleteById(board.getId());
    }

    public BoardResponse retrieve(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(BoardNotFoundException::new);
        return new BoardResponse(board);
    }

    public Page<BoardResponse> retrieveAll(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        return boards.map(BoardResponse::new);
    }

    protected void checkOwner(BaseAuditingEntity entity) {
        Long requestedUserid = AuthUtils.getLoginUserId();
        if (!Objects.equals(entity.getCreatedBy(), requestedUserid)) {
            throw new UnauthorizedHandleBoardException();
        }
    }
}
