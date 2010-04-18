CREATE TABLE world (
	"id" INT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"token" CHAR(5) UNIQUE NOT NULL,
	"name" CHAR(64) UNIQUE NOT NULL,
	"description" VARCHAR(1024) NOT NULL,
	"author" VARCHAR(256) NOT NULL,
	"version" CHAR(10) NOT NULL,
	PRIMARY KEY ("id")
);

CREATE TABLE country (
	"id" INT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
	"token" CHAR(5) UNIQUE NOT NULL,
	"name" CHAR(25) UNIQUE NOT NULL,
	"color" CHAR(8) NOT NULL,
	"world" INT NOT NULL,
	"continent" INT NOT NULL
);

CREATE TABLE continents (
	id INT NOT NULL PRIMARY KEY, 
	name char(25)
);

CREATE TABLE neighbours (
	id INT NOT NULL PRIMARY KEY, 
	country INT REFERENCES worlds(id), 
	neighbour_country INT REFERENCES countries(id)
);