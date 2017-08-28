(ns clojure-ttt.board-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.board :refer :all]))

(deftest new-board-test
  (testing "create an empty 3x3 board"
    (is (= (new-board 3)
      {0 :_, 1 :_, 2 :_,
       3 :_, 4 :_, 5 :_,
       6 :_, 7 :_, 8 :_}))
    (is (= (new-board 4)
      {0 :_ 1 :_ 2 :_ 3 :_
       4 :_ 5 :_ 6 :_ 7 :_
       8 :_ 9 :_ 10 :_ 11 :_
       12 :_ 13 :_ 14 :_ 15 :_}))))

(def blank-board (new-board 3))
(def blank-4-board (new-board 4))

(def marked-board
  (into (sorted-map)
    {0 "x" 1 :_ 2 :_
     3 :_ 4 "o" 5 :_
     6 :_ 7 :_ 8 "x"}))

(def row-0-winner
 (into (sorted-map)
   {0 "x" 1 "x" 2 "x"
    3 :_ 4 :_ 5 :_
    6 :_ 7 :_ 8 :_}))

(def no-winner-4x4
  (into (sorted-map)
    {0 :_ 1 :_ 2 :_ 3 :_
     4 "x" 5 "o" 6 "x" 7 "x"
     8 :_ 9 :_ 10 :_ 11 :_
     12 :_ 13 :_ 14 :_ 15 :_}))

(def row-winner-4x4
  (into (sorted-map)
    {0 :_ 1 :_ 2 :_ 3 :_
     4 "x" 5 "x" 6 "x" 7 "x"
     8 :_ 9 :_ 10 :_ 11 :_
     12 :_ 13 :_ 14 :_ 15 :_}))

(def col-winner-4x4
 (into (sorted-map)
   {0 "x" 1 :_ 2 :_ 3 :_
    4 "x" 5 :_ 6 :_ 7 :_
    8 "x" 9 :_ 10 :_ 11 :_
    12 "x" 13 :_ 14 :_ 15 :_}))

(def col-2-winner
 (into (sorted-map)
   {0 :_ 1 :_ 2 "o"
    3 :_ 4 :_ 5 "o"
    6 :_ 7 :_ 8 "o"}))

(def diag-0-winner
  (into (sorted-map)
    {0 "x" 1 :_ 2 :_
     3 :_ 4 "x" 5 :_
     6 :_ 7 :_ 8 "x"}))

(def left-diag-4x4
  (into (sorted-map)
    {0 "x" 1 :_ 2 :_ 3 :_
     4 :_ 5 "x" 6 :_ 7 :_
     8 :_ 9 :_ 10 "x" 11 :_
     12 :_ 13 :_ 14 :_ 15 "x"}))

(def right-diag-4x4
 (into (sorted-map)
   {0 :_ 1 :_ 2 :_ 3 "o"
    4 :_ 5 :_ 6 "o" 7 :_
    8 :_ 9 "o" 10 :_ 11 :_
    12 "o" 13 :_ 14 :_ 15 :_}))

(def tied-board
 (into (sorted-map)
   {0 "x" 1 "x" 2 "o"
    3 "o" 4 "o" 5 "x"
    6 "x" 7 "o" 8 "x"}))

(def full-board
  (into (sorted-map) (for [space (range 9) value ["x"]] [space value])))

(deftest board-dimension-test
  (testing "returns 3 given a 3x3 board"
    (= 3 (board-dimension blank-board)))
  (testing "returns 4 given a 4x4 board"
    (= 3 (board-dimension blank-4-board))))

(deftest marked-test
  (testing "returns false when space is empty"
    (is (= false (marked? blank-board 4))))
  (testing "returns true when space is marked"
    (is (= true (marked? marked-board 0)))))

(deftest mark-space-test
  (testing "marks space 1 with x"
    (is (= "x"
          (let [board (mark-space blank-board 1 "x")
                mark (get board 1)]
                  mark)))))

(deftest clear-space-test
  (testing "clears space 2"
    (is (= :_
          (let [board (clear-space row-0-winner 2)
                mark (get board 2)]
                  mark)))))

(deftest clear-spaces-test
  (testing "clears marked spaces"
    (is (= blank-board (clear-spaces row-0-winner [0 1 2])))))

(deftest empty-spaces-test
  (testing "all spaces are empty on a new board"
    (is (= (range 9) (empty-spaces blank-board))))
  (testing "marked spaces are not included in list"
    (is (= '(1 2 3 5 6 7) (empty-spaces marked-board)))))

(deftest num-empty-spaces-test
  (testing "new 3x3 board has 9 empty spaces"
    (is (= 9 (num-empty-spaces blank-board))))
  (testing "marked 4x4 board"
    (is (= 12 (num-empty-spaces right-diag-4x4)))))

(deftest full-test
  (testing "returns false for empty board"
    (is (= false (full? blank-board))))
  (testing "returns true for a full board"
    (is (= true (full? full-board))))
  (testing "returns false for partially marked board"
    (is (= false (full? marked-board)))))

(deftest space-exists-test
  (testing "0 exists on the board"
    (is (= true (space-exists? blank-board 0))))
  (testing "9 does not exist on the board"
    (is (= false (space-exists? blank-board 9)))))

(deftest rows-test
  (testing "returns marks on board partitioned as rows"
    (is (=
      [["x" "x" "x"] [:_ :_ :_] [:_ :_ :_]]
      (rows row-0-winner))))
  (testing "rows on 4x4 board"
    (is (=
      [[:_ :_ :_ :_] ["x" "x" "x" "x"] [:_ :_ :_ :_] [:_ :_ :_ :_]]
      (rows row-winner-4x4)))))

(deftest columns-test
  (testing "returns marks along board columns [[col 0] [col 1] [col 2]]"
    (is (=
       [[:_ :_ :_]
        [:_ :_ :_]
        ["o" "o" "o"]]
      (columns col-2-winner))))
  (testing "columns for 4x4 board"
    (is (= [["x" "x" "x" "x"] [:_ :_ :_ :_] [:_ :_ :_ :_] [:_ :_ :_ :_]]
           (columns col-winner-4x4)))))

(deftest diagonals-test
  (testing "returns marks along board diagonals [[top left diag] [top right]]"
    (is (= [["x" "x" "x"]
            [:_ "x" :_]]
      (diagonals diag-0-winner))))
  (testing "full left diagonal on 4x4 board"
    (is (= [["x" "x" "x" "x"][:_ :_ :_ :_]]
           (diagonals left-diag-4x4))))
  (testing "full right diagonal on 4x4 board"
    (is (= [[:_ :_ :_ :_]["o" "o" "o" "o"]]
           (diagonals right-diag-4x4)))))

(deftest in-a-row-test
  (testing "given a row (collection of values from board ie a single diagonal),
  returns true when the values are the same mark"
    (let [row (get (diagonals diag-0-winner) 0)]
      (is (= true (in-a-row? row)))))
  (testing "returns false for an empty row"
    (let [row '(:_ :_ :_)]
      (is (= false (in-a-row? row))))))

(deftest all-rows-test
  (testing "returns a collection of rows (diag/col/row) from the board
  eg: [[row 0] [row 1] [row 2][diag 0] [diag 1]...]"
    (is (= (all-rows row-0-winner)
      [["x" "x" "x"] [:_ :_ :_] [:_ :_ :_]
      ["x" :_ :_] ["x" :_ :_] ["x" :_ :_]
      ["x" :_ :_] ["x" :_ :_]]))))

(deftest to-indexed-vec-test
  (testing "converts board structure to an indexed vector"
    (is (= [[0 :_] [1 :_] [2 :_]
            [3 :_] [4 :_] [5 :_]
            [6 :_] [7 :_] [8 :_]]
            (to-indexed-vec blank-board)))))

(deftest render-space-test
  (testing "the space index when the space is empty"
    (is (= " 4  "
      (render-space [4 :_])))
  (testing "the space index when the space is marked"
    (is (= " x  "
      (render-space [1 "x"]))))))

(deftest to-string-list-test
  (testing "converts the board to a list of padded strings"
    (is (= '(" x  " " 1  " " 2  " " 3  " " x  " " 5  " " 6  " " 7  " " x  ") (to-string-list diag-0-winner)))))

(deftest has-winner-test
  (testing "returns true when there is a winner on a row"
    (is (= true (has-winner? row-0-winner))))
  (testing "returns true when there is a winner on a diagonal"
    (is (= true (has-winner? diag-0-winner))))
  (testing "returns true when there is a winner on a column"
    (is (= true (has-winner? col-2-winner))))
  (testing "returns false when there is no winner"
    (is (= false (has-winner? marked-board))))
  (testing "returns false for a blank board"
    (is (= false (has-winner? blank-board))))
  (testing "true when there is a 4x4 winner on a row"
    (is (= true (has-winner? row-winner-4x4))))
  (testing "true when there is a 4x4 winner on a column"
    (is (= true (has-winner? col-winner-4x4))))
  (testing "true when there is a 4x4 winner on a diagonal"
    (is (= true (has-winner? left-diag-4x4))))
  (testing "false when 4x4 has no winner"
    (is (= false (has-winner? no-winner-4x4)))))

(deftest get-winner-test
  (testing "returns winning mark"
    (is (= "x" (get-winner diag-0-winner))))
  (testing "returns winning mark for 4x4 board"
    (is (= "x" (get-winner col-winner-4x4)))))

(deftest tie-test
  (testing "true when there is a tie"
    (is (= true (tie? tied-board))))
  (testing "false when game is ongoing"
    (is (= false (tie? marked-board))))
  (testing "false when there is a winner"
    (is (= false (tie? col-2-winner)))))

(deftest game-over-test
  (testing "true when there is a tie"
    (is (= true (game-over? tied-board))))
  (testing "false when game is ongoing"
    (is (= false (game-over? marked-board))))
  (testing "true when there is a winner"
    (is (= true (game-over? col-2-winner)))))
