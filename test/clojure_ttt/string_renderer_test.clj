(ns clojure-ttt.string-renderer-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.string-renderer :refer :all]
            [clojure-ttt.board :as board]))

(def diagonal-winner
  (into (sorted-map)
    {0 "x" 1 :_ 2 :_
     3 :_ 4 "x" 5 :_
     6 :_ 7 :_ 8 "x"}))

(deftest row-divider-test
  (testing "returns row-divider"
    (is (=
      "\n--- --- --- \n"
      (row-divider)))))

(deftest render-space-test
  (testing "the space index when the space is empty"
    (is (= " 4 "
      (render-space [4 :_])))
  (testing "the space index when the space is marked"
    (is (= " x "
      (render-space [1 "x"]))))))

(deftest string-list-from-board-test
  (testing "converts the board to a list of strings"
    (is (= '(" x " " 1 " " 2 " " 3 " " x " " 5 " " 6 " " 7 " " x ") (string-list-from-board diagonal-winner)))))

(deftest board-to-string-test
  (testing "converts the board to a string"
    (is (= " x  1  2  3  x  5  6  7  x " (board-to-string diagonal-winner)))))

(deftest row-strings-test
  (testing "returns 2d list of strings representing rows"
    (is (= (row-strings diagonal-winner)
            ['(" x " "|" " 1 " "|" " 2 ")
            (list "\n--- --- --- \n")
            '(" 3 " "|" " x " "|" " 5 ")
            (list "\n--- --- --- \n")
            '(" 6 " "|" " 7 " "|" " x ")]))))

(deftest render-board-test
  (testing "renders the board as a string (with artistic flair)"
    (is (= (render-board diagonal-winner)
            " x | 1 | 2 \n--- --- --- \n 3 | x | 5 \n--- --- --- \n 6 | 7 | x "))))
