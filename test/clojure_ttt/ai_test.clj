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

; (deftest need-special-early-moves-test
;   (testing "true for 4x4"
;     (is (= true (need-special-early-moves? early-4x4))))
;   (testing "false for 3x3"
;     (is (= false (need-special-early-moves? (board/new-board 3))))))

(deftest change-turn-test
  (testing "reverses order of markers"
    (is (= ["c" "x"] (change-turn ["x" "c"])))))

; (deftest best-move-and-score-test
;   (testing "returns map with max score for ai player"
;     (is (= [3 8] (best-move-and-score "c" "c" {4 7, 2 4, 3 8}))))
;   (testing "returns map with min score for opponent"
;     (is (= [5 4] (best-move-and-score "x" "c" {4 7, 5 4, 6 8}))))
;   (testing "returns the best move from the map"
;     (is (= 3 (best-move "c" "c" {4 7, 2 4, 3 8})))))
;
; (deftest get-score-or-move-test
;   (testing "returns best score for computer player while game ongoing"
;     (is (= 8 (get-score-or-move 2 "c" "c" {4 2, 5 1, 3 8}))))
;   (testing "returns best move for computer player"
;     (is (= 3 (get-score-or-move 0 "c" "c" {4 2, 5 1, 3 8}))))
;   (testing "returns best move (min score) for non computer player"
;     (is (= 5 (get-score-or-move 0 "x" "c" {4 2, 5 1, 3 8})))))

(deftest choose-move-test
  (testing "blocks opponent from winning"
    (is (= 2 (choose-move "c" x-almost-win sample-players))))
  (testing "blocks opponent from winning"
    (is (= 3 (choose-move "c" x-almost-win-col sample-players))))
  (testing "chooses winning move"
    (is (= 5 (choose-move "c" c-almost-win sample-players)))))
  ; (testing "chooses winning move on 4x4 board"
  ;   (is (= 6 (think-fast {:ai-marker "c" :board c-almost-win-4x4 :players sample-players}))))
  ; (testing "blocks opponent from winning 4x4"
  ;   (is (= 6 (think-fast {:ai-marker "c" :board c-almost-lose-4x4 :players sample-players})))))
