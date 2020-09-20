package io.github.chavesrodolfo.service.impl;


import java.lang.reflect.Type;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public Page<TargetVO> listTargetByUser(Integer page, Integer size, String username) {
        log.info("listTargetByUser page {} size {} username {}", page, size, username);
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "title");
        
        Optional<User> user = userRepository.findByUsername(username);
        
        final Page<Target> targets = targetRepository.findTargetsByUser(pageRequest, user.get());
        
        final ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<Page<TargetVO>>(){}.getType();
        Page<TargetVO> targetsPage = modelMapper.map(targets,listType);

        return targetsPage;
    }
    
}
