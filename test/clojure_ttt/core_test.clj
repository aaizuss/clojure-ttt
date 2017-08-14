(ns clojure-ttt.core-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.core :refer :all]))

(deftest new-board-test
  (testing "new board creates empty board with 9 spaces"
    (is (= (new-board)
      {0 {:marked false, :mark nil},
       1 {:marked false, :mark nil},
       2 {:marked false, :mark nil},
       3 {:marked false, :mark nil},
       4 {:marked false, :mark nil},
       5 {:marked false, :mark nil},
       6 {:marked false, :mark nil},
       7 {:marked false, :mark nil},
       8 {:marked false, :mark nil}}))))

(def blank-board (new-board))
(def marked-board
  {0 {:marked true, :mark "x"},
   1 {:marked false, :mark nil},
   2 {:marked false, :mark nil},
   3 {:marked false, :mark nil},
   4 {:marked true, :mark "o"},
   5 {:marked false, :mark nil},
   6 {:marked false, :mark nil},
   7 {:marked false, :mark nil},
   8 {:marked true, :mark "x"}})

(deftest marked-test
  (testing "returns false when space is empty"
    (is (= false (marked? blank-board 4))))
  (testing "returns true when space is marked"
    (is (= true (marked? marked-board 0)))))

(deftest mark-space-test
  (testing "marks space by changing marked to true and filling in the mark"
    (is (= {:marked true, :mark "x"}
          (let [board (mark-space blank-board 1 "x")
                properties (get board 1)]
        properties)))))

(deftest valid-spaces-test
  (testing "all spaces are valid on a new board"
    (is (= (set (range 9)) (set (valid-spaces blank-board))))
  (testing "marked spaces are not included in list"
    (is (= (set '(1 2 3 5 6 7)) (set (valid-spaces marked-board)))))))
