package io.github.chavesrodolfo.service;

import org.springframework.data.domain.Page;

import io.github.chavesrodolfo.model.representations.StatementVO;

public interface StatementService {
    
    Page<StatementVO> fetchStatement(String username, Integer page, Integer size, String targetUuid);

    StatementVO newDeposit(String username, StatementVO statementVO);
    
    StatementVO updateStatement(String username, StatementVO statementVO, String uuid);

}
