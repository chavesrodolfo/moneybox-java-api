package io.github.chavesrodolfo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.chavesrodolfo.model.representations.MessageResponse;
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
    public Page<TargetVO> listTargets(final Principal principal, @RequestParam(value = "page", required = false, defaultValue = "0") final Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") final Integer size) {

        log.info("Username {} is fetching targets", principal.getName());

        return targetService.listTargets(page, size, principal.getName());
    }

    @PostMapping("/targets")
    @PreAuthorize("hasRole('USER')")
    public TargetVO createTarget(final Principal principal, @RequestBody final TargetVO targetVO) {

        log.info("Username {} is creating a target", principal.getName());

        return targetService.createTarget(principal.getName(), targetVO);
    }

    @DeleteMapping("/targets/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public MessageResponse deleteTarget(final Principal principal, @PathVariable("uuid") final String uuid) {

        log.info("Username {} is deleting the target {}", principal.getName(), uuid);

        targetService.deleteTarget(principal.getName(), uuid);

        return new MessageResponse(HttpStatus.OK.toString(), "Target deleted.");
    }

    @PutMapping("/targets/{uuid}")
    @PreAuthorize("hasRole('USER')")
    public TargetVO updateTarget(final Principal principal, @PathVariable("uuid") final String uuid, @RequestBody final TargetVO targetVO) {

        log.info("Username {} is updating the target {}", principal.getName(), uuid);

        return targetService.updateTarget(principal.getName(), uuid, targetVO);
    }
}
