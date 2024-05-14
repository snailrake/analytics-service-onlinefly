package ru.onlinefly.analytics_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.onlinefly.analytics_service.model.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Query(nativeQuery = true, value = """
            SELECT answer.question_id
            FROM result
            JOIN answer ON result.id = answer.result_id
            WHERE result.fly_id = :flyId AND answer.correct = false
            GROUP BY answer.question_id
            ORDER BY COUNT(*) DESC
            LIMIT 1;
            """)
    long getHardestQuestion(long flyId);
}
