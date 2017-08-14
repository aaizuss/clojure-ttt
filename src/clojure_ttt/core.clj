(ns clojure-ttt.core
  (:gen-class))

(defn new-board []
  (into {} (for [space (range 9) v [{:marked false, :mark nil}]] [space v])))

(defn marked? [board space]
  (get-in board [space :marked]))

(defn mark-space [board space marker]
  (assoc-in board [space] {:marked true :mark marker}))

(defn remove-marker [board space]
  "unmark the board at space (necessary for minimax algorithm)"
  (assoc-in board [space] {:marked false :mark nil}))

(defn valid-spaces [board]
  (keep (fn [[space-index space-info]] (if-not (:marked space-info) space-index)) board))

(defn valid-spaces-alt [board]
  (for [[space-index space-info] board :when (not (:marked space-info))] space-index))
