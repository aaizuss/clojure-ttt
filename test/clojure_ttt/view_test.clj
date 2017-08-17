(ns clojure-ttt.view-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.view :refer :all]))

; i need to figure out how to prevent
; the tests from printing
(deftest get-marker-test
  (testing "returns a valid mark"
    (is (= "x"
        (with-in-str "x" (get-marker :order-num 1)))))
  (testing "continues asking for a marker until it is valid")
    (is (= "x"
        (with-in-str "?\n4\nbbb\nx"
            (get-marker :order-num 1))))
  (testing "continues asking for a marker if given opponent mark"
    (is (= "x"
        (with-in-str "a\nx"
            (get-marker :order-num 2 :opponent-marker "a"))))))
