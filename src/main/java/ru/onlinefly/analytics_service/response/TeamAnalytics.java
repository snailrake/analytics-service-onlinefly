package ru.onlinefly.analytics_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamAnalytics {

    private double avgScore;
    private String avgTime;
    private String bestStudent;
    private String worstStudent;
}
