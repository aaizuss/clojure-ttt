(ns clojure-ttt.view-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.view :refer :all]
            [clojure-ttt.board :as board]))

(def blank-board (board/new-board 3))
(def marked-board (board/mark-space blank-board 6 "x"))
(def game-options {:1 :human-v-human :2 :human-v-cpu :3 :cpu-v-human})
(def board-options {:3 :3x3 :4 :4x4})

(deftest invalid-marker-msg-test
  (testing "must be single letter"
    (with-out-str
      (is (= "www is an invalid mark. Markers must be a single letter."
            (invalid-marker-msg "www" "")))))
  (testing "does not accept special characters"
    (with-out-str
      (is (= "* is an invalid mark. You must choose a letter."
            (invalid-marker-msg "*" "")))))
  (testing "does not accept marker that is the same as opponent"
    (with-out-str
      (is (= "A is an invalid mark. Your opponent already chose that marker."
            (invalid-marker-msg "A" "A"))))))

(deftest get-marker-test
  (testing "returns a valid mark"
    (with-out-str
      (is (= "x" (with-in-str "x" (get-marker :order-num 1))))))
  (testing "continues asking for a marker until it is valid"
    (with-out-str
      (is (= "x"
        (with-in-str "?\n4\nbbb\nx"
          (get-marker :order-num 1))))))
  (testing "continues asking for a marker if given opponent mark"
    (with-out-str
      (is (= "x"
        (with-in-str "a\nx"
          (get-marker :order-num 2 :opponent-marker "a")))))))

(deftest get-move-test
  (testing "returns a valid move as an int"
    (with-out-str
      (is (= 4
        (with-in-str "4" (get-move blank-board))))))
  (testing "continues asking for a move until it is valid"
    (with-out-str
      (is (= 2
        (with-in-str "10\na\n2" (get-move blank-board))))))
  (testing "continues asking for a marker if space is taken"
    (with-out-str
      (is (= 0
          (with-in-str "6\n0" (get-move marked-board)))))))

(deftest get-move-or-undo-test
  (testing "returns a valid move as an int"
    (with-out-str
      (is (= 4
        (with-in-str "4" (get-move-or-undo blank-board []))))))
  (testing "returns u if user types u with a long enough move history"
    (with-out-str
      (is (= "u"
          (with-in-str "u" (get-move-or-undo marked-board [6 8 2]))))))
  (testing "continues asking for a move when move history is empty"
    (with-out-str
      (is (= 4
          (with-in-str "u\nu\n4" (get-move-or-undo blank-board [])))))))

(deftest get-game-selection-test
  (testing "valid input returns the game selection"
    (with-out-str
      (is (= :human-v-cpu
          (with-in-str "2" (get-game-selection game-options)))))))

(deftest get-board-selection-test
  (testing "valid input returns the game selection"
    (with-out-str
      (is (= :4x4
          (with-in-str "4" (get-board-selection board-options)))))))
