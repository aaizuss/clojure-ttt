(ns clojure-ttt.board-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.string-renderer :refer :all]
            [clojure-ttt.board :as board]))

(deftest row-divider-test
  (testing "returns row-divider"
    (is (=
      "\n--- --- ---\n"
      (row-divider)))))

(deftest render-space-test
  (testing "the space index when the space is empty"
    (is (= " 4 "
      (render-space board/new-board 4)))))
