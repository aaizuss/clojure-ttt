(ns clojure-ttt.game-db-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.game-db :refer :all]))

(deftest game-sql-test
  (testing "generates sql to create game table"
    (is (= "CREATE TABLE game (game_id serial PRIMARY KEY, state VARCHAR(9), turn VARCHAR(1), move integer)" (with-out-str (print game-sql))))))
