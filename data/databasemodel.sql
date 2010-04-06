CREATE TABLE worlds (
	id INT NOT NULL PRIMARY KEY, 
	token CHAR(5), 
	name CHAR(25), 
	description LONG VARCHAR, 
	author LONG VARCHAR, 
	version CHAR(10)
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