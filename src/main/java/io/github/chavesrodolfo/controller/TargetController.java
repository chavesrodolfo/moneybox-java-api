package io.github.chavesrodolfo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.chavesrodolfo.model.representations.TargetVO;
import io.github.chavesrodolfo.service.TargetService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TargetController {

    @Autowired
    private TargetService targetService;

    @GetMapping("/targets")
    @PreAuthorize("hasRole('USER')")
    public Page<TargetVO> listTargets(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        log.info("Username {} fetching targets", username);

        return targetService.listTargetByUser(page, size, username);
    }
}
