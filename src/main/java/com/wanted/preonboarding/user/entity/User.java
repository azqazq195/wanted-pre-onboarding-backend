package com.wanted.preonboarding.user.entity;

import com.wanted.preonboarding._common.domain.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User extends BaseEntity {
    private String email;
    private String password;
}
