package com.wanted.preonboarding.auth.entity.repository;

import com.wanted.preonboarding.auth.entity.BlackListToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListTokenRepository extends CrudRepository<BlackListToken, String> {
}
