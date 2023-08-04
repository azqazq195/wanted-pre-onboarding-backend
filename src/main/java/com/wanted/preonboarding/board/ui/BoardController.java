package com.wanted.preonboarding.board.ui;

import com.wanted.preonboarding.board.application.BoardService;
import com.wanted.preonboarding.board.application.dto.BoardRequest;
import com.wanted.preonboarding.board.application.dto.BoardResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardResponse> create(
            @RequestBody @Valid BoardRequest boardRequest
    ) {
        BoardResponse response = boardService.create(boardRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid BoardRequest boardRequest
    ) {
        BoardResponse response = boardService.update(id, boardRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        boardService.delete(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> retrieve(
            @PathVariable Long id
    ) {
        BoardResponse response = boardService.retrieve(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<Page<BoardResponse>> retrieveAll(
            Pageable pageable
    ) {
        Page<BoardResponse> response = boardService.retrieveAll(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
