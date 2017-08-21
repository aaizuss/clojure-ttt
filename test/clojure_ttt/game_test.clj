(ns clojure-ttt.game-test
  (:require [clojure.test :refer :all]
            [clojure-ttt.game :refer :all]
            [clojure-ttt.board :as board]))

(def blank-board (board/new-board))

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
       "Enter a number 0-8 to make a move: "
       "\n\n x | 1 | o "
       "\n--- --- --- \n "
       "3 | o | 5 "
       "\n--- --- --- \n "
       "o | x | x \n\n"))

(deftest setup-players-test
  (testing "returns a map with current-player and opponent"
    (let [player-map
        (with-in-str "x\no" (setup-players))]
      (is (= "x" (:current-player player-map)))
      (is (= "o" (:opponent player-map))))))

(deftest game-loop-test
  (testing "halts when o wins and displays message that o wins"
    (is (= (str before-win-message "o wins!\n")
        (with-out-str (with-in-str "6" (game-loop
                                          {:board o-close-to-win
                                            :current-player "o"
                                            :opponent "x"})))))))
