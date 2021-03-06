# --- !Ups

create table if not exists "people" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "name" varchar not null,
  "age" int not null
);

create table if not exists "typy_dokumentu" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "name" VARCHAR NOT NULL);

create table if not exists "dokumenty_identyfikacyjne" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "nr_dokumentu" VARCHAR NOT NULL,
  "typ_dokumentu" BIGINT NOT NULL,
  foreign key (typ_dokumentu) references typy_dokumentu(id));

create table if not exists "dane_osobowe" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "imie" VARCHAR NOT NULL,
  "drugie_imie" VARCHAR,
  "nazwisko" VARCHAR NOT NULL,
  "data_urodzenia" DATE NOT NULL,
  "miejsce_urodzenia" VARCHAR NOT NULL,
  "pesel" VARCHAR NOT NULL,
  "dokument_identyfikacyjny" BIGINT NOT NULL,
  foreign key (dokument_identyfikacyjny) references dokumenty_identyfikacyjne(id));

create table if not exists "pracownicy" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "login" VARCHAR NOT NULL,
  "haslo" VARCHAR NOT NULL,
  "czy_kierownik" BOOLEAN NOT NULL);

create table if not exists "decyzje" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "data_decyzji" DATE NOT NULL,
  "uzasadnienie" VARCHAR NOT NULL,
  "wydajacy" BIGINT NOT NULL,
  "rodzaj_decyzji" VARCHAR NOT NULL,
  foreign key (wydajacy) references pracownicy(id));

create table if not exists "placowki" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "nazwa" VARCHAR NOT NULL,
  "maks_spraw_zagregowanych" INTEGER NOT NULL);

create table if not exists "przesylki" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "data_przeslania" TIMESTAMP NOT NULL,
  "placowka" BIGINT NOT NULL,
  foreign key (placowka) references placowki(id));

create table if not exists "sprawy" (
  "id" bigint generated by default as identity(start with 1) not null primary key,
  "zdjecie" VARCHAR NOT NULL,
  "tresc_sprawy" VARCHAR NOT NULL,
  "identyfikator" VARCHAR NOT NULL,
  "data_utworzenia" TIMESTAMP NOT NULL,
  "czy_wyslana" BOOLEAN NOT NULL,
  "dane_osobowe" BIGINT NOT NULL,
  "aktualna_decyzja" BIGINT,
  "czy_zakonczona" BOOLEAN NOT NULL,
  "przesylka" BIGINT,
  foreign key (dane_osobowe) references dane_osobowe(id),
  foreign key (aktualna_decyzja) references decyzje(id),
  foreign key (przesylka) references przesylki(id));

insert into typy_dokumentu (name) values ('Dowód osobisty');
insert into typy_dokumentu (name) values ('Prawo jazdy');
insert into typy_dokumentu (name) values ('Paszport');

insert into pracownicy (login, haslo, czy_kierownik) values ('admin', 'admin', 'true');
insert into pracownicy (login, haslo, czy_kierownik) values ('pracownik', '123', 'false');

# --- !Downs

drop table "people" if exists;
drop table "typy_dokumentu" if exists;
drop table "dokumenty_identyfikacyjne" if exists;
drop table "dane_osobowe" if exists;
drop table "pracownicy" if exists;
drop table "decyzje" if exists;
drop table "placowki" if exists;
drop table "przesylki" if exists;
drop table "sprawy" if exists;
