(ns clojure-ttt.game-db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure-ttt.player :as player]
            [clojure-ttt.board :as board]
            [clojure.string :as string]))

(def dbname "ttt_experiment")
(def host "localhost")

(def db {:dbtype "postgresql"
         :dbname dbname
         :host host
         :user "amandaaizuss"})

(defn generic-marker [player]
  (if (player/goes-first? player) "x" "o"))

(defn board-state [board player1 player2]
  (let [specific-board (board/flat-string board)
        p1-marker (player/get-marker player1)
        p2-marker (player/get-marker player2)
        p1-board (string/replace specific-board (re-pattern p1-marker) (generic-marker player1))]
    (string/replace p1-board (re-pattern p2-marker) (generic-marker player2))))

; to create a table called game
(def game-sql (jdbc/create-table-ddl :game [[:state "VARCHAR(16) NOT NULL"]
                                          [:turn "VARCHAR(1) NOT NULL"]
                                          [:moves "integer[]"]
                                          ["PRIMARY KEY" "(state, turn)"]]))

(defn- upsert [board-state turn move]
  (let [quoted-turn (str "'" turn "', ")
        quoted-board (str "'" board-state "', ")
        quoted-move (str "'{" move "}'")
        formatted-values (str "(" quoted-board quoted-turn quoted-move ") ")
        command (str "insert into game values " formatted-values "on conflict (state, turn) do update set moves = game.moves || excluded.moves")]
    (jdbc/execute! db [command])))

(defn update-db [board player1 player2 move]
  (let [board-state (board-state board player1 player2)
        turn (generic-marker player1)]
      (upsert board-state turn move)))

(def count-moves
  (jdbc/query db ["select state, move, count(*) as count from game, unnest(game.moves) as move group by 1,2 order by 1,3 desc"]))
