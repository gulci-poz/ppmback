package pl.gulci.ppmback.exceptions;

import lombok.Data;

@Data
public class UniqueProjectIdExceptionResponse {

    private String projectIdentifier;

    public UniqueProjectIdExceptionResponse(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }
}
