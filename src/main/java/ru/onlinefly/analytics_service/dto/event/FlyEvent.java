package ru.onlinefly.analytics_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.onlinefly.analytics_service.dto.QuestionResponseDto;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlyEvent {

    private String studentId;
    private long teamId;
    private long flyId;
    private String name;
    private String email;
    private String flyName;
    private double score;
    private long time;
    private List<QuestionResponseDto> questionResponses;
}
