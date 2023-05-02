--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2023-05-03 00:35:08 CEST

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE doubloons_db;
--
-- TOC entry 3631 (class 1262 OID 16479)
-- Name: doubloons_db; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE doubloons_db WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'C';


ALTER DATABASE doubloons_db OWNER TO postgres;

\connect doubloons_db

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 214 (class 1259 OID 16480)
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categories (
                                   category_id integer NOT NULL,
                                   category_name character varying(30) NOT NULL,
                                   category_description character varying(255) NOT NULL
);


ALTER TABLE public.categories OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16483)
-- Name: comments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.comments (
                                 month_and_year character varying(7) NOT NULL,
                                 given_to character varying(30) NOT NULL,
                                 given_by character varying(30) NOT NULL,
                                 comment character varying(1000) NOT NULL
);


ALTER TABLE public.comments OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16488)
-- Name: doubloons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.doubloons (
                                  doubloon_id bigint NOT NULL,
                                  category_id integer NOT NULL,
                                  given_to character varying(30) NOT NULL,
                                  given_by character varying(30) NOT NULL,
                                  amount integer NOT NULL,
                                  month_and_year character varying(7) NOT NULL
);


ALTER TABLE public.doubloons OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16647)
-- Name: doubloons_doubloon_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.doubloons ALTER COLUMN doubloon_id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.doubloons_doubloon_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
    );


--
-- TOC entry 217 (class 1259 OID 16491)
-- Name: team_leaders; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.team_leaders (
                                     leader_id character varying(30) NOT NULL,
                                     team_id character varying(255) NOT NULL
);


ALTER TABLE public.team_leaders OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16554)
-- Name: teams; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.teams (
                              team_id character varying(255) NOT NULL,
                              team_name character varying(50) NOT NULL,
                              team_description character varying(255)
);


ALTER TABLE public.teams OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16497)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
                              email character varying(30) NOT NULL,
                              team_id character varying(255),
                              first_name character varying(20) NOT NULL,
                              last_name character varying(20) NOT NULL,
                              password character varying(255) NOT NULL,
                              avatar character varying(255)
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 3619 (class 0 OID 16480)
-- Dependencies: 214
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.categories (category_id, category_name, category_description) VALUES (5, 'craftsmanship', 'to-update');
INSERT INTO public.categories (category_id, category_name, category_description) VALUES (4, 'learn fast', 'to-update');
INSERT INTO public.categories (category_id, category_name, category_description) VALUES (3, 'autonomy', 'to-update');
INSERT INTO public.categories (category_id, category_name, category_description) VALUES (2, 'trust', 'to-update');
INSERT INTO public.categories (category_id, category_name, category_description) VALUES (1, 'collaboration', 'to-update');


--
-- TOC entry 3620 (class 0 OID 16483)
-- Dependencies: 215
-- Data for Name: comments; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3621 (class 0 OID 16488)
-- Dependencies: 216
-- Data for Name: doubloons; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3622 (class 0 OID 16491)
-- Dependencies: 217
-- Data for Name: team_leaders; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3624 (class 0 OID 16554)
-- Dependencies: 219
-- Data for Name: teams; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.teams (team_id, team_name, team_description) VALUES ('1', 'Example Team', 'Example Team description');


--
-- TOC entry 3623 (class 0 OID 16497)
-- Dependencies: 218
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (email, team_id, first_name, last_name, password, avatar) VALUES ('admin@example.com', '1', 'Mike', 'Wazowski', 'secret', 'avatars.example/avatar1.jpg');
INSERT INTO public.users (email, team_id, first_name, last_name, password, avatar) VALUES ('user1@example.com', '1', 'James', 'Sullivan', 'secret', 'avatars.example/avatar2.jpg');
INSERT INTO public.users (email, team_id, first_name, last_name, password, avatar) VALUES ('user@example.com', '1', 'Randall', 'Boggs', 'secret', 'string');


--
-- TOC entry 3632 (class 0 OID 0)
-- Dependencies: 220
-- Name: doubloons_doubloon_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.doubloons_doubloon_id_seq', 15, true);


--
-- TOC entry 3460 (class 2606 OID 16503)
-- Name: categories categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT categories_pkey PRIMARY KEY (category_id);


--
-- TOC entry 3462 (class 2606 OID 16505)
-- Name: comments comments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (month_and_year, given_to, given_by);


--
-- TOC entry 3464 (class 2606 OID 16642)
-- Name: doubloons doubloons_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT doubloons_pkey PRIMARY KEY (doubloon_id);


--
-- TOC entry 3468 (class 2606 OID 16560)
-- Name: teams teams_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teams
    ADD CONSTRAINT teams_pkey PRIMARY KEY (team_id);


--
-- TOC entry 3466 (class 2606 OID 16513)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (email);


--
-- TOC entry 3469 (class 2606 OID 16514)
-- Name: comments comments_given_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_given_by_fkey FOREIGN KEY (given_by) REFERENCES public.users(email);


--
-- TOC entry 3470 (class 2606 OID 16519)
-- Name: comments comments_given_to_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_given_to_fkey FOREIGN KEY (given_to) REFERENCES public.users(email);


--
-- TOC entry 3471 (class 2606 OID 16524)
-- Name: doubloons doubloons_category_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT doubloons_category_fkey FOREIGN KEY (category_id) REFERENCES public.categories(category_id) NOT VALID;


--
-- TOC entry 3472 (class 2606 OID 16529)
-- Name: doubloons doubloons_given_by_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT doubloons_given_by_fkey FOREIGN KEY (given_by) REFERENCES public.users(email) NOT VALID;


--
-- TOC entry 3473 (class 2606 OID 16534)
-- Name: doubloons doubloons_given_to_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT doubloons_given_to_fkey FOREIGN KEY (given_to) REFERENCES public.users(email) NOT VALID;


--
-- TOC entry 3474 (class 2606 OID 16539)
-- Name: team_leaders team_leaders_leader_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team_leaders
    ADD CONSTRAINT team_leaders_leader_fkey FOREIGN KEY (leader_id) REFERENCES public.users(email);


--
-- TOC entry 3475 (class 2606 OID 16566)
-- Name: team_leaders team_leaders_team_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.team_leaders
    ADD CONSTRAINT team_leaders_team_fkey FOREIGN KEY (team_id) REFERENCES public.teams(team_id) NOT VALID;


--
-- TOC entry 3476 (class 2606 OID 16561)
-- Name: users users_team_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_team_fkey FOREIGN KEY (team_id) REFERENCES public.teams(team_id) NOT VALID;


-- Completed on 2023-05-03 00:35:09 CEST

--
-- PostgreSQL database dump complete
--

