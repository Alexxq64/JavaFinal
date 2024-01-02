--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

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
-- Name: transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transactions (
    transaction_id integer NOT NULL,
    sender_id integer,
    receiver_id integer,
    operation_type integer,
    amount double precision,
    execution_date timestamp without time zone
);


ALTER TABLE public.transactions OWNER TO postgres;

--
-- Name: transactions_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transactions_transaction_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transactions_transaction_id_seq OWNER TO postgres;

--
-- Name: transactions_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transactions_transaction_id_seq OWNED BY public.transactions.transaction_id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    balance numeric(10,2) DEFAULT 0 NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: user_account_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_account_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_account_user_id_seq OWNER TO postgres;

--
-- Name: user_account_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_account_user_id_seq OWNED BY public.users.user_id;


--
-- Name: transactions transaction_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions ALTER COLUMN transaction_id SET DEFAULT nextval('public.transactions_transaction_id_seq'::regclass);


--
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN user_id SET DEFAULT nextval('public.user_account_user_id_seq'::regclass);


--
-- Data for Name: transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transactions (transaction_id, sender_id, receiver_id, operation_type, amount, execution_date) FROM stdin;
2	\N	1	1	100	2023-12-26 00:00:00
3	\N	1	1	100	2023-12-26 00:00:00
4	1	\N	2	50	2023-12-26 00:00:00
1	\N	1	1	100	2023-12-25 00:00:00
5	\N	1	1	100	2023-12-24 00:00:00
6	1	\N	2	50	2023-12-24 00:00:00
7	\N	1	1	100	2023-12-24 00:00:00
8	1	\N	2	50	2023-12-24 00:00:00
9	\N	1	1	100	2023-12-24 00:00:00
10	1	\N	2	50	2023-12-24 00:00:00
11	\N	1	1	100	2023-12-24 00:00:00
12	1	\N	2	50	2023-12-24 00:00:00
13	\N	1	1	100	2023-12-25 00:00:00
14	1	\N	2	50	2023-12-25 00:00:00
15	\N	1	1	100	2023-12-26 00:00:00
16	1	\N	2	50	2023-12-26 00:00:00
17	1	2	3	200	2023-12-26 00:00:00
18	1	2	3	200	2023-12-26 00:00:00
19	1	2	3	20	2023-12-26 00:00:00
20	1	2	3	20	2023-12-26 00:00:00
21	1	2	3	20	2023-12-26 00:00:00
22	1	2	3	15	2023-12-26 00:00:00
23	1	2	3	20	2023-12-26 00:00:00
24	1	\N	2	50	2023-12-26 00:00:00
25	1	2	3	20	2023-12-26 00:00:00
26	1	2	3	20	2023-12-26 00:00:00
28	\N	1	1	50	2023-12-26 00:00:00
29	\N	1	1	50	2023-12-26 00:00:00
30	\N	1	1	50	2023-12-26 00:00:00
31	\N	1	1	50	2023-12-26 00:00:00
32	\N	1	1	50	2023-12-26 00:00:00
33	1	2	3	20	2023-12-26 00:00:00
34	1	2	3	20	2023-12-26 00:00:00
35	1	2	3	20	2024-01-02 00:00:00
36	1	2	3	20	2024-01-02 00:00:00
37	1	2	3	20	2024-01-02 00:00:00
38	1	2	3	20	2024-01-02 00:00:00
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, balance) FROM stdin;
1	145.00
2	1655.00
3	800.00
4	2000.00
\.


--
-- Name: transactions_transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transactions_transaction_id_seq', 38, true);


--
-- Name: user_account_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_account_user_id_seq', 4, true);


--
-- Name: transactions transactions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_pkey PRIMARY KEY (transaction_id);


--
-- Name: users user_account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT user_account_pkey PRIMARY KEY (user_id);


--
-- Name: transactions transactions_receiver_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_receiver_user_id_fkey FOREIGN KEY (receiver_id) REFERENCES public.users(user_id);


--
-- Name: transactions transactions_sender_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transactions
    ADD CONSTRAINT transactions_sender_user_id_fkey FOREIGN KEY (sender_id) REFERENCES public.users(user_id);


--
-- PostgreSQL database dump complete
--

