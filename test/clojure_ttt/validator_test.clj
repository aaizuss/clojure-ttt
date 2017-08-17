(ns clojure-ttt.validator-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.validator :refer :all]
            [clojure-ttt.board :as board]))

(def blank-board (board/new-board))

(def marked-board
  (into (sorted-map)
    {0 "x" 1 :_ 2 :_
     3 :_ 4 "o" 5 :_
     6 :_ 7 :_ 8 "x"}))

(deftest is-num-test
  (testing "false if not a number"
    (is (= false (is-num? "a"))))
  (testing "'5' can be parsed as a number"
    (is (= true (is-num? "5")))))

(deftest valid-board-position-test
  (testing "false if not a number"
    (is (= false (valid-board-position? blank-board "a"))))
  (testing "false if not on board"
    (is (= false (valid-board-position? blank-board "10"))))
  (testing "true if position is on board"
    (is (= true (valid-board-position? blank-board "1"))))
  (testing "true if position is on board (even if it is marked)"
    (is (= true (valid-board-position? marked-board "4")))))

(deftest can-move-test
  (testing "true if position is not marked"
    (is (= true (valid-move? blank-board "2"))))
  (testing "false if the space does not exist"
    (is (= false (valid-move? blank-board "9"))))
  (testing "false if the space is taken"
    (is (= false (valid-move? marked-board "4")))))
