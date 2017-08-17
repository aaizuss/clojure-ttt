(ns clojure-ttt.view-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.view :refer :all]
            [clojure-ttt.board :as board]))

(def blank-board (board/new-board))
(def marked-board (board/mark-space blank-board 6 "x"))

; i need to figure out how to prevent
; the tests from printing
(deftest get-marker-test
  (testing "returns a valid mark"
    (is (= "x"
        (with-in-str "x" (get-marker :order-num 1)))))
  (testing "continues asking for a marker until it is valid"
    (is (= "x"
        (with-in-str "?\n4\nbbb\nx" (get-marker :order-num 1)))))
  (testing "continues asking for a marker if given opponent mark"
    (is (= "x"
        (with-in-str "a\nx"
            (get-marker :order-num 2 :opponent-marker "a"))))))

(deftest get-move-test
  (testing "returns a valid move"
    (is (= "4"
        (with-in-str "4" (get-move blank-board)))))
  (testing "continues asking for a move until it is valid"
    (is (= "2"
        (with-in-str "10\na\n2" (get-move blank-board)))))
  (testing "continues asking for a marker if space is taken"
    (is (= "0"
        (with-in-str "6\n0" (get-move marked-board))))))
