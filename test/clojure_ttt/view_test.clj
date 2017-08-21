(ns clojure-ttt.view-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.view :refer :all]
            [clojure-ttt.board :as board]))

(def blank-board (board/new-board))
(def marked-board (board/mark-space blank-board 6 "x"))

(deftest invalid-marker-msg-test
  (testing "must be single letter"
    (is (= "www is an invalid mark. Markers must be a single letter."
            (invalid-marker-msg "www" ""))))
  (testing "does not accept special characters"
    (is (= "* is an invalid mark. You must choose a letter."
            (invalid-marker-msg "*" ""))))
  (testing "does not accept marker that is the same as opponent"
    (is (= "A is an invalid mark. Your opponent already chose that marker."
            (invalid-marker-msg "A" "A")))))

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
  (testing "returns a valid move as an int"
    (is (= 4
        (with-in-str "4" (get-move blank-board)))))
  (testing "continues asking for a move until it is valid"
    (is (= 2
        (with-in-str "10\na\n2" (get-move blank-board)))))
  (testing "continues asking for a marker if space is taken"
    (is (= 0
        (with-in-str "6\n0" (get-move marked-board))))))
