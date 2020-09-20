package io.github.chavesrodolfo.model.representations;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserVO extends RepresentationModel<UserVO> {
    private String uuid;
    private String username;
    private boolean active;
    private String roles;
}