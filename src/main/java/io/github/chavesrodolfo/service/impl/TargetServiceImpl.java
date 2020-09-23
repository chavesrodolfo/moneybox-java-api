package io.github.chavesrodolfo.service.impl;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.github.chavesrodolfo.exceptions.InvalidDataException;
import io.github.chavesrodolfo.exceptions.TargetNotFoundException;
import io.github.chavesrodolfo.model.Target;
import io.github.chavesrodolfo.model.User;
import io.github.chavesrodolfo.model.representations.TargetStatus;
import io.github.chavesrodolfo.model.representations.TargetVO;
import io.github.chavesrodolfo.repository.StatementRepository;
import io.github.chavesrodolfo.repository.TargetRepository;
import io.github.chavesrodolfo.repository.UserRepository;
import io.github.chavesrodolfo.service.TargetService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TargetServiceImpl implements TargetService {

    @Autowired
    TargetRepository targetRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Page<TargetVO> listTargets(String username, Integer page, Integer size, boolean activeOnly) {
        log.info("listTargetByUser page {} size {} username {}", page, size, username);
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
        
        Optional<User> user = userRepository.findByUsername(username);
        
        //check if should find activeOnly
        Page<Target> targets;
        if (activeOnly) {
            targets = targetRepository.findTargetsActiveOnlyByUser(pageRequest, user.get());
        } else {
            targets = targetRepository.findTargetsByUser(pageRequest, user.get());
        }

        return targets.map(this::convertToTargetVO);
    }

    private TargetVO convertToTargetVO(final Target target) {
        return modelMapper.map(target, TargetVO.class);
    }

    @Override
    public TargetVO createTarget(String username, TargetVO targetVO) {
       
        validateTarget(targetVO);

        targetVO.setStatus(TargetStatus.ACTIVE.getStatus());

        //transform to entity
        Target target = modelMapper.map(targetVO, Target.class);

        //setting additional attributes
        Optional<User> user = userRepository.findByUsername(username);
        target.setUser(user.get());
        
        target = targetRepository.save(target);

        //transform to VO
        targetVO = modelMapper.map(target, TargetVO.class);

        return targetVO;
    }

    private void validateTarget(TargetVO targetVO) {
        if (StringUtils.isEmpty(targetVO.getTitle())) {
            throw new InvalidDataException("targetVO{title} is required");
        }
        if (targetVO.getFinalValue() == null || targetVO.getFinalValue() <= 0) {
            throw new InvalidDataException("targetVO{finalValue} must be > 0");
        }
        if (targetVO.getMonthlyValue() == null || targetVO.getMonthlyValue() < 0) {
            throw new InvalidDataException("targetVO{monthlyValue} must be >= 0");
        }
        if (targetVO.getInitialValue() == null || targetVO.getInitialValue() < 0) {
            throw new InvalidDataException("targetVO{initialValue} must be >= 0");
        }
        if (targetVO.getEventDate() == null || targetVO.getEventDate().before(Calendar.getInstance().getTime())) {
            throw new InvalidDataException("targetVO{eventDate} must be after current date");
        }
    }

    @Override
    public void deleteTarget(String username, String uuid) {

        Optional<User> user = userRepository.findByUsername(username);

       //find user's target and verify if it exists
       Target target = targetRepository.findTargetByUuidAndUser(uuid, user.get()).orElseThrow(() -> new TargetNotFoundException(uuid));

        try {
            boolean existStetement = statementRepository.countByTargetAndUser(target, user.get()) > 0L ? true : false;

            if (existStetement) {
                target.setStatus(TargetStatus.DELETED.getStatus());
                targetRepository.save(target);
                log.info("Target {} virtually deleted for user {}", uuid, username);
            } else {
                targetRepository.delete(target);
                log.info("Target {} deleted for user {}", uuid, username);
            }
            
        } catch (NoSuchElementException e) {
            log.error("Target not found {}", uuid);
            throw new TargetNotFoundException(uuid);
        } catch (Exception e) {
            log.error("Unexpected error. Target not found {}", uuid, e);
        }
    }

    @Override
    public TargetVO updateTarget(String username, String uuid, TargetVO targetVO) {

        validateTarget(targetVO);

        Optional<User> user = userRepository.findByUsername(username);

        //find user's target and verify if it exists
        Target target = targetRepository.findTargetByUuidAndUser(uuid, user.get()).orElseThrow(() -> new TargetNotFoundException(uuid));

        //update fields
        target.setTitle(targetVO.getTitle());
        target.setDescription(targetVO.getDescription());
        target.setFinalValue(targetVO.getFinalValue());
        target.setInitialValue(targetVO.getInitialValue());
        target.setMonthlyValue(targetVO.getMonthlyValue());
        target.setEventDate(targetVO.getEventDate());

        target = targetRepository.save(target);
        log.info("Target {} updated for user {}", uuid, username);

        return targetVO;
    }
    
}
