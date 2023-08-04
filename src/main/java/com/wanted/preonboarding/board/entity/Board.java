package com.wanted.preonboarding.board.entity;

import com.wanted.preonboarding._common.domain.BaseAuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Board extends BaseAuditingEntity {
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    public void update(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
    }
}

