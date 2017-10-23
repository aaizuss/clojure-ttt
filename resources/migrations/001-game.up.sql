CREATE TABLE game (state VARCHAR(16) NOT NULL,
                   turn VARCHAR(1) NOT NULL,
                   moves INTEGER[],
                   PRIMARY KEY (state, turn));
