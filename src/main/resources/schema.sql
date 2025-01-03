DROP TABLE IF EXISTS todo;
DROP SEQUENCE IF EXISTS todo_id_seq;
DROP TABLE IF EXISTS category;
DROP SEQUENCE IF EXISTS category_id_seq;

CREATE SEQUENCE IF NOT EXISTS category_id_seq
    INCREMENT 1
    START 14
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
ALTER SEQUENCE category_id_seq OWNER TO postgres;

-- Table: category
CREATE TABLE IF NOT EXISTS category
(
    id integer NOT NULL DEFAULT nextval('category_id_seq'::regclass),
    name character varying(255) COLLATE pg_catalog."default",
    color character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT category_pkey PRIMARY KEY (id)
)TABLESPACE pg_default;

ALTER TABLE IF EXISTS category OWNER to postgres;

CREATE SEQUENCE IF NOT EXISTS todo_id_seq
    INCREMENT 1
    START 25
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;
ALTER SEQUENCE todo_id_seq OWNER TO postgres;

CREATE TABLE IF NOT EXISTS  todo
(
    id integer NOT NULL DEFAULT nextval('todo_id_seq'::regclass),
    todo_task   varchar(255)          not null,
    done        boolean default false not null,
    category_id integer constraint todo_category_id_fk  references category,
    duedate     date,
    priority    integer               not null
)TABLESPACE pg_default;

ALTER TABLE IF EXISTS todo OWNER to postgres;


