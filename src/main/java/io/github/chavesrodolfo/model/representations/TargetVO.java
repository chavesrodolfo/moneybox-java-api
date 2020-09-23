package io.github.chavesrodolfo.model.representations;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TargetVO  extends RepresentationModel<TargetVO> {
    private String uuid;
    private String title;
    private String description;
    private Double finalValue;
    private Double monthlyValue;
    private Double initialValue;
    private Date eventDate;
    private String status;
}
