package io.github.chavesrodolfo.controller;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.chavesrodolfo.model.representations.UserVO;
import io.github.chavesrodolfo.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
    public Page<UserVO> listUsers(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page, 
        @RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

            Page<UserVO> userDtoPage = userService.listUsers(page, size);

		return userDtoPage;
    }
    
    @GetMapping("/users/{uuid}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getUser(@PathVariable("uuid") String uuid) {

        UserVO userDTO = userService.listUsersByUuid(uuid);
    
        userDTO.add(linkTo(methodOn(UserController.class).getUser(uuid)).withSelfRel(),
        linkTo(methodOn(UserController.class).listUsers(1,10)).withRel("users"));

        return ResponseEntity.ok(userDTO);
	}
}