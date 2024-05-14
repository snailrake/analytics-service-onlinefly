package ru.onlinefly.analytics_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.onlinefly.analytics_service.client.FlyServiceClient;
import ru.onlinefly.analytics_service.dto.FlyNameDto;
import ru.onlinefly.analytics_service.dto.QuestionContentDto;
import ru.onlinefly.analytics_service.dto.ResultDto;
import ru.onlinefly.analytics_service.dto.event.FlyEvent;
import ru.onlinefly.analytics_service.mapper.ResultMapper;
import ru.onlinefly.analytics_service.model.Answer;
import ru.onlinefly.analytics_service.model.Result;
import ru.onlinefly.analytics_service.repository.ResultRepository;
import ru.onlinefly.analytics_service.response.AllFlyAnalytics;
import ru.onlinefly.analytics_service.response.FlyAnalytics;
import ru.onlinefly.analytics_service.response.StudentAnalytics;
import ru.onlinefly.analytics_service.response.TeamAnalytics;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService {

    private final AnswerService answerService;
    private final ResultRepository resultRepository;
    private final FlyServiceClient flyServiceClient;
    private final ResultMapper resultMapper;

    @Transactional(readOnly = true)
    public List<ResultDto> getAllRateByTeamId(long teamId) {
        List<String> studentIds = resultRepository.findAllByTeamIdOrderByAvgScoreDesc(teamId);
        List<Result> results = studentIds.stream()
                .map(studentId -> {
                    double avg = resultRepository.getAvgStudentScore(studentId);
                    String name = flyServiceClient.getStudentNameById(studentId).getName();
                    long avgTime = resultRepository.getAvgStudentTime(studentId);
                    return Result.builder()
                            .score(avg)
                            .studentId(name)
                            .time(avgTime)
                            .build();
                }).toList();
        return resultMapper.toDto(results);
    }

    @Transactional(readOnly = true)
    public List<ResultDto> getAllRating() {
        List<String> studentIds = resultRepository.findAllOrderByScoreDesc();
        List<Result> results = studentIds.stream()
                .map(studentId -> {
                    double avg = resultRepository.getAvgStudentScore(studentId);
                    String name = flyServiceClient.getStudentNameById(studentId).getName();
                    long avgTime = resultRepository.getAvgStudentTime(studentId);
                    return Result.builder()
                            .score(avg)
                            .studentId(name)
                            .time(avgTime)
                            .build();
                }).toList();
        return resultMapper.toDto(results);
    }

    private List<ResultDto> getResultDto(List<Result> results) {
        return results.stream()
                .map(result -> {
                    ResultDto resultDto = resultMapper.toDto(result);
                    resultDto.setStudentId(flyServiceClient.getStudentNameById(resultDto.getStudentId()).getName());
                    resultDto.setTeam(flyServiceClient.getTeamById(result.getTeamId()));
                    resultDto.setFly(flyServiceClient.getShortFly(result.getFlyId()));
                    return resultDto;
                })
                .toList();
    }

    @Transactional
    public void handleFlyEvent(FlyEvent event) {
        Result result = resultRepository.save(Result.builder()
                .studentId(event.getStudentId())
                .teamId(event.getTeamId())
                .flyId(event.getFlyId())
                .score(event.getScore())
                .time(event.getTime())
                .build());
        event.getQuestionResponses()
                .forEach(questionResponse ->
                        answerService.save(Answer.builder()
                                .result(result)
                                .questionId(questionResponse.getQuestionId())
                                .isCorrect(questionResponse.isCorrect())
                                .build())
                );
    }

    @Transactional(readOnly = true)
    public StudentAnalytics getStudentAnalytics(String studentId, long teamId) {
        long avgTime = resultRepository.getAvgStudentTime(studentId);
        long hours = avgTime / 3600;
        long minutes = (avgTime % 3600) / 60;
        long seconds = avgTime % 60;
        String formattedAvgTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        double avgScore = resultRepository.getAvgStudentScore(studentId);
        long studentRank = resultRepository.findStudentRank(studentId, teamId);
        return StudentAnalytics.builder()
                .avgTime(formattedAvgTime)
                .avgScore(avgScore)
                .studentRank(studentRank)
                .build();
    }

    @Transactional(readOnly = true)
    public FlyAnalytics getFlyAnalytics(long flyId) {
        long avgTime = resultRepository.getAvgFlyTime(flyId);
        long hours = avgTime / 3600;
        long minutes = (avgTime % 3600) / 60;
        long seconds = avgTime % 60;
        String formattedAvgTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        double avgScore = resultRepository.getAvgFlyScore(flyId);
        long hardestQuestionId = answerService.getHardestQuestionId(flyId);
        QuestionContentDto hardestQuestion = flyServiceClient.getQuestionContent(hardestQuestionId);
        return FlyAnalytics.builder()
                .avgTime(formattedAvgTime)
                .avgScore(avgScore)
                .hardestQuestion(hardestQuestion.getContent())
                .build();
    }

    @Transactional(readOnly = true)
    public TeamAnalytics getTeamAnalytics(long teamId) {
        long avgTime = resultRepository.getAvgTeamTime(teamId);
        long hours = avgTime / 3600;
        long minutes = (avgTime % 3600) / 60;
        long seconds = avgTime % 60;
        String formattedAvgTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        double avgScore = resultRepository.getAvgTeamScore(teamId);
        String bestStudentId = resultRepository.getBestStudentOfTeam(teamId);
        String worstStudentId = resultRepository.getWorstStudentOfTeam(teamId);
        return TeamAnalytics.builder()
                .avgScore(avgScore)
                .avgTime(formattedAvgTime)
                .bestStudent(bestStudentId)
                .worstStudent(worstStudentId)
                .build();
    }

    @Transactional(readOnly = true)
    public AllFlyAnalytics getAllFlyAnalytics() {
        long avgTime = resultRepository.getAvgAllFlyTime();
        long hours = avgTime / 3600;
        long minutes = (avgTime % 3600) / 60;
        long seconds = avgTime % 60;
        String formattedAvgTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        double avgScore = resultRepository.getAvgAllFlyScore();
        long mostSuccessFlyId = resultRepository.getMostSuccessFly();
        long mostUnsuccessFlyId = resultRepository.getMostUnsuccessFly();
        FlyNameDto mostSuccessFly = flyServiceClient.getShortFly(mostSuccessFlyId);
        FlyNameDto mostUnsuccessFly = flyServiceClient.getShortFly(mostUnsuccessFlyId);
        return AllFlyAnalytics.builder()
                .avgTime(formattedAvgTime)
                .avgScore(avgScore)
                .mostSuccessFly(mostSuccessFly.getName())
                .mostUnsuccessFly(mostUnsuccessFly.getName())
                .build();
    }
}
