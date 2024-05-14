package ru.onlinefly.analytics_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.onlinefly.analytics_service.model.Result;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

    @Query(nativeQuery = true, value = """
            SELECT avg(time) FROM result
            WHERE result.student_id = :studentId
            """)
    long getAvgStudentTime(String studentId);

    @Query(nativeQuery = true, value = """
            SELECT avg(score) FROM result
            WHERE result.student_id = :studentId
            """)
    double getAvgStudentScore(String studentId);

    @Query(nativeQuery = true, value = """
            SELECT avg(time) FROM result
            WHERE result.fly_id = :flyId
            """)
    long getAvgFlyTime(long flyId);

    @Query(nativeQuery = true, value = """
            SELECT avg(score) FROM result
            WHERE result.fly_id = :flyId
            """)
    double getAvgFlyScore(long flyId);

    @Query(nativeQuery = true, value = """
            SELECT avg(score) FROM result
            WHERE result.team_id = :teamId
            """)
    double getAvgTeamScore(long teamId);

    @Query(nativeQuery = true, value = """
            SELECT avg(time) FROM result
            WHERE result.team_id = :teamId
            """)
    long getAvgTeamTime(long teamId);

    @Query(nativeQuery = true, value = """
            SELECT result.student_id
            FROM result
            WHERE team_id = :teamId
            GROUP BY student_id
            ORDER BY sum(score) DESC
            LIMIT 1
            """)
    String getBestStudentOfTeam(long teamId);

    @Query(nativeQuery = true, value = """
            SELECT result.student_id
            FROM result
            WHERE team_id = :teamId
            GROUP BY student_id
            ORDER BY sum(score)
            LIMIT 1
            """)
    String getWorstStudentOfTeam(long teamId);

    @Query(nativeQuery = true, value = """
            SELECT avg(time) FROM result
            """)
    long getAvgAllFlyTime();

    @Query(nativeQuery = true, value = """
            SELECT avg(score) FROM result
            """)
    long getAvgAllFlyScore();

    @Query(nativeQuery = true, value = """
            SELECT result.fly_id
            FROM result
            GROUP BY fly_id
            ORDER BY sum(score) DESC
            LIMIT 1
            """)
    long getMostSuccessFly();

    @Query(nativeQuery = true, value = """
            SELECT result.fly_id
            FROM result
            GROUP BY fly_id
            ORDER BY sum(score)
            LIMIT 1
            """)
    long getMostUnsuccessFly();

    @Query(nativeQuery = true, value = """
            SELECT result.student_id
            FROM result
            GROUP BY student_id
            ORDER BY AVG(score) DESC;
            """)
    List<String> findAllOrderByScoreDesc();

    @Query(nativeQuery = true, value = """
            SELECT result.student_id
            FROM result
            WHERE team_id = :teamId
            GROUP BY student_id
            ORDER BY AVG(score) DESC;
            """)
    List<String> findAllByTeamIdOrderByAvgScoreDesc(long teamId);

    @Query(nativeQuery = true, value = """
            WITH average_scores AS (
                SELECT student_id, AVG(score) AS average_score
                FROM result
                WHERE team_id = :teamId
                GROUP BY student_id
            ),
            fly_counts AS (
                SELECT student_id, COUNT(*) AS fly_count
                FROM result
                WHERE team_id = :teamId
                GROUP BY student_id
            ),
            ranked_results AS (
                SELECT a.student_id, 
                       DENSE_RANK() OVER (ORDER BY a.average_score DESC, f.fly_count DESC, a.student_id) AS position
                FROM average_scores a
                JOIN fly_counts f ON a.student_id = f.student_id
            )
            SELECT position
            FROM ranked_results
            WHERE student_id = :studentId
            """)
    long findStudentRank(String studentId, long teamId);
}
