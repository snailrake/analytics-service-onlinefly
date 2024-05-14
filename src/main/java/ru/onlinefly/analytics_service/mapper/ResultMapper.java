package ru.onlinefly.analytics_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.onlinefly.analytics_service.dto.ResultDto;
import ru.onlinefly.analytics_service.model.Result;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResultMapper {

    @Mapping(target = "score", source = "score", qualifiedByName = "scoreToString")
    @Mapping(target = "time", source = "time", qualifiedByName = "timeToString")
    ResultDto toDto(Result result);

    List<ResultDto> toDto(List<Result> results);

    @Named("scoreToString")
    default String toScoreString(double score) {
        return String.format("%.2f%%", score);
    }

    @Named("timeToString")
    default String toTimeString(long time) {
        long hours = time / 3600;
        long minutes = (time % 3600) / 60;
        long seconds = time % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
