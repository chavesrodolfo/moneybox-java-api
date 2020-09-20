package io.github.chavesrodolfo.service;

import org.springframework.data.domain.Page;

import io.github.chavesrodolfo.model.representations.UserVO;

public interface UserService {

	Page<UserVO> listUsers(Integer page, Integer size);

	UserVO listUsersByUuid(String uuid);

}
