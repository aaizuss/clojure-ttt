(ns clojure-ttt.game-db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure-ttt.player :as player]
            [clojure-ttt.board :as board]))

(def dbname "ttt_experiment")

(def db {:dbtype "postgresql"
         :dbname dbname
         :host "localhost"
         :user "amandaaizuss"})

; not great to put this in this namespace?
(defn generic-marker [player]
  (if (player/goes-first? player) "x" "o"))

; to create a table called game
(def game-sql (jdbc/create-table-ddl :game [[:game_id :serial "PRIMARY KEY"]
                                          [:state "VARCHAR(9)"]
                                          [:turn "VARCHAR(1)"]
                                          [:move "integer"]]))

; what happens if i try to insert a game where the state
; already exists...
; i could just have lots of repeat states
; then to see moves, do
; SELECT moves FROM game WHERE state = '__x_o____' AND turn = 'x';
; and can find max count
(defn insert-record [board-state turn move]
  (jdbc/insert! db :game {:state board-state :turn turn :move move}))

(defn update-db [board player move]
  (insert-record (board/flat-string board) (generic-marker player) move))

; this returns a table with the count and move
(defn count-moves [board-state turn]
  (jdbc/query db [(str "SELECT count(*), move FROM game WHERE state = " board-state " AND turn = " turn " group by move order by count(*) desc limit 1")]))

; sequence of moves for specific board
;(jdbc/query db ["SELECT move FROM game WHERE state = _________ AND turn = 'x'"] {:row-fn :move})
