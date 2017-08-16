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

(deftest rows-test
  (testing "returns board partitioned as rows"
    (is (=
      [["x" "x" "x"] [:_ :_ :_] [:_ :_ :_]]
      (rows row-0-winner)))))

(deftest columns-test
  (testing "returns board columns [[col 0] [col 1] [col 2]]"
    (is (=
       [[:_ :_ :_]
        [:_ :_ :_]
        ["o" "o" "o"]]
      (columns col-2-winner)))))

(deftest diagonals-test
  (testing "returns board diagonals [[top left diag] [top right]]"
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
;
; (deftest winner-on-row-test
;   (testing "returns true when the given row has a winner"
;     (is (= true (winner-on-row? row-0-winner 0)))
;   (testing "returns false when the given row does not have a winner"
;     (is (= false (winner-on-row? row-0-winner 1))))))
;
; (deftest row-winner-test
;   (testing "returns true when the board has a row winner (0)"
;     (is (= true (row-winner? row-0-winner))))
;   (testing "returns true when the board has a row winner (2)"
;     (is (= true (row-winner? row-2-winner))))
;   (testing "returns false when the board does not have a row winner"
;     (is (= false (row-winner? col-2-winner)))))
;
; (deftest winner-on-column-test
;   (testing "returns true when the given column has a winner"
;     (is (= true (winner-on-column? col-2-winner 2))))
;   (testing "returns false when the given column does not have a winner"
;     (is (= false (winner-on-column? col-2-winner 0)))))
;
; (deftest column-winner-test
;   (testing "returns true when the board has a column winner"
;     (is (= true (column-winner? col-2-winner))))
;   (testing "returns false when the board does not have a column winner"
;     (is (= false (column-winner? row-0-winner))))
;   (testing "returns false when there is no column winner"
;     (is (= false (column-winner? blank-board)))))
;
; (deftest diag-winner-test
;   (testing "returns true when the board has a diagonal winner"
;     (is (= true (diag-winner? diag-0-winner))))
;   (testing "returns false when the board does not have a diagonal winner"
;     (is (= false (diag-winner? row-0-winner))))
;   (testing "returns false when there is no diagonal winner"
;     (is (= false (diag-winner? blank-board)))))
;
; (deftest has-winner-test?
;   (testing "returns true when the board has a row winner"
;     (is (= true (has-winner? row-2-winner))))
;   (testing "returns true when the board has a diagonal winner"
;     (is (= true (has-winner? diag-0-winner))))
;   (testing "returns true when the board has a column winner"
;     (is (= true (has-winner? col-2-winner))))
;   (testing "returns false when the board does not have a winner"
;     (is (= false (has-winner? marked-board)))))
;
; (deftest tie-test
;   (testing "true when there is a tie"
;     (is (= true (tie? tied-board))))
;   (testing "false when game is ongoing"
;     (is (= false (tie? marked-board))))
;   (testing "false when there is a winner"
;     (is (= false (tie? col-2-winner)))))
