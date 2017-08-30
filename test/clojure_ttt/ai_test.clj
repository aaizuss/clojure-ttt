(ns clojure-ttt.ai-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.ai :refer :all]
            [clojure-ttt.board :as board]))

(def sample-players ["x" "c"])

(def x-almost-win
 (into (sorted-map)
   {0 "x" 1 "x" 2 :_
    3 :_ 4 "c" 5 :_
    6 :_ 7 :_ 8 :_}))

(def x-almost-win-col
 (into (sorted-map)
   {0 "x" 1 "c" 2 "c"
    3 :_ 4 :_ 5 "x"
    6 "x" 7 :_ 8 :_}))

(def c-almost-win
 (into (sorted-map)
   {0 "x" 1 :_ 2 :_
    3 "c" 4 "c" 5 :_
    6 "x" 7 :_ 8 :_}))

(def c-almost-win-4x4
 (into (sorted-map)
   {0 "x" 1 :_ 2 :_ 3 :_
    4 "c" 5 "c" 6 :_ 7 "c"
    8 "x" 9 :_ 10 :_ 11 :_
    12 :_ 13 :_ 14 :_ 15 :_}))

(def c-almost-lose-4x4
 (into (sorted-map)
   {0 "c" 1 :_ 2 :_ 3 :_
    4 "x" 5 "x" 6 :_ 7 "x"
    8 "c" 9 :_ 10 :_ 11 :_
    12 :_ 13 :_ 14 :_ 15 :_}))

(def early-4x4
 (into (sorted-map)
   {0 "x" 1 :_ 2 :_ 3 :_
    4 :_ 5 :_ 6 :_ 7 "c"
    8 "x" 9 :_ 10 :_ 11 :_
    12 :_ 13 :_ 14 :_ 15 :_}))

(def markers ["c" "x"])

(deftest change-turn-test
  (testing "reverses order of markers"
    (is (= ["c" "x"] (change-turn ["x" "c"])))))

(deftest update-alpha-test
  (testing "updates alpha when it is the ai turn"
    (is (= 8 (update-alpha true 4 8))))
  (testing "does not update alpha when it is not the ai's turn"
    (is (= 4 (update-alpha true 4 8)))))

(deftest best-move-and-score-test
  (testing "returns map with max score for ai player"
    (is (= [3 8] (best-move-and-score true {4 7, 2 4, 3 8}))))
  (testing "returns map with min score for opponent"
    (is (= [5 4] (best-move-and-score false {4 7, 5 4, 6 8}))))
  (testing "returns the best move from the map"
    (is (= 3 (best-move true {4 7, 2 4, 3 8})))))

(deftest return-score-or-move-test
  (testing "returns best score for computer player while game ongoing"
    (is (= 8 (return-score-or-move 2 {4 2, 5 1, 3 8} true))))
  (testing "returns best move for computer player"
    (is (= 3 (return-score-or-move 0 {4 2, 5 1, 3 8} true))))
  (testing "returns best move (min score) for non computer player"
    (is (= 5 (return-score-or-move 0 {4 2, 5 1, 3 8} false)))))

(deftest think-fast-test
  (testing "blocks opponent from winning"
    (is (= 2 (think-fast x-almost-win "c" sample-players))))
  (testing "blocks opponent from winning"
    (is (= 3 (think-fast x-almost-win-col "c" sample-players))))
  (testing "chooses winning move"
    (is (= 5 (think-fast c-almost-win "c" sample-players)))))
  ; (testing "chooses winning move on 4x4 board"
  ;   (is (= 6 (think-fast {:ai-marker "c" :board c-almost-win-4x4 :players sample-players}))))
  ; (testing "blocks opponent from winning 4x4"
  ;   (is (= 6 (think-fast {:ai-marker "c" :board c-almost-lose-4x4 :players sample-players})))))
