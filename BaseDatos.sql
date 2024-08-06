
SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'customers' AND pid <> pg_backend_pid();

SELECT pg_terminate_backend(pid)
FROM pg_stat_activity
WHERE datname = 'accounts' AND pid <> pg_backend_pid();


drop database "customers";
drop database "accounts";
CREATE DATABASE "customers" owner "postgres" ;
CREATE DATABASE "accounts" owner "postgres" ;


CREATE TABLE public.customers (
	uuid uuid NOT NULL,
	created_at timestamp(6) NULL,
	deleted_at timestamp(6) NULL,
	is_deleted bool NOT NULL,
	updated_at timestamp(6) NULL,
	client_id varchar(255) NULL,
	"password" varchar(255) NULL,
	address varchar(255) NULL,
	age int4 NULL,
	gender varchar(255) NULL,
	identification varchar(255) NULL,
	"name" varchar(255) NULL,
	phone varchar(255) NULL,
	status bool NULL,
	CONSTRAINT customers_pkey PRIMARY KEY (uuid)
);

INSERT INTO public.customers (uuid,created_at,deleted_at,is_deleted,updated_at,client_id,"password",address,age,gender,identification,"name",phone,status) VALUES
	 ('11515772-e6b9-4356-bf99-5e3ff9934026','2024-08-06 16:04:02.413628',NULL,false,'2024-08-06 16:04:02.413628','0f4c515a-7b23-4197-bbb9-f9823a676729','1234','Otavalo sn y principal',30,'Hombre','0401590039','Gandhy Cuasapas','098254785',true);

CREATE TABLE public.accounts (
	uuid uuid NOT NULL,
	created_at timestamp(6) NULL,
	deleted_at timestamp(6) NULL,
	is_deleted bool NOT NULL,
	updated_at timestamp(6) NULL,
	actual_balance numeric(38, 2) NULL,
	customer_uuid uuid NULL,
	initial_balance numeric(38, 2) NULL,
	"number" varchar(255) NULL,
	status bool NULL,
	"type" varchar(255) NULL,
	CONSTRAINT accounts_pkey PRIMARY KEY (uuid)
);

-- Drop table

-- DROP TABLE public.transactions;

CREATE TABLE public.transactions (
	uuid uuid NOT NULL,
	created_at timestamp(6) NULL,
	deleted_at timestamp(6) NULL,
	is_deleted bool NOT NULL,
	updated_at timestamp(6) NULL,
	amount numeric(38, 2) NOT NULL,
	balance numeric(38, 2) NOT NULL,
	"date" timestamp(6) NOT NULL,
	transaction_type varchar(255) NOT NULL,
	account_id uuid NOT NULL,
	CONSTRAINT transactions_pkey PRIMARY KEY (uuid),
	CONSTRAINT "FK5m36vn1mqywoeuwoccmetovtt" FOREIGN KEY (account_id) REFERENCES public.accounts(uuid)
);


INSERT INTO public.accounts (uuid,created_at,deleted_at,is_deleted,updated_at,actual_balance,customer_uuid,initial_balance,"number",status,"type") VALUES
	 ('17c2d470-748f-4a04-af93-fabf089f88ee','2024-08-06 16:07:54.102945',NULL,false,'2024-08-06 16:08:01.782478',200.00,'11515772-e6b9-4356-bf99-5e3ff9934026',100.00,'1234567890',true,'AHORROS');
INSERT INTO public.transactions (uuid,created_at,deleted_at,is_deleted,updated_at,amount,balance,"date",transaction_type,account_id) VALUES
	 ('962d2dbd-0327-4e6c-9a8d-5c83df96c6db','2024-08-06 16:08:01.765473',NULL,false,'2024-08-06 16:08:01.765473',100.00,200.00,'2024-08-02 19:30:00','DEPOSITO','17c2d470-748f-4a04-af93-fabf089f88ee');
