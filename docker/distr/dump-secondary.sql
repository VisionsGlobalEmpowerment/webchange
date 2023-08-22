--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.8
-- Dumped by pg_dump version 9.6.8

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: activity_stats; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.activity_stats (
    id integer NOT NULL,
    user_id integer,
    course_id integer,
    activity_id character varying(32) NOT NULL,
    data json NOT NULL
);


ALTER TABLE public.activity_stats OWNER TO webchange;

--
-- Name: activity_stats_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.activity_stats_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.activity_stats_id_seq OWNER TO webchange;

--
-- Name: activity_stats_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.activity_stats_id_seq OWNED BY public.activity_stats.id;


--
-- Name: classes; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.classes (
    id integer NOT NULL,
    name character varying(30) NOT NULL,
    school_id integer
);


ALTER TABLE public.classes OWNER TO webchange;

--
-- Name: classes_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.classes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.classes_id_seq OWNER TO webchange;

--
-- Name: classes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.classes_id_seq OWNED BY public.classes.id;


--
-- Name: course_events; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.course_events (
    id integer NOT NULL,
    user_id integer,
    course_id integer,
    created_at timestamp with time zone NOT NULL,
    type character varying(30) NOT NULL,
    data json NOT NULL
);


ALTER TABLE public.course_events OWNER TO webchange;

--
-- Name: course_actions_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.course_actions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.course_actions_id_seq OWNER TO webchange;

--
-- Name: course_actions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.course_actions_id_seq OWNED BY public.course_events.id;


--
-- Name: course_progresses; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.course_progresses (
    id integer NOT NULL,
    user_id integer,
    course_id integer,
    data json NOT NULL
);


ALTER TABLE public.course_progresses OWNER TO webchange;

--
-- Name: course_progresses_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.course_progresses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.course_progresses_id_seq OWNER TO webchange;

--
-- Name: course_progresses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.course_progresses_id_seq OWNED BY public.course_progresses.id;


--
-- Name: course_stats; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.course_stats (
    id integer NOT NULL,
    user_id integer,
    class_id integer,
    course_id integer,
    data json NOT NULL
);


ALTER TABLE public.course_stats OWNER TO webchange;

--
-- Name: course_stats_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.course_stats_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.course_stats_id_seq OWNER TO webchange;

--
-- Name: course_stats_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.course_stats_id_seq OWNED BY public.course_stats.id;


--
-- Name: course_versions; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.course_versions (
    id integer NOT NULL,
    course_id integer,
    data json NOT NULL,
    owner_id integer NOT NULL,
    created_at timestamp with time zone NOT NULL
);


ALTER TABLE public.course_versions OWNER TO webchange;

--
-- Name: course_versions_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.course_versions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.course_versions_id_seq OWNER TO webchange;

--
-- Name: course_versions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.course_versions_id_seq OWNED BY public.course_versions.id;


--
-- Name: courses; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.courses (
    id integer NOT NULL,
    name character varying(30) NOT NULL,
    slug character varying(30),
    lang character varying(30),
    image_src character varying(1024)
);


ALTER TABLE public.courses OWNER TO webchange;

--
-- Name: courses_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.courses_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.courses_id_seq OWNER TO webchange;

--
-- Name: courses_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.courses_id_seq OWNED BY public.courses.id;


--
-- Name: dataset_items; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.dataset_items (
    id integer NOT NULL,
    name character varying(30) NOT NULL,
    dataset_id integer,
    data json NOT NULL
);


ALTER TABLE public.dataset_items OWNER TO webchange;

--
-- Name: dataset_items_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.dataset_items_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.dataset_items_id_seq OWNER TO webchange;

--
-- Name: dataset_items_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.dataset_items_id_seq OWNED BY public.dataset_items.id;


--
-- Name: datasets; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.datasets (
    id integer NOT NULL,
    course_id integer,
    name character varying(30) NOT NULL,
    scheme json NOT NULL
);


ALTER TABLE public.datasets OWNER TO webchange;

--
-- Name: datasets_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.datasets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.datasets_id_seq OWNER TO webchange;

--
-- Name: datasets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.datasets_id_seq OWNED BY public.datasets.id;


--
-- Name: lesson_sets; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.lesson_sets (
    id integer NOT NULL,
    name character varying(30) NOT NULL,
    dataset_id integer,
    data json NOT NULL
);


ALTER TABLE public.lesson_sets OWNER TO webchange;

--
-- Name: lesson_sets_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.lesson_sets_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.lesson_sets_id_seq OWNER TO webchange;

--
-- Name: lesson_sets_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.lesson_sets_id_seq OWNED BY public.lesson_sets.id;


--
-- Name: scene_versions; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.scene_versions (
    id integer NOT NULL,
    scene_id integer,
    data json NOT NULL,
    owner_id integer NOT NULL,
    created_at timestamp with time zone NOT NULL
);


ALTER TABLE public.scene_versions OWNER TO webchange;

--
-- Name: scene_versions_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.scene_versions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.scene_versions_id_seq OWNER TO webchange;

--
-- Name: scene_versions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.scene_versions_id_seq OWNED BY public.scene_versions.id;


--
-- Name: scenes; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.scenes (
    id integer NOT NULL,
    course_id integer,
    name character varying(30) NOT NULL
);


ALTER TABLE public.scenes OWNER TO webchange;

--
-- Name: scenes_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.scenes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.scenes_id_seq OWNER TO webchange;

--
-- Name: scenes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.scenes_id_seq OWNED BY public.scenes.id;


--
-- Name: schema_migrations; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.schema_migrations (
    id bigint NOT NULL,
    applied timestamp without time zone,
    description character varying(1024)
);


ALTER TABLE public.schema_migrations OWNER TO webchange;

--
-- Name: schools; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.schools (
    id integer NOT NULL,
    name character varying(30) NOT NULL
);


ALTER TABLE public.schools OWNER TO webchange;

--
-- Name: schools_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.schools_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.schools_id_seq OWNER TO webchange;

--
-- Name: schools_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.schools_id_seq OWNED BY public.schools.id;


--
-- Name: students; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.students (
    id integer NOT NULL,
    user_id integer,
    class_id integer,
    school_id integer,
    access_code character varying(30),
    gender integer,
    date_of_birth date
);


ALTER TABLE public.students OWNER TO webchange;

--
-- Name: students_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.students_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.students_id_seq OWNER TO webchange;

--
-- Name: students_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.students_id_seq OWNED BY public.students.id;


--
-- Name: teachers; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.teachers (
    id integer NOT NULL,
    user_id integer,
    school_id integer
);


ALTER TABLE public.teachers OWNER TO webchange;

--
-- Name: teachers_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.teachers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.teachers_id_seq OWNER TO webchange;

--
-- Name: teachers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.teachers_id_seq OWNED BY public.teachers.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: webchange
--

CREATE TABLE public.users (
    id integer NOT NULL,
    first_name character varying(30),
    last_name character varying(30),
    email character varying(30),
    password character varying(300),
    active boolean DEFAULT false NOT NULL,
    created_at timestamp with time zone NOT NULL,
    last_login timestamp with time zone NOT NULL
);


ALTER TABLE public.users OWNER TO webchange;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: webchange
--

CREATE SEQUENCE public.users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO webchange;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: webchange
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: activity_stats id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.activity_stats ALTER COLUMN id SET DEFAULT nextval('public.activity_stats_id_seq'::regclass);


--
-- Name: classes id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.classes ALTER COLUMN id SET DEFAULT nextval('public.classes_id_seq'::regclass);


--
-- Name: course_events id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_events ALTER COLUMN id SET DEFAULT nextval('public.course_actions_id_seq'::regclass);


--
-- Name: course_progresses id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_progresses ALTER COLUMN id SET DEFAULT nextval('public.course_progresses_id_seq'::regclass);


--
-- Name: course_stats id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_stats ALTER COLUMN id SET DEFAULT nextval('public.course_stats_id_seq'::regclass);


--
-- Name: course_versions id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_versions ALTER COLUMN id SET DEFAULT nextval('public.course_versions_id_seq'::regclass);


--
-- Name: courses id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.courses ALTER COLUMN id SET DEFAULT nextval('public.courses_id_seq'::regclass);


--
-- Name: dataset_items id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.dataset_items ALTER COLUMN id SET DEFAULT nextval('public.dataset_items_id_seq'::regclass);


--
-- Name: datasets id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.datasets ALTER COLUMN id SET DEFAULT nextval('public.datasets_id_seq'::regclass);


--
-- Name: lesson_sets id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.lesson_sets ALTER COLUMN id SET DEFAULT nextval('public.lesson_sets_id_seq'::regclass);


--
-- Name: scene_versions id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.scene_versions ALTER COLUMN id SET DEFAULT nextval('public.scene_versions_id_seq'::regclass);


--
-- Name: scenes id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.scenes ALTER COLUMN id SET DEFAULT nextval('public.scenes_id_seq'::regclass);


--
-- Name: schools id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.schools ALTER COLUMN id SET DEFAULT nextval('public.schools_id_seq'::regclass);


--
-- Name: students id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.students ALTER COLUMN id SET DEFAULT nextval('public.students_id_seq'::regclass);


--
-- Name: teachers id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.teachers ALTER COLUMN id SET DEFAULT nextval('public.teachers_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Data for Name: activity_stats; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.activity_stats (id, user_id, course_id, activity_id, data) FROM stdin;
\.


--
-- Name: activity_stats_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.activity_stats_id_seq', 1, false);


--
-- Data for Name: classes; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.classes (id, name, school_id) FROM stdin;
\.


--
-- Name: classes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.classes_id_seq', 1, true);


--
-- Name: course_actions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.course_actions_id_seq', 1, false);


--
-- Data for Name: course_events; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.course_events (id, user_id, course_id, created_at, type, data) FROM stdin;
\.


--
-- Data for Name: course_progresses; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.course_progresses (id, user_id, course_id, data) FROM stdin;
\.


--
-- Name: course_progresses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.course_progresses_id_seq', 1, true);


--
-- Data for Name: course_stats; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.course_stats (id, user_id, class_id, course_id, data) FROM stdin;
\.


--
-- Name: course_stats_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.course_stats_id_seq', 1, false);


--
-- Data for Name: course_versions; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.course_versions (id, course_id, data, owner_id, created_at) FROM stdin;
\.


--
-- Name: course_versions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.course_versions_id_seq', 9, true);


--
-- Data for Name: courses; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.courses (id, name, slug, lang, image_src) FROM stdin;
\.


--
-- Name: courses_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.courses_id_seq', 4, true);


--
-- Data for Name: dataset_items; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.dataset_items (id, name, dataset_id, data) FROM stdin;
\.


--
-- Name: dataset_items_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.dataset_items_id_seq', 36, true);


--
-- Data for Name: datasets; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.datasets (id, course_id, name, scheme) FROM stdin;
\.


--
-- Name: datasets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.datasets_id_seq', 3, true);


--
-- Data for Name: lesson_sets; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.lesson_sets (id, name, dataset_id, data) FROM stdin;
\.


--
-- Name: lesson_sets_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.lesson_sets_id_seq', 5, true);


--
-- Data for Name: scene_versions; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.scene_versions (id, scene_id, data, owner_id, created_at) FROM stdin;
\.


--
-- Name: scene_versions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.scene_versions_id_seq', 17, true);


--
-- Data for Name: scenes; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.scenes (id, course_id, name) FROM stdin;
\.


--
-- Name: scenes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.scenes_id_seq', 8, true);


--
-- Data for Name: schema_migrations; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.schema_migrations (id, applied, description) FROM stdin;
\.


--
-- Data for Name: schools; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.schools (id, name) FROM stdin;
\.


--
-- Name: schools_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.schools_id_seq', 2, false);


--
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.students (id, user_id, class_id, school_id, access_code, gender, date_of_birth) FROM stdin;
\.


--
-- Name: students_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.students_id_seq', 1, true);


--
-- Data for Name: teachers; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.teachers (id, user_id, school_id) FROM stdin;
\.


--
-- Name: teachers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.teachers_id_seq', 1, true);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: webchange
--

COPY public.users (id, first_name, last_name, email, password, active, created_at, last_login) FROM stdin;
\.


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: webchange
--

SELECT pg_catalog.setval('public.users_id_seq', 2, true);


--
-- Name: activity_stats activity_stats_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.activity_stats
    ADD CONSTRAINT activity_stats_pkey PRIMARY KEY (id);


--
-- Name: classes classes_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.classes
    ADD CONSTRAINT classes_pkey PRIMARY KEY (id);


--
-- Name: course_events course_actions_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_events
    ADD CONSTRAINT course_actions_pkey PRIMARY KEY (id);


--
-- Name: course_progresses course_progresses_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_progresses
    ADD CONSTRAINT course_progresses_pkey PRIMARY KEY (id);


--
-- Name: course_stats course_stats_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_stats
    ADD CONSTRAINT course_stats_pkey PRIMARY KEY (id);


--
-- Name: course_versions course_versions_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_versions
    ADD CONSTRAINT course_versions_pkey PRIMARY KEY (id);


--
-- Name: courses courses_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.courses
    ADD CONSTRAINT courses_pkey PRIMARY KEY (id);


--
-- Name: dataset_items dataset_items_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.dataset_items
    ADD CONSTRAINT dataset_items_pkey PRIMARY KEY (id);


--
-- Name: datasets datasets_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.datasets
    ADD CONSTRAINT datasets_pkey PRIMARY KEY (id);


--
-- Name: lesson_sets lesson_sets_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.lesson_sets
    ADD CONSTRAINT lesson_sets_pkey PRIMARY KEY (id);


--
-- Name: scene_versions scene_versions_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.scene_versions
    ADD CONSTRAINT scene_versions_pkey PRIMARY KEY (id);


--
-- Name: scenes scenes_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.scenes
    ADD CONSTRAINT scenes_pkey PRIMARY KEY (id);


--
-- Name: schema_migrations schema_migrations_id_key; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.schema_migrations
    ADD CONSTRAINT schema_migrations_id_key UNIQUE (id);


--
-- Name: students school_access_code_key; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT school_access_code_key UNIQUE (school_id, access_code);


--
-- Name: schools schools_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.schools
    ADD CONSTRAINT schools_pkey PRIMARY KEY (id);


--
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (id);


--
-- Name: teachers teachers_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT teachers_pkey PRIMARY KEY (id);


--
-- Name: users users_email_unique; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_email_unique UNIQUE (email);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: dataset_items_name; Type: INDEX; Schema: public; Owner: webchange
--

CREATE UNIQUE INDEX dataset_items_name ON public.dataset_items USING btree (dataset_id, name);


--
-- Name: datasets_name; Type: INDEX; Schema: public; Owner: webchange
--

CREATE UNIQUE INDEX datasets_name ON public.datasets USING btree (course_id, name);


--
-- Name: activity_stats activity_stats_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.activity_stats
    ADD CONSTRAINT activity_stats_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: activity_stats activity_stats_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.activity_stats
    ADD CONSTRAINT activity_stats_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: classes classes_school_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.classes
    ADD CONSTRAINT classes_school_id_fkey FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: course_events course_actions_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_events
    ADD CONSTRAINT course_actions_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: course_events course_actions_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_events
    ADD CONSTRAINT course_actions_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: course_progresses course_progresses_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_progresses
    ADD CONSTRAINT course_progresses_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: course_progresses course_progresses_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_progresses
    ADD CONSTRAINT course_progresses_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: course_stats course_stats_class_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_stats
    ADD CONSTRAINT course_stats_class_id_fkey FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: course_stats course_stats_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_stats
    ADD CONSTRAINT course_stats_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: course_stats course_stats_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_stats
    ADD CONSTRAINT course_stats_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: course_versions course_versions_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.course_versions
    ADD CONSTRAINT course_versions_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: dataset_items dataset_items_dataset_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.dataset_items
    ADD CONSTRAINT dataset_items_dataset_id_fkey FOREIGN KEY (dataset_id) REFERENCES public.datasets(id);


--
-- Name: datasets datasets_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.datasets
    ADD CONSTRAINT datasets_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: lesson_sets lesson_sets_dataset_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.lesson_sets
    ADD CONSTRAINT lesson_sets_dataset_id_fkey FOREIGN KEY (dataset_id) REFERENCES public.datasets(id);


--
-- Name: scene_versions scene_versions_scene_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.scene_versions
    ADD CONSTRAINT scene_versions_scene_id_fkey FOREIGN KEY (scene_id) REFERENCES public.scenes(id);


--
-- Name: scenes scenes_course_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.scenes
    ADD CONSTRAINT scenes_course_id_fkey FOREIGN KEY (course_id) REFERENCES public.courses(id);


--
-- Name: students students_class_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_class_id_fkey FOREIGN KEY (class_id) REFERENCES public.classes(id);


--
-- Name: students students_school_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_school_id_fkey FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: students students_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: teachers teachers_school_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT teachers_school_id_fkey FOREIGN KEY (school_id) REFERENCES public.schools(id);


--
-- Name: teachers teachers_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: webchange
--

ALTER TABLE ONLY public.teachers
    ADD CONSTRAINT teachers_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--

