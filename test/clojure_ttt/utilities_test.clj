(ns clojure-ttt.utilities-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.utilities :refer :all]))

(deftest input-to-num-test
  (testing "parses numeric string into a number"
    (is (= 5 (input-to-num "5"))))
  (testing "parses numeric string into a number"
    (is (= -20 (input-to-num "-20")))))

(deftest in-range-test
  (testing "0 is in [0, 9)"
    (is (= true (in-range? 0 0 9))))
  (testing "9 is not in [0, 9)"
    (is (= false (in-range? 9 0 9))))
  (testing "4 is in [0, 9)"
    (is (= true (in-range? 4 0 9)))))