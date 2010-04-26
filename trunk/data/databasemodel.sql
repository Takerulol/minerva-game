CREATE TABLE world (
	"id" INT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"token" VARCHAR(10) UNIQUE NOT NULL,
	"name" VARCHAR(64) UNIQUE NOT NULL,
	"description" VARCHAR(1024) NOT NULL,
	"author" VARCHAR(256) NOT NULL,
	"version" VARCHAR(10) NOT NULL,
	PRIMARY KEY ("id")
)

CREATE TABLE country (
	"id" INT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"token" VARCHAR(5) UNIQUE NOT NULL,
	"name" VARCHAR(25) UNIQUE NOT NULL,
	"color" VARCHAR(8) NOT NULL,
	"world" INT NOT NULL,
	"continent" INT NOT NULL
)

CREATE TABLE continent (
	"id" INT NOT NULL GENERATED BY DEFAULT AS IDENTITY, 
	"name" VARCHAR(25) UNIQUE NOT NULL
)

CREATE TABLE neighbour (
	"id" INT NOT NULL GENERATED BY DEFAULT AS IDENTITY, 
	"country" INT NOT NULL, 
	"neighbour_country" INT NOT NULL
)

CREATE TABLE player (
	"id" INT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"username" VARCHAR(25) UNIQUE NOT NULL,
	"password" VARCHAR(25) NOT NULL,
	"last_name" VARCHAR(25) NOT NULL,
	"first_name" VARCHAR(25) NOT NULL,
	"email" CHAR(50) UNIQUE NOT NULL,
	"last_login" TIMESTAMP NOT NULL
);

// Testdaten

-- ## Anlegen einer Welt
insert into world ("token", "name", "description", "author", "version") values ('earth', 'Die Erde', 'Die Erde als Risiko-Map', 'André König', '1.0 beta');

-- ## Anlegen der Kontinente
insert into continent ("name") values ('Europa');

-- ## Anlegen der Länder
insert into country ("token", "name", "color", "world", "continent") values ('sk', 'Skandinavien', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('ua', 'Ukraine', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('me', 'Mittel-Europa', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('se', 'Süd-Europa', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('we', 'West-Europa', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('is', 'Island', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('uk', 'Groß-Britanien', 'ffffff', 1, 1);

-- ## Festlegung der Länderbeziehungen

-- Skandinavien: Ukraine
insert into neighbour ("country", "neighbour_country") values (1, 2);

-- Ukraine: Skandinavien, Mittel-Europa
insert into neighbour ("country", "neighbour_country") values (2, 1);
insert into neighbour ("country", "neighbour_country") values (2, 3);

-- Mittel-Europa: Ukraine, Süd-Europa, West-Europa
insert into neighbour ("country", "neighbour_country") values (3, 2);
insert into neighbour ("country", "neighbour_country") values (3, 4);
insert into neighbour ("country", "neighbour_country") values (3, 5);

-- Süd-Europa: Mittel-Europa
insert into neighbour ("country", "neighbour_country") values (4, 3); 

-- West-Europa: Mittel-Europa, Island, Groß-Britanien
insert into neighbour ("country", "neighbour_country") values (5, 3);
insert into neighbour ("country", "neighbour_country") values (5, 6);
insert into neighbour ("country", "neighbour_country") values (5, 7); 

-- Island: West-Europa, Groß-Britanien
insert into neighbour ("country", "neighbour_country") values (6, 5);
insert into neighbour ("country", "neighbour_country") values (6, 7);

-- Groß-Britanien: Island
insert into neighbour ("country", "neighbour_country") values (7, 6);

-- ## Anlegen von Spielern
insert into player ("username", "password", "last_name", "first_name", "email", "last_login") values ('notion', '5ea790e4a248dab6ac4b6ae1854463c7', 'König', 'André', 'andre.koenig@gm.com', 20100425);