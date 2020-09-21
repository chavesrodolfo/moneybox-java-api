package io.github.chavesrodolfo.service.impl;


import java.lang.reflect.Type;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.chavesrodolfo.exceptions.TargetNotFoundException;
import io.github.chavesrodolfo.model.Target;
import io.github.chavesrodolfo.model.User;
import io.github.chavesrodolfo.model.representations.TargetVO;
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

    public Page<TargetVO> listTargets(Integer page, Integer size, String username) {
        log.info("listTargetByUser page {} size {} username {}", page, size, username);
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
        
        Optional<User> user = userRepository.findByUsername(username);
        
        final Page<Target> targets = targetRepository.findTargetsByUser(pageRequest, user.get());
        
        final ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<Page<TargetVO>>(){}.getType();
        Page<TargetVO> targetsPage = modelMapper.map(targets,listType);

        return targetsPage;
    }

    @Override
    public TargetVO createTarget(String username, TargetVO targetVO) {
       
        //transform to entity
        final ModelMapper modelMapper = new ModelMapper();
        Target target = modelMapper.map(targetVO, new TypeToken<Target>(){}.getType());

        //setting additional attributes
        Optional<User> user = userRepository.findByUsername(username);
        target.setUser(user.get());
        
        target = targetRepository.save(target);

        //transform to VO
        targetVO = modelMapper.map(target, new TypeToken<TargetVO>(){}.getType());

        return targetVO;
    }

    @Override
    public void deleteTarget(String username, String uuid) {

        Optional<User> user = userRepository.findByUsername(username);

       //find user's target and verify if it exists
       Target target = targetRepository.findTargetByUuidAndUser(uuid, user.get()).orElseThrow(() -> new TargetNotFoundException(uuid));

        try {
            targetRepository.delete(target);
            log.info("Target {} deleted for user {}", uuid, username);
        } catch (NoSuchElementException e) {
            log.error("Target not found {}", uuid);
            throw new TargetNotFoundException(uuid);
        } catch (Exception e) {
            log.error("Unexpected error. Target not found {}", uuid, e);
        }
    }

    @Override
    public TargetVO updateTarget(String username, String uuid, TargetVO targetVO) {
        Optional<User> user = userRepository.findByUsername(username);

        //find user's target and verify if it exists
        Target target = targetRepository.findTargetByUuidAndUser(uuid, user.get()).orElseThrow(() -> new TargetNotFoundException(uuid));

        //update fields
        target.setTitle(targetVO.getTitle());
        target.setDescription(targetVO.getDescription());
        target.setValue(targetVO.getValue());

        try {
            target = targetRepository.save(target);
            log.info("Target {} updated for user {}", uuid, username);
        } catch (NoSuchElementException e) {
            log.error("Target not found {}", uuid);
            throw new TargetNotFoundException(uuid);
        } catch (Exception e) {
            log.error("Unexpected error. Target not found {}", uuid, e);
        }

        return targetVO;
    }
    
}
