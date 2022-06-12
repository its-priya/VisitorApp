package io.bootify.visitor_app.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FlatDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String flatNumber;

}
