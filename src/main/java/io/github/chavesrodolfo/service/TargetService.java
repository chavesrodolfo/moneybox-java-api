package io.github.chavesrodolfo.service;

import org.springframework.data.domain.Page;

import io.github.chavesrodolfo.model.representations.TargetVO;

public interface TargetService {

	Page<TargetVO> listTargets(Integer page, Integer size, String username);

	TargetVO createTarget(String username, TargetVO targetVO);

	void deleteTarget(String username, String uuid);

	TargetVO updateTarget(String username, String uuid, TargetVO targetVO);

}
