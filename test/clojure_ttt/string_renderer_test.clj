(ns clojure-ttt.string-renderer-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.string-renderer :refer :all]
            [clojure-ttt.board :as board]))

(def diagonal-winner
  (into (sorted-map)
    {0 "x" 1 :_ 2 :_
     3 :_ 4 "x" 5 :_
     6 :_ 7 :_ 8 "x"}))

(deftest win-message-test
  (testing "customizes message for marker"
    (is (= "X wins!" (win-message "X")))))

(deftest row-divider-test
  (testing "returns row-divider for 3x3 board"
    (is (=
      "\n---- ---- ---- \n"
      (row-divider 3))))
  (testing "returns row divider for 4x4 board"
    (is (= "\n---- ---- ---- ---- \n" (row-divider 4)))))

(deftest row-strings-test
  (testing "returns 2d list of strings representing rows"
    (is (= (row-strings diagonal-winner)
            ['(" x  " "|" " 1  " "|" " 2  ")
            (list "\n---- ---- ---- \n")
            '(" 3  " "|" " x  " "|" " 5  ")
            (list "\n---- ---- ---- \n")
            '(" 6  " "|" " 7  " "|" " x  ")]))))

(deftest render-board-test
  (testing "renders the board as a string (with artistic flair)"
    (is (= (render-board diagonal-winner)
            (wrap-newline " x  | 1  | 2  \n---- ---- ---- \n 3  | x  | 5  \n---- ---- ---- \n 6  | 7  | x  ")))))

(deftest marker-selection-test
  (testing "renders the message for player 2"
    (is (= "Choose a single letter for Player 2's mark:"
            (marker-selection 2)))))

(deftest turn-message-test
  (testing "renders turn message for a given mark"
    (is (= "It is X's turn." (turn-message "X")))))
