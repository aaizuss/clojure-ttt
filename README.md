# Clojure Tic Tac Toe

Learning clojure by writing a simple command line tic tac toe.

## Features
* Play on a 3x3 or 4x4 board
* Choose your own marker
* Play against another person or an unbeatable computer
* Undo moves when playing against the computer

## Database Setup
the game saves moves to a PostgreSQL database, which requires you to have
a PostgreSQL database running locally
* Install [PostgreSQL](https://www.postgresql.org/download/) - I recommend following these instructions and configuring your `$PATH` to use the command line tools: [postgrespp](https://postgresapp.com/)
* On the command line, enter `psql postgres`
* Create a database called `ttt_experiment` by entering
```
CREATE DATABASE ttt_experiment
```

## Usage

You must have [Leiningen](https://leiningen.org/) installed. Clone this repo, and make sure you're in the root directory `clojure-ttt`

### Play
* If this is your fist time running the game, run the database migration with `lein migrate`
* To play the game, enter `lein run`

### Tests
* Run all tests: `lein test`
* Run tests for a specific namespace (eg, board-test): `lein test clojure-ttt.board-test`

### Migrations
* `lein migrate`
* `lein rollback`
