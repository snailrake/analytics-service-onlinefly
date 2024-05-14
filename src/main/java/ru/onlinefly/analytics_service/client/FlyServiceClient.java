package ru.onlinefly.analytics_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.onlinefly.analytics_service.dto.FlyNameDto;
import ru.onlinefly.analytics_service.dto.QuestionContentDto;
import ru.onlinefly.analytics_service.dto.StudentNameDto;
import ru.onlinefly.analytics_service.dto.TeamDto;

@FeignClient(name = "fly-service", url = "${fly-service.host}:${fly-service.port}")
public interface FlyServiceClient {

    @GetMapping("/api/v1/question/content/{id}")
    QuestionContentDto getQuestionContent(@PathVariable long id);

    @GetMapping("/api/v1/student/name/{id}")
    StudentNameDto getStudentNameById(@PathVariable String id);

    @GetMapping("/api/v1/fly/name/{id}")
    FlyNameDto getShortFly(@PathVariable long id);

    @GetMapping("/api/v1/team/{id}")
    TeamDto getTeamById(@PathVariable long id);
}