package pl.gulci.ppmback.exceptions;

import lombok.Data;

@Data
public class UniqueProjectIdExceptionResponse {

    // TODO: 20.01.19 unique project id exception - id not unique (next id shouldn't be omitted) (continue 9min)

    private String projectIdentifier;

    public UniqueProjectIdExceptionResponse(String projectIdentifier) {
        this.projectIdentifier = projectIdentifier;
    }
}
