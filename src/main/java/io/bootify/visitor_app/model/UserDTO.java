package io.bootify.visitor_app.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String phone;

    @Size(max = 255)
    private String email;

    private Long flatId;

    private Long addressId;

    @NotNull
    private Long roleId;

}
