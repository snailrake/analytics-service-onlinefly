package ru.onlinefly.analytics_service.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.onlinefly.analytics_service.dto.ResultDto;
import ru.onlinefly.analytics_service.response.AllFlyAnalytics;
import ru.onlinefly.analytics_service.response.FlyAnalytics;
import ru.onlinefly.analytics_service.response.StudentAnalytics;
import ru.onlinefly.analytics_service.response.TeamAnalytics;
import ru.onlinefly.analytics_service.service.ResultService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/analytics")
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/rating")
    public List<ResultDto> getAllRating() {
        return resultService.getAllRating();
    }

    @GetMapping("/rating/team/{id}")
    public List<ResultDto> getRatingByTeamId(@PathVariable @Positive int id) {
        return resultService.getAllRateByTeamId(id);
    }

    @GetMapping("/student/{id}")
    public StudentAnalytics getStudentAnalytics(@PathVariable String id, @RequestParam long teamId) {
        return resultService.getStudentAnalytics(id, teamId);
    }

    @GetMapping("/fly/{id}")
    public FlyAnalytics getFlyAnalytics(@PathVariable long id) {
        return resultService.getFlyAnalytics(id);
    }

    @GetMapping("/team/{id}")
    public TeamAnalytics getTeamAnalytics(@PathVariable @Positive long id) {
        return resultService.getTeamAnalytics(id);
    }

    @GetMapping("/fly")
    public AllFlyAnalytics getAllFlyAnalytics() {
        return resultService.getAllFlyAnalytics();
    }
}