(ns clojure-ttt.ai-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.ai :refer :all]
            [clojure-ttt.board :as board]))

(def x-almost-win
 (into (sorted-map)
   {0 "x" 1 "x" 2 :_
    3 :_ 4 "c" 5 :_
    6 :_ 7 :_ 8 :_}))

(def almost-full-block
 (into (sorted-map)
   {0 "x" 1 "x" 2 "c"
    3 :_ 4 :_ 5 "x"
    6 "x" 7 "c" 8 "c"}))

(def x-almost-win-col
 (into (sorted-map)
   {0 "x" 1 "c" 2 "c"
    3 :_ 4 :_ 5 "x"
    6 "x" 7 :_ 8 :_}))

(def bad-first-c-move
 (into (sorted-map)
   {0 :_ 1 :_ 2 "x"
    3 :_ 4 :_ 5 :_
    6 "x" 7 :_ 8 "c"}))

(def c-almost-win
 (into (sorted-map)
   {0 "x" 1 :_ 2 "x"
    3 "c" 4 "c" 5 :_
    6 "x" 7 :_ 8 :_}))

(def easy-board
  (into (sorted-map)
    {0 "c" 1 :_ 2 "c"
     3 :_ 4 :_ 5 "x"
     6 "x" 7 "c" 8 "x"}))

(def win-or-lose
 (into (sorted-map)
   {0 "c" 1 "x" 2 "c"
    3 "c" 4 "x" 5 "x"
    6 :_ 7 :_ 8 "x"}))

(def almost-full-block-2
 (into (sorted-map)
   {0 "c" 1 :_ 2 "x"
    3 "x" 4 "c" 5 "c"
    6 "x" 7 :_ 8 "x"}))

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

(deftest minimax-test
  (testing "when start depth is 3 and ai about to win"
    (is (= [1 12] (minimax easy-board 3 ["c" "x"] true "c" -1000 1000))))
  (testing "when start depth is 2 and ai can win or lose"
    (is (= [6 11] (minimax win-or-lose 2 ["c" "x"] true "c" -1000 1000)))))

(deftest update-best-move-score-test
  (testing "chooses max move and score pair for ai"
    (is (= [4 7] (update-best-move-score true [4 7] [6 5] 2))))
  (testing "chooses min move and score pair (based on score) for opponent"
    (is (= [1 5] (update-best-move-score false [4 7] [6 5] 1)))))

(deftest choose-4-test
  (testing "ai move after opponent chooses top left corner"
    (is (= 4 (choose-move "c"
     (into (sorted-map)
       {0 "x" 1 :_ 2 :_
        3 :_ 4 :_ 5 :_
        6 :_ 7 :_ 8 :_}) markers))))
  (testing "ai move after opponent chooses top right corner"
    (is (= 4 (choose-move "c"
     (into (sorted-map)
       {0 :_ 1 :_ 2 "x"
        3 :_ 4 :_ 5 :_
        6 :_ 7 :_ 8 :_}) markers))))
  (testing "ai move after opponent chooses bottom left corner"
    (is (= 4 (choose-move "c"
     (into (sorted-map)
       {0 :_ 1 :_ 2 :_
        3 :_ 4 :_ 5 :_
        6 "x" 7 :_ 8 :_}) markers))))
  (testing "ai move after opponent chooses bottom right corner"
    (is (= 4 (choose-move "c"
     (into (sorted-map)
       {0 :_ 1 :_ 2 :_
        3 :_ 4 :_ 5 :_
        6 :_ 7 :_ 8 "x"}) markers)))))

(deftest choose-move-test
  (testing "blocks opponent from winning"
    (is (= 2 (choose-move "c" x-almost-win markers))))
  (testing "blocks opponent from winning (options are ultimately lose or tie)"
    (is (= 3 (choose-move "c" almost-full-block markers))))
  (testing "blocks opponent who was initially dumb from winning (options are ultimately lose or tie)"
    (is (= 7 (choose-move "c" almost-full-block-2 markers))))
  (testing "blocks opponent from winning"
    (is (= 3 (choose-move "c" x-almost-win-col markers))))
  (testing "blocks opponent from winning even though it will ultimately lose"
    (is (= 4 (choose-move "c" bad-first-c-move markers))))
  (testing "2 options, one to win one to lose"
    (is (= 6 (choose-move "c" win-or-lose markers))))
  (testing "chooses winning move"
    (is (= 1 (choose-move "c" easy-board markers))))
  (testing "chooses winning move"
    (is (= 5 (choose-move "c" c-almost-win markers))))
  (testing "chooses winning move on 4x4 board"
    (is (= 6 (choose-move "c" c-almost-win-4x4 markers))))
  (testing "blocks opponent from winning 4x4"
    (is (= 6 (choose-move "c" c-almost-lose-4x4 markers)))))
