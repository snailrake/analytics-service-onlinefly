package ru.onlinefly.analytics_service.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "result")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "student_id", nullable = false)
    private String studentId;

    @Column(name = "team_id", nullable = false)
    private long teamId;

    @Column(name = "fly_id", nullable = false)
    private long flyId;

    @Column(name = "score", nullable = false)
    private double score;

    @Column(name = "time", nullable = false)
    private long time;

    @OneToMany(mappedBy = "result", cascade = CascadeType.REMOVE)
    private List<Answer> answers;
}
