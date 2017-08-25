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

(deftest need-special-early-moves-test
  (testing "true for 4x4"
    (is (= true (need-special-early-moves? early-4x4))))
  (testing "false for 3x3"
    (is (= false (need-special-early-moves? (board/new-board 3))))))

(deftest change-turn-test
  (testing "reverses order of markers"
    (is (= ["c" "x"] (change-turn ["x" "c"])))))

(deftest get-ai-move-test
  (testing "blocks opponent from winning"
    (is (= 2 (get-ai-move "c" x-almost-win sample-players))))
  (testing "chooses winning move"
    (is (= 5 (get-ai-move "c" c-almost-win sample-players)))))
  ; (testing "chooses winning move on 4x4 board"
  ;   (is (= 6 (get-ai-move "c" c-almost-win-4x4 sample-players))))
  ; (testing "blocks opponent from winning 4x4"
  ;   (is (= 6 (get-ai-move "c" c-almost-lose-4x4 sample-players)))))

; lol
(deftest computers-tie-test
  (testing "computers tie when playing against each other"
    (let
      [new-board (board/new-board 3)
       c-move-1 (get-ai-move "c" new-board sample-players)
       board-1 (board/mark-space new-board c-move-1 "c")
       x-move-1 (get-ai-move "x" board-1 sample-players)
       board-2 (board/mark-space board-1 x-move-1 "x")
       c-move-2 (get-ai-move "c" board-2 sample-players)
       board-3 (board/mark-space board-2 c-move-2 "c")
       x-move-2 (get-ai-move "x" board-3 sample-players)
       board-4 (board/mark-space board-3 x-move-2 "x")
       c-move-3 (get-ai-move "x" board-4 sample-players)
       board-5 (board/mark-space board-4 c-move-3 "c")
       x-move-3 (get-ai-move "x" board-5 sample-players)
       board-6 (board/mark-space board-5 x-move-3 "x")
       c-move-4 (get-ai-move "x" board-6 sample-players)
       board-7 (board/mark-space board-6 c-move-4 "c")
       x-move-4 (get-ai-move "x" board-7 sample-players)
       board-8 (board/mark-space board-7 x-move-4 "x")
       c-move-5 (get-ai-move "x" board-8 sample-players)
       board-9 (board/mark-space board-8 c-move-5 "c")]
    (is (= true (board/tie? board-9)))))
  (testing "start from mid game"
    (let
      [c-move-4 (get-ai-move "c" x-almost-win sample-players)
       board-1 (board/mark-space x-almost-win c-move-4 "c")
       x-move-5 (get-ai-move "x" board-1 sample-players)
       board-2 (board/mark-space board-1 x-move-5 "x")
       c-move-6 (get-ai-move "c" board-2 sample-players)
       board-3 (board/mark-space board-2 c-move-6 "c")
       x-move-7 (get-ai-move "x" board-3 sample-players)
       board-4 (board/mark-space board-3 x-move-7 "x")
       c-move-8 (get-ai-move "x" board-4 sample-players)
       board-5 (board/mark-space board-4 c-move-8 "c")
       x-move-9 (get-ai-move "x" board-5 sample-players)
       board-6 (board/mark-space board-5 x-move-9 "x")]
    (is (= true (board/tie? board-6))))))
