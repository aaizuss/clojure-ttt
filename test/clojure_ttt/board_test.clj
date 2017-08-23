(ns clojure-ttt.board-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.board :refer :all]))

(deftest new-board-test
  (testing "new board creates empty board with 9 spaces"
    (is (= (new-board)
      {0 :_, 1 :_, 2 :_,
       3 :_, 4 :_, 5 :_,
       6 :_, 7 :_, 8 :_}))))

(def blank-board (new-board))

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

(def row-2-winner
 (into (sorted-map)
   {0 :_ 1 :_ 2 :_
    3 "o" 4 "o" 5 "o"
    6 :_ 7 :_ 8 :_}))

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

(def tied-board
 (into (sorted-map)
   {0 "x" 1 "x" 2 "o"
    3 "o" 4 "o" 5 "x"
    6 "x" 7 "o" 8 "x"}))

(def full-board
  (into (sorted-map) (for [space (range 9) value ["x"]] [space value])))

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

(deftest empty-spaces-test
  (testing "all spaces are empty on a new board"
    (is (= (range 9) (empty-spaces blank-board))))
  (testing "marked spaces are not included in list"
    (is (= '(1 2 3 5 6 7) (empty-spaces marked-board)))))

(deftest full-test
  (testing "returns false for empty board"
    (is (= false (full? blank-board))))
  (testing "returns true for a full board"
    (is (= true (full? full-board))))
  (testing "returns false for partially marked board"
    (is (= false (full? marked-board)))))

(deftest space-exists-test
  (testing "0 exists on the board"
    (is (= true (space-exists? 0))))
  (testing "9 does not exist on the board"
    (is (= false (space-exists? 9)))))

(deftest rows-test
  (testing "returns marks on board partitioned as rows"
    (is (=
      [["x" "x" "x"] [:_ :_ :_] [:_ :_ :_]]
      (rows row-0-winner)))))

(deftest columns-test
  (testing "returns marks along board columns [[col 0] [col 1] [col 2]]"
    (is (=
       [[:_ :_ :_]
        [:_ :_ :_]
        ["o" "o" "o"]]
      (columns col-2-winner)))))

(deftest diagonals-test
  (testing "returns marks along board diagonals [[top left diag] [top right]]"
    (is (= [["x" "x" "x"]
            [:_ "x" :_]]
      (diagonals diag-0-winner)))))

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
    (is (= " 4 "
      (render-space [4 :_])))
  (testing "the space index when the space is marked"
    (is (= " x "
      (render-space [1 "x"]))))))

(deftest to-string-list-test
  (testing "converts the board to a list of strings"
    (is (= '(" x " " 1 " " 2 " " 3 " " x " " 5 " " 6 " " 7 " " x ") (to-string-list diag-0-winner)))))

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
    (is (= false (has-winner? blank-board)))))

(deftest get-winner-test
  (testing "returns winning mark"
    (is (= "x" (get-winner diag-0-winner)))))

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
