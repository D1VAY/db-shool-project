-- Table: public.groups

CREATE TABLE groups
(
    group_id serial                NOT NULL,
    name     character varying(25) NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (group_id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

