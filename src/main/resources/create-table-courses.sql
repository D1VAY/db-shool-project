-- Table: public.course

CREATE TABLE courses
(
    course_id          serial                 NOT NULL,
    course_name        character varying(250)  NOT NULL,
    course_description character varying(250) NOT NULL,
    CONSTRAINT courses_pkey PRIMARY KEY (course_id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;
