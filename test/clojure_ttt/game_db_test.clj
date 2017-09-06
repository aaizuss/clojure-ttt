(ns clojure-ttt.game-db-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.game-db :refer :all]))

(def marked-board
 (into (sorted-map)
   {0 "A" 1 "A" 2 "B"
    3 "B" 4 :_ 5 "A"
    6 "B" 7 :_ 8 "A"}))

(def player1 {:marker "A" :human true :goes-first true})
(def player2 {:marker "B" :human true :goes-first false})

(deftest game-sql-test
  (testing "generates sql to create game table"
    (is (= "CREATE TABLE game (state VARCHAR(9) NOT NULL, turn VARCHAR(1) NOT NULL, moves integer[], PRIMARY KEY (state, turn))" (with-out-str (print game-sql))))))

(deftest board-state-test
  (testing "p1 is A and p2 is B"
    (is (= "xxoo_xo_x" (board-state marked-board player1 player2)))))
