package com.wanted.preonboarding.board.application;

import com.wanted.preonboarding.auth.utils.AuthUtils;
import com.wanted.preonboarding.board.application.dto.BoardRequest;
import com.wanted.preonboarding.board.application.dto.BoardResponse;
import com.wanted.preonboarding.board.application.exception.BoardNotFoundException;
import com.wanted.preonboarding.board.application.exception.UnauthorizedHandleBoardException;
import com.wanted.preonboarding.board.application.mapper.BoardMapper;
import com.wanted.preonboarding.board.entity.Board;
import com.wanted.preonboarding.board.entity.repository.BoardRepository;
import com.wanted.preonboarding.fixtures.BoardFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private BoardMapper boardMapper;

    @InjectMocks
    private BoardService boardService;

    private static MockedStatic<AuthUtils> authUtilsMockedStatic = mockStatic(AuthUtils.class);

    @Test
    public void create() {
        // given
        BoardRequest boardRequest = BoardFixtures.createBoardRequest();
        Board board = BoardFixtures.createBoard();
        BoardResponse boardResponse = BoardFixtures.createBoardResponse(board);

        when(boardMapper.dtoToEntity(boardRequest)).thenReturn(board);
        when(boardRepository.save(board)).thenReturn(board);

        // when
        BoardResponse result = boardService.create(boardRequest);

        // then
        assertEquals(boardResponse, result);

        verify(boardMapper, times(1)).dtoToEntity(boardRequest);
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    public void update() {
        // given
        Long id = 1L;
        BoardRequest boardRequest = BoardFixtures.createBoardRequest();
        Board board = BoardFixtures.createBoard();
        BoardResponse boardResponse = BoardFixtures.createBoardResponse(board);

        when(boardRepository.findById(id)).thenReturn(Optional.of(board));
        when(AuthUtils.getLoginUserId()).thenReturn(board.getCreatedBy());
        when(boardMapper.dtoToEntity(boardRequest)).thenReturn(board);
        when(boardRepository.save(board)).thenReturn(board);

        // when
        BoardResponse result = boardService.update(id, boardRequest);

        // then
        assertEquals(boardResponse, result);

        verify(boardRepository, times(1)).findById(id);
        verify(boardMapper, times(1)).dtoToEntity(boardRequest);
        verify(boardRepository, times(1)).save(board);
    }

    @Test
    public void update_BoardNotFound() {
        // given
        Long id = 1L;
        BoardRequest boardRequest = BoardFixtures.createBoardRequest();

        when(boardRepository.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(BoardNotFoundException.class, () -> boardService.update(id, boardRequest));

        // then
        verify(boardRepository, times(1)).findById(id);
    }

    @Test
    public void delete() {
        Long id = 1L;
        Board board = BoardFixtures.createBoard();

        when(boardRepository.findById(id)).thenReturn(Optional.of(board));
        when(AuthUtils.getLoginUserId()).thenReturn(board.getCreatedBy());
        doNothing().when(boardRepository).deleteById(board.getId());

        boardService.delete(id);

        verify(boardRepository, times(1)).findById(id);
        verify(boardRepository, times(1)).deleteById(board.getId());
    }

    @Test
    public void delete_BoardNotFound() {
        // given
        Long id = 1L;

        when(boardRepository.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(BoardNotFoundException.class, () -> boardService.delete(id));

        // then
        verify(boardRepository, times(1)).findById(id);
    }

    @Test
    public void retrieve() {
        // given
        Long id = 1L;
        Board board = BoardFixtures.createBoard();
        BoardResponse boardResponse = BoardFixtures.createBoardResponse(board);

        when(boardRepository.findById(id)).thenReturn(Optional.of(board));

        // when
        BoardResponse result = boardService.retrieve(id);

        // then
        assertEquals(boardResponse, result);
        verify(boardRepository, times(1)).findById(id);
    }

    @Test
    public void retrieve_BoardNotFound() {
        // given
        Long id = 1L;

        when(boardRepository.findById(id)).thenReturn(Optional.empty());

        // when
        assertThrows(BoardNotFoundException.class, () -> boardService.retrieve(id));

        // then
        verify(boardRepository, times(1)).findById(id);
    }

    @Test
    public void retrieveAll() {
        // given
        Pageable pageable = PageRequest.of(0, 10); // 예를 들어 첫 페이지에 10개의 항목을 요청
        List<Board> boardList = Arrays.asList(BoardFixtures.createBoard(), BoardFixtures.createBoard());
        Page<Board> boardPage = new PageImpl<>(boardList, pageable, boardList.size());

        when(boardRepository.findAll(pageable)).thenReturn(boardPage);

        // when
        Page<BoardResponse> result = boardService.retrieveAll(pageable);

        // then
        assertEquals(boardList.size(), result.getContent().size());
        for (int i = 0; i < boardList.size(); i++) {
            assertEquals(new BoardResponse(boardList.get(i)), result.getContent().get(i));
        }

        verify(boardRepository, times(1)).findAll(pageable);
        verify(boardRepository, times(1)).findAll(pageable);
    }

    @Test
    public void checkOwner() {
        // given
        Board board = BoardFixtures.createBoard();

        when(AuthUtils.getLoginUserId()).thenReturn(board.getId());

        // when, then
        boardService.checkOwner(board);
    }

    @Test
    public void checkOwner_NotOwner() {
        // given
        Board board = BoardFixtures.createBoard();

        when(AuthUtils.getLoginUserId()).thenReturn(-1L);

        // when, then
        assertThrows(UnauthorizedHandleBoardException.class, () -> boardService.checkOwner(board));
    }
}
