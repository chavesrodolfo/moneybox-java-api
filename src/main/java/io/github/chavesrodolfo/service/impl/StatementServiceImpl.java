package io.github.chavesrodolfo.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.github.chavesrodolfo.exceptions.InvalidDataException;
import io.github.chavesrodolfo.exceptions.StatementNotFoundException;
import io.github.chavesrodolfo.exceptions.TargetNotFoundException;
import io.github.chavesrodolfo.exceptions.UserNotFoundException;
import io.github.chavesrodolfo.model.Statement;
import io.github.chavesrodolfo.model.Target;
import io.github.chavesrodolfo.model.User;
import io.github.chavesrodolfo.model.representations.StatementVO;
import io.github.chavesrodolfo.model.representations.TargetVO;
import io.github.chavesrodolfo.repository.StatementRepository;
import io.github.chavesrodolfo.repository.TargetRepository;
import io.github.chavesrodolfo.repository.UserRepository;
import io.github.chavesrodolfo.service.StatementService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatementServiceImpl implements StatementService {

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    TargetRepository targetRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<StatementVO> fetchStatement(final String username, final Integer page, final Integer size, final String targetUuid) {
        log.info("fetchStatement page {} size {} username {}", page, size, username);

        final PageRequest pageRequest = PageRequest.of(page, size);

        final User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        final Optional<Target> target = targetRepository.findTargetByUuidAndUser(targetUuid, user);

        Page<Statement> statement;

        //check if it's a stetement of an unique target or it's a full
        if (target.isPresent()) {
            statement = statementRepository.findStatementByTargetAndUser(pageRequest, target.get(), user);
        } else {
            statement = statementRepository.findStatementByUser(pageRequest, user);
        }

        return statement.map(this::convertToStatementVO);

    }

    private StatementVO convertToStatementVO(final Statement statement) {
        final StatementVO statementVO = modelMapper.map(statement, StatementVO.class);
        final TargetVO targetVO = modelMapper.map(statement.getTarget(), TargetVO.class);
        statementVO.setTargetVO(targetVO);
        return statementVO;
    }

    @Override
    public StatementVO newDeposit(final String username, StatementVO statementVO) {

        log.info("newDeposit username {}", username);

        final User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        validateStatement(statementVO);

        final Target target = targetRepository.findTargetByUuidAndUser(statementVO.getTargetVO().getUuid(), user)
            .orElseThrow(() -> new TargetNotFoundException(statementVO.getTargetVO().getUuid()));

        Statement statement = modelMapper.map(statementVO, Statement.class);

        statement.setUser(user);
        statement.setTarget(target);

        statement = statementRepository.save(statement);

        return modelMapper.map(statement, StatementVO.class);
    }

    private void validateStatement(StatementVO statementVO) {
        if(statementVO.getValue() == null || statementVO.getValue()  <= 0) {
            throw new InvalidDataException("value >0 is required");
        } 

        if(statementVO.getTargetVO() == null || StringUtils.isEmpty(statementVO.getTargetVO().getUuid())) {
            throw new InvalidDataException("targetVO{uuid} is required");
        } 
    }

    @Override
    public StatementVO updateStatement(final String username, StatementVO statementVO, String uuid) {

        log.info("updateStatement username {} statment {}", username, uuid);

        validateStatement(statementVO);

        final User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        //find user's statement and verify if it exists
        Statement statement = statementRepository.findStatementByUuidAndUser(uuid, user).orElseThrow(()-> new StatementNotFoundException(uuid));

        final Target target = targetRepository.findTargetByUuidAndUser(statementVO.getTargetVO().getUuid(), user)
            .orElseThrow(() -> new TargetNotFoundException(statementVO.getTargetVO().getUuid()));

        //update fields
        statement.setDetails(statementVO.getDetails());
        statement.setValue(statementVO.getValue());
        statement.setTarget(target);

        statement = statementRepository.save(statement);
        log.info("Statement {} updated for user {}", uuid, username);

        return modelMapper.map(statement, StatementVO.class);
    }

    
}
