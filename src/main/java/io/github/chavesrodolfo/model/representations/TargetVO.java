package io.github.chavesrodolfo.model.representations;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TargetVO  extends RepresentationModel<TargetVO> {
    private String uuid;
    private String title;
    private String description;
    private Double value;
}
