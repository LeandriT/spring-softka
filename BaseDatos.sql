--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2
-- Dumped by pg_dump version 14.2

-- Started on 2024-08-05 01:27:38

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
-- TOC entry 3318 (class 1262 OID 26437)
-- Name: accounts; Type: DATABASE; Schema: -; Owner: account

CREATE USER "account" WITH PASSWORD 'bxshaq06gi4hgdss'; 
--
CREATE DATABASE accounts WITH TEMPLATE = template0 ENCODING = 'UTF8';

ALTER DATABASE accounts OWNER TO account;

\connect accounts

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
-- TOC entry 3 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 3319 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 26446)
-- Name: accounts; Type: TABLE; Schema: public; Owner: account
--

CREATE TABLE public.accounts (
    uuid uuid NOT NULL,
    created_at timestamp(6) without time zone,
    deleted_at timestamp(6) without time zone,
    is_deleted boolean NOT NULL,
    updated_at timestamp(6) without time zone,
    actual_balance numeric(38,2),
    customer_uuid uuid,
    initial_balance numeric(38,2),
    number character varying(255),
    status boolean,
    type character varying(255)
);


ALTER TABLE public.accounts OWNER TO account;

--
-- TOC entry 210 (class 1259 OID 26453)
-- Name: transactions; Type: TABLE; Schema: public; Owner: account
--

CREATE TABLE public.transactions (
    uuid uuid NOT NULL,
    created_at timestamp(6) without time zone,
    deleted_at timestamp(6) without time zone,
    is_deleted boolean NOT NULL,
    updated_at timestamp(6) without time zone,
    amount numeric(38,2) NOT NULL,
    balance numeric(38,2) NOT NULL,
    date timestamp(6) without time zone NOT NULL,
    transaction_type character varying(255) NOT NULL,
    account_id uuid NOT NULL
);


ALTER TABLE public.transactions OWNER TO account;

--
-- TOC entry 3311 (class 0 OID 26446)
-- Dependencies: 209
-- Data for Name: accounts; Type: TABLE DATA; Schema: public; Owner: account
--



--
-- TOC entry 3312 (class 0 OID 26453)
-- Dependencies: 210
-- Data for Name: transactions; Type: TABLE DATA; Schema: public; Owner: account
--



--
-- TOC entry 3168 (class 2606 OID 26452)
-- Name: accounts accounts_pkey; Type: CONSTRAINT; Schema: public; Owner: account
--

ALTER TABLE ONLY public.accounts
    ADD CONSTRAINT accounts_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3170 (class 2606 OID 26457)
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: account
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (uuid);


--
-- TOC entry 3171 (class 2606 OID 26458)
-- Name: transactions FK5m36vn1mqywoeuwoccmetovtt; Type: FK CONSTRAINT; Schema: public; Owner: account
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT "FK5m36vn1mqywoeuwoccmetovtt" FOREIGN KEY (account_id) REFERENCES public.accounts(uuid);


-- Completed on 2024-08-05 01:27:38

--
-- PostgreSQL database dump complete
--





--------------------------

--
-- PostgreSQL database dump
--

-- Dumped from database version 14.2
-- Dumped by pg_dump version 14.2

-- Started on 2024-08-05 01:31:15

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
-- TOC entry 3 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE USER "customer" WITH PASSWORD 'bxshaq06gi4hgdss'; 
--
CREATE DATABASE customers WITH TEMPLATE = template0 ENCODING = 'UTF8';

ALTER DATABASE customers OWNER TO customer;

\connect customer
--
-- TOC entry 3310 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 209 (class 1259 OID 26439)
-- Name: customers; Type: TABLE; Schema: public; Owner: customer
--

CREATE TABLE public.customers (
    uuid uuid NOT NULL,
    created_at timestamp(6) without time zone,
    deleted_at timestamp(6) without time zone,
    is_deleted boolean NOT NULL,
    updated_at timestamp(6) without time zone,
    client_id character varying(255),
    password character varying(255),
    address character varying(255),
    age integer,
    gender character varying(255),
    identification character varying(255),
    name character varying(255),
    phone character varying(255),
    status boolean
);


ALTER TABLE public.customers OWNER TO customer;

--
-- TOC entry 3304 (class 0 OID 26439)
-- Dependencies: 209
-- Data for Name: customers; Type: TABLE DATA; Schema: public; Owner: customer
--



--
-- TOC entry 3164 (class 2606 OID 26445)
-- Name: customers customers_pkey; Type: CONSTRAINT; Schema: public; Owner: customer
--

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (uuid);


-- Completed on 2024-08-05 01:31:15

--
-- PostgreSQL database dump complete
--

