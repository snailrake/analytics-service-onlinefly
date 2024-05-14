package ru.onlinefly.analytics_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAnalytics {

    private String avgTime;
    private double avgScore;
    private long studentRank;
}
