--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2023-03-26 19:30:55 CEST

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

--
-- TOC entry 3613 (class 1262 OID 16398)
-- Name: Doubloons; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE "Doubloons" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'C';


ALTER DATABASE "Doubloons" OWNER TO postgres;

\connect "Doubloons"

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
-- TOC entry 214 (class 1259 OID 16399)
-- Name: categories; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.categories (
    "categoryId" integer NOT NULL,
    "categoryName" character varying(30) NOT NULL,
    "categoryDescription" character varying(255) NOT NULL
);


ALTER TABLE public.categories OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 16402)
-- Name: doubloons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.doubloons (
    "doubloonId" integer NOT NULL,
    "categoryId" integer NOT NULL,
    "givenTo" character varying(30) NOT NULL,
    "givenBy" character varying(30) NOT NULL,
    amount integer NOT NULL,
    comment character varying(255),
    "timestamp" timestamp with time zone NOT NULL
);


ALTER TABLE public.doubloons OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16405)
-- Name: teams; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.teams (
    "teamId" integer NOT NULL,
    "teamName" character varying(30) NOT NULL,
    "teamDescription" character varying(255) NOT NULL
);


ALTER TABLE public.teams OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 16408)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    email character varying(30) NOT NULL,
    "teamId" integer,
    "firstName" character varying(20) NOT NULL,
    "lastName" character varying(20) NOT NULL,
    password character varying(255) NOT NULL,
    avatar character varying(255),
    "leadingTeam" boolean
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 3604 (class 0 OID 16399)
-- Dependencies: 214
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3605 (class 0 OID 16402)
-- Dependencies: 215
-- Data for Name: doubloons; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 3606 (class 0 OID 16405)
-- Dependencies: 216
-- Data for Name: teams; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.teams ("teamId", "teamName", "teamDescription") VALUES (1, 'example team', 'creating the best examples in the world');


--
-- TOC entry 3607 (class 0 OID 16408)
-- Dependencies: 217
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.users (email, "teamId", "firstName", "lastName", password, avatar, "leadingTeam") VALUES ('admin@example.com', 1, 'Mike', 'Wazowski', 'Secret', 'https://userAvatars.com/avatar1', true);
INSERT INTO public.users (email, "teamId", "firstName", "lastName", password, avatar, "leadingTeam") VALUES ('user@example.com', 1, 'James', 'Sullivan', 'Secret', 'https://userAvatars.com/avatar1', false);


--
-- TOC entry 3451 (class 2606 OID 16414)
-- Name: categories Categories_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.categories
    ADD CONSTRAINT "Categories_pkey" PRIMARY KEY ("categoryId");


--
-- TOC entry 3453 (class 2606 OID 16416)
-- Name: doubloons Doubloons_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT "Doubloons_pkey" PRIMARY KEY ("doubloonId");


--
-- TOC entry 3455 (class 2606 OID 16418)
-- Name: teams Teams_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teams
    ADD CONSTRAINT "Teams_pkey" PRIMARY KEY ("teamId");


--
-- TOC entry 3457 (class 2606 OID 16420)
-- Name: users Users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT "Users_pkey" PRIMARY KEY (email);


--
-- TOC entry 3458 (class 2606 OID 16421)
-- Name: doubloons Doubloons_CategoryID_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT "Doubloons_CategoryID_fkey" FOREIGN KEY ("categoryId") REFERENCES public.categories("categoryId") NOT VALID;


--
-- TOC entry 3459 (class 2606 OID 16426)
-- Name: doubloons Doubloons_GivenBy_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT "Doubloons_GivenBy_fkey" FOREIGN KEY ("givenBy") REFERENCES public.users(email) NOT VALID;


--
-- TOC entry 3460 (class 2606 OID 16431)
-- Name: doubloons Doubloons_UserID_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.doubloons
    ADD CONSTRAINT "Doubloons_UserID_fkey" FOREIGN KEY ("givenTo") REFERENCES public.users(email) NOT VALID;


--
-- TOC entry 3461 (class 2606 OID 16441)
-- Name: users Users_TeamID_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT "Users_TeamID_fkey" FOREIGN KEY ("teamId") REFERENCES public.teams("teamId") NOT VALID;


-- Completed on 2023-03-26 19:30:55 CEST

--
-- PostgreSQL database dump complete
--

