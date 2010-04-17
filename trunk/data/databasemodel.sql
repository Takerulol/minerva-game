CREATE TABLE world (
	"id" INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	"token" CHAR(5) NOT NULL,
	"name" CHAR(64) UNIQUE NOT NULL,
	"description" VARCHAR(1024) NOT NULL,
	"author" VARCHAR(256) NOT NULL,
	"version" CHAR(10) NOT NULL,
	PRIMARY KEY ("id")
);



CREATE TABLE continents (
	id INT NOT NULL PRIMARY KEY, 
	name char(25)
);

CREATE TABLE countries (
	id INT NOT NULL PRIMARY KEY, 
	token CHAR(5), 
	name CHAR(25), 
	color CHAR(6), 
	world INT REFERENCES worlds(id),
	continent INT REFERENCES continents(id)
);

CREATE TABLE neighbours (
	id INT NOT NULL PRIMARY KEY, 
	country INT REFERENCES worlds(id), 
	neighbour_country INT REFERENCES countries(id)
);