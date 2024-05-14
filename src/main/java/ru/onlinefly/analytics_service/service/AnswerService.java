package ru.onlinefly.analytics_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.onlinefly.analytics_service.model.Answer;
import ru.onlinefly.analytics_service.repository.AnswerRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;

    public Answer save(Answer answer) {
        return answerRepository.save(answer);
    }

    public long getHardestQuestionId(long flyId) {
        return answerRepository.getHardestQuestion(flyId);
    }
}
