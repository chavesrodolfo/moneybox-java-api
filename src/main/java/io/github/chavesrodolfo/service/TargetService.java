package io.github.chavesrodolfo.service;

import org.springframework.data.domain.Page;

import io.github.chavesrodolfo.model.representations.TargetVO;

public interface TargetService {

	Page<TargetVO> listTargetByUser(Integer page, Integer size, String username);

}
