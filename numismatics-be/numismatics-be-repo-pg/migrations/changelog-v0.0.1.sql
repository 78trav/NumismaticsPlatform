--liquibase formatted sql

--changeset trav78:1 labels:v0.0.1
CREATE TYPE "np_condition_type" AS ENUM
(
    'UNDEFINED',
    'PF',
    'PL',
    'BU',
    'UNC',
    'AU_PLUS',
    'AU',
    'XF_PLUS',
    'XF',
    'VF_PLUS',
    'VF',
    'F',
    'VG',
    'G',
    'AG',
    'FA',
    'PR'
);

CREATE TABLE IF NOT EXISTS "lots" (
	"id" BIGSERIAL primary key,
    "name" varchar(100) not null,
    "description" text constraint np_description_length_constraint check (length(description) < 4096),
    "lock" varchar(36) not null,
    "owner_id" varchar(36) not null,
    "section_id" bigint not null default 0,
    "is_coin" boolean not null default true,
    "year" smallint not null default 0,
    "country_id" bigint not null default 0,
    "catalogue_number" varchar(15) not null,
    "denomination" varchar(30) not null,
    "material_id" bigint not null default 0,
    "weight" numeric(6,2) not null default 0,
    "condition" np_condition_type not null default 'UNDEFINED'::np_condition_type,
    "serial_number" varchar(15) not null,
    "quantity" smallint not null default 1
);

CREATE INDEX lots_owner_id_idx on "lots" ("owner_id");

CREATE INDEX lots_section_id_idx on "lots" ("section_id");

CREATE INDEX lots_country_id_idx on "lots" ("country_id");

CREATE INDEX lots_material_id_idx on "lots" ("material_id");

CREATE INDEX lots_condition_idx on "lots" ("condition");
