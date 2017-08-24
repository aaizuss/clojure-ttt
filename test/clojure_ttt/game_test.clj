(ns clojure-ttt.game-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.game :refer :all]
            [clojure-ttt.player :as player]
            [clojure-ttt.board :as board]))

(def marked-board
  (into (sorted-map)
    {0 "x" 1 :_ 2 :_
     3 :_ 4 "o" 5 :_
     6 :_ 7 :_ 8 "x"}))

(def o-close-to-win
 (into (sorted-map)
   {0 "x" 1 :_ 2 "o"
    3 :_ 4 "o" 5 :_
    6 :_ 7 "x" 8 "x"}))

(def before-win-message
  (str "\n x | 1 | o \n--- --- --- \n "
       "3 | o | 5 \n--- --- --- \n "
       "6 | x | x \n\n" "It is o's turn.\n"
       "Enter a number 0-8 to mark that position on the board: "
       "\n\n x | 1 | o "
       "\n--- --- --- \n "
       "3 | o | 5 "
       "\n--- --- --- \n "
       "o | x | x \n\n"))

(def sample-human-p1
  {:marker "x" :human true :goes-first true})

(def sample-human-o
  {:marker "o" :human true :goes-first false})

(def sample-computer-p2
  {:marker "o" :human false :goes-first false})

(def sample-human-p2
  {:marker "x" :human true :goes-first false})

(def sample-computer-p1
  {:marker "o" :human false :goes-first true})

(def sample-human-v-computer
  {:current-player sample-human-p1
   :opponent sample-computer-p2})

(def sample-computer-v-human
  {:current-player sample-computer-p2
   :opponent sample-human-p1})

(deftest stub-players-test
  (testing "stubs players for human v computer"
    (is (= {:current-player {:marker "" :human true :goes-first true}
            :opponent {:marker "" :human false :goes-first false}}
            (stub-players :human-v-cpu)))))


(deftest setup-players-test
  (with-out-str
    (testing "returns players map for human v computer game"
      (let [player-map
          (with-in-str "2\nx\no" (setup-players game-options))]
        (is (= sample-human-p1 (:current-player player-map)))
        (is (= sample-computer-p2 (:opponent player-map))))))
  (with-out-str
    (testing "returns players map for computer v human game"
      (let [player-map
          (with-in-str "3\no\nx" (setup-players game-options))]
        (is (= sample-computer-p1 (:current-player player-map)))
        (is (= sample-human-p2 (:opponent player-map))))))
  (with-out-str
    (testing "returns players map for human v human game"
      (let [player-map
          (with-in-str "1\nx\no" (setup-players game-options))]
        (is (= sample-human-p1 (:current-player player-map)))
        (is (= sample-human-o (:opponent player-map)))))))

(deftest game-loop-test
  (testing "halts when o wins and displays message that o wins"
    (is (= (str before-win-message "o wins!\n")
        (with-out-str (with-in-str "6" (game-loop
                                          {:board o-close-to-win
                                            :current-player sample-human-o
                                            :opponent sample-human-p1})))))))
