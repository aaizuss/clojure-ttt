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

(def markers ["c" "x"])

(deftest change-turn-test
  (testing "reverses order of markers"
    (is (= ["c" "x"] (change-turn ["x" "c"])))))

(deftest get-ai-move-test
  (testing "blocks opponent from winning"
    (is (= 2 (get-ai-move "c" x-almost-win sample-players))))
  (testing "chooses winning move"
    (is (= 5 (get-ai-move "c" c-almost-win sample-players)))))

(deftest computers-tie-test
  (testing "computers tie when playing against each other"
    (let
      [new-board (board/new-board)
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
    (is (= true (board/tie? board-9))))))


; i’m struggling to figure out how to improve my ai tests without adding more dependencies. right now i test that the ai will block the opponent from winning, and that it chooses the winning move when it has the opportunity. the only other way i can think to test would be making 2 computers play against each other and testing that the result is a tie, but since the ai namespace only makes moves, i’d have to do it one move at a time
