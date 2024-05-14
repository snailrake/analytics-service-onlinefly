CREATE TABLE result (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    student_id varchar(256) NOT NULL,
    team_id bigint NOT NULL,
    fly_id bigint NOT NULL,
    score decimal(4,1) NOT NULL,
    time bigint NOT NULL
);

CREATE TABLE answer (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    result_id bigint NOT NULL,
    question_id bigint NOT NULL,
    correct boolean NOT NULL
);

ALTER TABLE answer ADD CONSTRAINT fk_result_id FOREIGN KEY (result_id) REFERENCES result (id) ON DELETE CASCADE;
