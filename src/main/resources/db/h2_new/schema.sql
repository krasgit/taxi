CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE SCHEMA "todo";

CREATE TABLE "users" (
  "id" integer PRIMARY KEY,
  "username" varchar(30),
  "password" varchar(30),
  "mobile" varchar(30),
  "email" varchar(30),
  "token" varchar(30),
  "tokenDate" date,
  "rating" int,
  "created_at" timestamp
);

CREATE TABLE "contractors" (
  "id" integer PRIMARY KEY,
  "username" varchar(30),
  "password" varchar(30),
  "mobile" varchar(30),
  "email" varchar(30),
  "token" varchar(30),
  "tokenDate" date,
  "rating" int,
  "created_at" timestamp
);

CREATE TABLE "orders" (
  "id" integer PRIMARY KEY,
  "parrentId" integer,
  "userId" id,
  "routesId" id,
  "stateId" id,
  "contractorId" int,
  "numberPassager" int,
  "numberBags" int,
  "note" varchar(100),
  "role" varchar(30),
  "startOn" datetime,
  "created_at" timestamp
);

CREATE TABLE "ordersState" (
  "id" integer PRIMARY KEY,
  "name" varchar(20)
);

CREATE TABLE "geolocations" (
  "id" integer PRIMARY KEY,
  "username" varchar(30),
  "latitude" number,
  "longitude" number,
  "reliability" int,
  "created_at" timestamp
);

CREATE TABLE "routes" (
  "id" integer PRIMARY KEY,
  "routesId" integer,
  "geolocationId" int,
  "order" int,
  "created_at" timestamp
);

CREATE TABLE "todo"."roles" (
  "principalid" varchar,
  "role" varchar(30),
  "rolegroup" varchar(30)
);

ALTER TABLE "orders" ADD FOREIGN KEY ("parrentId") REFERENCES "orders" ("id");

ALTER TABLE "routes" ADD FOREIGN KEY ("geolocationId") REFERENCES "geolocations" ("id");

ALTER TABLE "orders" ADD FOREIGN KEY ("routesId") REFERENCES "routes" ("routesId");

ALTER TABLE "orders" ADD FOREIGN KEY ("stateId") REFERENCES "ordersState" ("id");

ALTER TABLE "orders" ADD FOREIGN KEY ("userId") REFERENCES "users" ("id");

ALTER TABLE "orders" ADD FOREIGN KEY ("contractorId") REFERENCES "contractors" ("id");