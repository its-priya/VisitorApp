package io.bootify.visitor_app.model;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VisitDTO {

    private Long id;

    @NotNull
    private VisitStatus status;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    @Size(max = 255)
    private String imageUrl;

    @Size(max = 255)
    private String purpose;

    private Integer totalVisitors;

    @NotNull
    private Long flatId;

    @NotNull
    private Long visitorId;

}
