package io.github.chavesrodolfo.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.github.chavesrodolfo.exceptions.UserNotFoundException;
import io.github.chavesrodolfo.model.User;
import io.github.chavesrodolfo.model.representations.UserVO;
import io.github.chavesrodolfo.repository.UserRepository;
import io.github.chavesrodolfo.service.UserService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<UserVO> listUsers(final Integer page, final Integer size) {
        log.info("listUsers page {} size {}", page, size);
        
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "username");

        final Page<User> users = userRepository.findActiveUsers(pageRequest);

        return users.map(this::convertToUserVO);

    }

    private UserVO convertToUserVO(final User user) {
        return modelMapper.map(user, UserVO.class);
    }

    @Override
    public UserVO listUsersByUuid(final String uuid) {
        log.info("listUsersByUuid uuid {}", uuid);

        final User user = userRepository.findByUuid(uuid).orElseThrow(() -> new UserNotFoundException(uuid));

        final UserVO userDTO = modelMapper.map(user, UserVO.class);

        return userDTO;
    }
    
}