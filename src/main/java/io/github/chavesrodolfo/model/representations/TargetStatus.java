package io.github.chavesrodolfo.model.representations;

import lombok.Getter;

@Getter
public enum TargetStatus {
    ACTIVE("ACTIVE"),
    FINISHED("FINISHED"),
    DELETED("DELETED");

    private final String status;

    private TargetStatus(String status) {
        this.status = status;
    }
}
