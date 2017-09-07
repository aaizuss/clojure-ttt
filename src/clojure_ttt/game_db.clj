(ns clojure-ttt.game-db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure-ttt.player :as player]
            [clojure-ttt.board :as board]
            [clojure.string :as string]
            [ragtime.jdbc :as ragtime]
            [ragtime.repl :as repl]))

(def dbname "ttt_experiment")
(def host "localhost")


(def db {:dbtype "postgresql"
         :dbname dbname
         :host host})

(defn load-config []
  {:datastore (ragtime/sql-database db)
   :migrations (ragtime/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))

(defn generic-marker [player]
  (if (player/goes-first? player) "x" "o"))

(defn board-state [board player1 player2]
  (let [specific-board (board/flat-string board)
        p1-marker (player/get-marker player1)
        p2-marker (player/get-marker player2)
        p1-board (string/replace specific-board (re-pattern p1-marker) (generic-marker player1))]
    (string/replace p1-board (re-pattern p2-marker) (generic-marker player2))))

(defn- upsert [board-state turn move]
  (let [quoted-turn (str "'" turn "', ")
        quoted-board (str "'" board-state "', ")
        quoted-move (str "'{" move "}'")
        formatted-values (str "(" quoted-board quoted-turn quoted-move ") ")
        command (str "insert into game values " formatted-values "on conflict (state, turn) do update set moves = game.moves || excluded.moves")]
    (jdbc/execute! db [command])))

(defn full-table []
  (jdbc/query db ["select * from game"]))

(defn connected? []
  (let [result (try (full-table) (catch org.postgresql.util.PSQLException e false))]
    (if result true result)))

(defn update-db [board player1 player2 move]
  (let [board-state (board-state board player1 player2)
        turn (generic-marker player1)]
      (upsert board-state turn move)))
