(ns clojure-ttt.player-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.player :refer :all]))

(def sample-human
  {:marker "x" :human true :goes-first true})

(def sample-computer
  {:marker "o" :human false :goes-first false})

(deftest create-player-test
  (testing "creates a human player that goes first"
    (is (= sample-human (create-player "x" true true))))
  (testing "creates a computer player that goes second"
    (is (= sample-computer (create-player "o" false false)))))

(deftest stub-human-first-test
  (testing "creates a human player with no marker that goes first"
    (is (= {:marker "" :human true :goes-first true} (stub-human-first)))))

(deftest is-human-test
  (testing "returns true when player is human"
    (is (= true (is-human? sample-human))))
  (testing "returns false when player is computer"
    (is (= false (is-human? sample-computer)))))

(deftest goes-first-test
  (testing "returns true when player goes first"
    (is (= true (goes-first? sample-human))))
  (testing "returns false when player does not go first"
    (is (= false (goes-first? sample-computer)))))
