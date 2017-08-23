(ns clojure-ttt.ai-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.ai :refer :all]
            [clojure-ttt.board :as board]))

(def sample-human
  {:marker "x" :human true :goes-first true})

(def sample-computer
  {:marker "c" :human false :goes-first false})

(def sample-players
  {:current-player sample-computer
   :opponent sample-human})

(def x-almost-win
 (into (sorted-map)
   {0 "x" 1 "x" 2 :_
    3 :_ 4 "c" 5 :_
    6 :_ 7 :_ 8 :_}))

(def c-almost-win
 (into (sorted-map)
   {0 "x" 1 :_ 2 :_
    3 "c" 4 "c" 5 :_
    6 :_ 7 :_ 8 :_}))

(deftest get-ai-move-test
  (testing "blocks opponent from winning"
    (is (= 2 (get-ai-move x-almost-win sample-players)))))
