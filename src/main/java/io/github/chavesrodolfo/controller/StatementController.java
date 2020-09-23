package io.github.chavesrodolfo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.chavesrodolfo.model.representations.StatementVO;
import io.github.chavesrodolfo.service.StatementService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class StatementController {
    
    @Autowired
    private StatementService statementService;

    @GetMapping("/statement")
    @PreAuthorize("hasRole('USER')")
    public Page<StatementVO> fetchStatement(final Principal principal, 
            @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") final Integer size, 
            @RequestParam(value = "target", required = false) final String targetUuid) {

        log.info("Username {} is fetching statement", principal.getName());

        Page<StatementVO> statementVO = statementService.fetchStatement(principal.getName(), page, size, targetUuid);

        return statementVO;
    }

    @PostMapping("/statement")
    @PreAuthorize("hasRole('USER')")
    public StatementVO newDeposit(final Principal principal, @RequestBody final StatementVO statementVO) {

        log.info("Username {} is doing a new deposit", principal.getName());

        return statementService.newDeposit(principal.getName(), statementVO);
    }

    @PutMapping("/statement/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public StatementVO updateStatement(final Principal principal, @RequestBody final StatementVO statementVO, @PathVariable("uuid") final String uuid) {

        log.info("Username {} is updating a statement", principal.getName());

        return statementService.updateStatement(principal.getName(), statementVO, uuid);
    }
}
