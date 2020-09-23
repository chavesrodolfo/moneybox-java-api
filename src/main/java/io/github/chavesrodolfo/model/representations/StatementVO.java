package io.github.chavesrodolfo.model.representations;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StatementVO extends RepresentationModel<StatementVO>{
    private String uuid;
    private String details;
    private Double value;
    private TargetVO targetVO;
    private UserVO userVO;
    private Date creationDate;
}
