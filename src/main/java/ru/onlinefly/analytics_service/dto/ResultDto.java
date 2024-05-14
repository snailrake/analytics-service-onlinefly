package ru.onlinefly.analytics_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {

    private long id;
    private String studentId;
    private TeamDto team;
    private FlyNameDto fly;
    private String score;
    private String time;
}
