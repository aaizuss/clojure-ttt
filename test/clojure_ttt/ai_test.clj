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

(deftest update-best-move-score-test
  (testing "chooses max move and score pair for ai"
    (is (= [4 7] (update-best-move-score true [4 7] [6 5] 2))))
  (testing "chooses min move and score pair (based on score) for opponent"
    (is (= [1 5] (update-best-move-score false [4 7] [6 5] 1)))))

(deftest choose-move-test
  (testing "blocks opponent from winning"
    (is (= 2 (choose-move "c" x-almost-win sample-players))))
  (testing "blocks opponent from winning"
    (is (= 3 (choose-move "c" x-almost-win-col sample-players))))
  (testing "chooses winning move"
    (is (= 5 (choose-move "c" c-almost-win sample-players)))))
  ; (testing "chooses winning move on 4x4 board"
  ;   (is (= 6 (choose-move "c" c-almost-win-4x4 sample-players))))
  ; (testing "blocks opponent from winning 4x4"
  ;   (is (= 6 (choose-move "c" c-almost-lose-4x4 sample-players)))))
