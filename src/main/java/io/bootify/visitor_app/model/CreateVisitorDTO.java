package io.bootify.visitor_app.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateVisitorDTO {

    @NotNull
    @Size(max = 255)
    private String line1;

    @Size(max = 255)
    private String line2;

    @Size(max = 255)
    private String city;

    @Size(max = 255)
    private String state;

    @NotNull
    @Size(max = 6)
    private String pincode;


    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String phone;

    @Size(max = 255)
    private String email;
}
