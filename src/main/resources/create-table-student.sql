-- Table: public.students

CREATE SEQUENCE students_id_seq;

CREATE TABLE students
(
    student_id integer               NOT NULL DEFAULT nextval('students_id_seq'),
    group_id   integer,
    first_name character varying(25) NOT NULL,
    last_name  character varying(25) NOT NULL,
    CONSTRAINT students_pkey PRIMARY KEY (student_id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;
