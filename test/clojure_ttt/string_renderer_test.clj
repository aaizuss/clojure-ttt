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
            (wrap-newline " x | 1 | 2 \n--- --- --- \n 3 | x | 5 \n--- --- --- \n 6 | 7 | x ")))))

(deftest marker-selection-test
  (testing "renders the message for player 2"
    (is (= "Player 2, enter a single letter for your mark: "
            (marker-selection 2)))))
