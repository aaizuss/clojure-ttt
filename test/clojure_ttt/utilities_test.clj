(ns clojure-ttt.utilities-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.utilities :refer :all]))

(deftest input-to-num-test
  (testing "parses numeric string into a number"
    (is (= 5 (input-to-num "5"))))
  (testing "parses numeric string into a number"
    (is (= -20 (input-to-num "-20")))))
