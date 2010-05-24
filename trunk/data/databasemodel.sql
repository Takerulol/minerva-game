CREATE TABLE world (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	"token" VARCHAR(10) UNIQUE NOT NULL,
	"name" VARCHAR(64) UNIQUE NOT NULL,
	"description" VARCHAR(1024) NOT NULL,
	"author" VARCHAR(256) NOT NULL,
	"version" VARCHAR(10) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE country (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	"token" VARCHAR(5) NOT NULL,
	"name" VARCHAR(25) NOT NULL,
	"color" VARCHAR(8) NOT NULL,
	"world" INT NOT NULL,
	"continent" INT NOT NULL,
	PRIMARY KEY ("id"),
	UNIQUE("token", "name", "world")
);

CREATE TABLE continent (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
	"name" VARCHAR(25) UNIQUE NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE neighbour (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), 
	"country" INT NOT NULL, 
	"neighbour_country" INT NOT NULL,
	PRIMARY KEY ("id"),
	UNIQUE ("country", "neighbour_country")
);

CREATE TABLE player (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	"username" VARCHAR(25) UNIQUE NOT NULL,
	"password" VARCHAR(32) NOT NULL,
	"last_name" VARCHAR(25) NOT NULL,
	"first_name" VARCHAR(25) NOT NULL,
	"email" VARCHAR(320) UNIQUE NOT NULL,
	"logged_in" SMALLINT NOT NULL DEFAULT 0,
	PRIMARY KEY ("id")
);

-- Testdaten

-- ## Player ############################################### (Password is: 1234)
insert into player ("username", "password", "last_name", "first_name", "email") values ('Takero', '81dc9bdb52d04dc20036dbd8313ed055', 'Bollmann', 'Christian', 'cbollmann@stud.hs-bremen.de');
insert into player ("username", "password", "last_name", "first_name", "email") values ('cstrempel', '81dc9bdb52d04dc20036dbd8313ed055', 'Strempel', 'Carina', 'cstrempel@stud.hs-bremen.de');
insert into player ("username", "password", "last_name", "first_name", "email") values ('akoenig', '81dc9bdb52d04dc20036dbd8313ed055', 'König', 'André', 'akoenig@stud.hs-bremen.de');

-- ## Erde #################################################
insert into world ("token", "name", "description", "author", "version") values ('earth', 'Die Erde', 'Die Erde als Risiko-Map', 'André König', '1.0 beta');

insert into continent ("name") values ('Europa');

insert into country ("token", "name", "color", "world", "continent") values ('sk', 'Skandinavien', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('ua', 'Ukraine', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('me', 'Mittel-Europa', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('se', 'Süd-Europa', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('we', 'West-Europa', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('is', 'Island', 'ffffff', 1, 1);
insert into country ("token", "name", "color", "world", "continent") values ('uk', 'Groß-Britanien', 'ffffff', 1, 1);

insert into neighbour ("country", "neighbour_country") values (1, 2);

-- NACHBARN Ukraine: Skandinavien, Mittel-Europa
insert into neighbour ("country", "neighbour_country") values (2, 1);
insert into neighbour ("country", "neighbour_country") values (2, 3);

-- NACHBARN Mittel-Europa: Ukraine, Süd-Europa, West-Europa
insert into neighbour ("country", "neighbour_country") values (3, 2);
insert into neighbour ("country", "neighbour_country") values (3, 4);
insert into neighbour ("country", "neighbour_country") values (3, 5);
-- insert into neighbour ("country", "neighbour_country") values (3, 7);

-- NACHBARN Süd-Europa: Mittel-Europa
insert into neighbour ("country", "neighbour_country") values (4, 3); 

-- NACHBARN West-Europa: Mittel-Europa, Island, Groß-Britanien
insert into neighbour ("country", "neighbour_country") values (5, 3);
insert into neighbour ("country", "neighbour_country") values (5, 6);
insert into neighbour ("country", "neighbour_country") values (5, 7); 

-- NACHBARN Island: West-Europa, Groß-Britanien
insert into neighbour ("country", "neighbour_country") values (6, 5);
insert into neighbour ("country", "neighbour_country") values (6, 7);

-- NACHBARN GroÃŸ-Britanien: Island, West-Europa
insert into neighbour ("country", "neighbour_country") values (7, 6);
insert into neighbour ("country", "neighbour_country") values (7, 5); 

-- ## Jupiter #################################################
insert into world ("token", "name", "description", "author", "version") values ('jupiter', 'Der Jupiter', 'Der Jupiter mit seinen Monden.', 'Carina Strempel', '1.0 beta');

insert into continent ("name") values ('Section 1');
insert into continent ("name") values ('Section 2');
insert into continent ("name") values ('Section 3');

insert into country ("token", "name", "color", "world", "continent") values ('io', 'Io', 'ffffff', 2, 2); -- 8
insert into country ("token", "name", "color", "world", "continent") values ('eur', 'Europa', 'ffffff', 2, 2); -- 9 
insert into country ("token", "name", "color", "world", "continent") values ('ga', 'Ganymed', 'ffffff', 2, 2); -- 10
insert into country ("token", "name", "color", "world", "continent") values ('kal', 'Kallisto', 'ffffff', 2, 2); -- 11

insert into country ("token", "name", "color", "world", "continent") values ('am', 'Amalthea', 'ffffff', 2, 2); -- 12
insert into country ("token", "name", "color", "world", "continent") values ('hm', 'Himalia', 'ffffff', 2, 2); -- 13
insert into country ("token", "name", "color", "world", "continent") values ('el', 'Elara', 'ffffff', 2, 2); -- 14

-- NACHBARN Io: Europa, Ganymed
insert into neighbour ("country", "neighbour_country") values (8, 9);
insert into neighbour ("country", "neighbour_country") values (8, 10);
insert into neighbour ("country", "neighbour_country") values (8, 13);

-- NACHBARN Europa: Io, Amalthea
insert into neighbour ("country", "neighbour_country") values (9, 8);
insert into neighbour ("country", "neighbour_country") values (9, 12);

-- NACHBARN Ganymed: Io, Kallisto, Elara
insert into neighbour ("country", "neighbour_country") values (10, 8);
insert into neighbour ("country", "neighbour_country") values (10, 11);
insert into neighbour ("country", "neighbour_country") values (10, 14);

-- NACHBARN Kallisto: Ganymed, Amalthea
insert into neighbour ("country", "neighbour_country") values (11, 10);
insert into neighbour ("country", "neighbour_country") values (11, 12);

-- NACHBARN Amalthea: Europa, Kallisto, Elara, Himalia
insert into neighbour ("country", "neighbour_country") values (12, 9);
insert into neighbour ("country", "neighbour_country") values (12, 11);
insert into neighbour ("country", "neighbour_country") values (12, 14);
insert into neighbour ("country", "neighbour_country") values (12, 13);

-- NACHBARN Himalia: Amalthea, Io
insert into neighbour ("country", "neighbour_country") values (13, 8);
insert into neighbour ("country", "neighbour_country") values (13, 12);

-- NACHBARN Elara: Ganymed, Amalthea
insert into neighbour ("country", "neighbour_country") values (14, 10);
insert into neighbour ("country", "neighbour_country") values (14, 12);