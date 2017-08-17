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

(deftest valid-move-test
  (testing "true if position is not marked"
    (is (= true (valid-move? blank-board 2))))
  (testing "false if the space does not exist"
    (is (= false (valid-move? blank-board 9))))
  (testing "false if the space is taken"
    (is (= false (valid-move? marked-board 4)))))
