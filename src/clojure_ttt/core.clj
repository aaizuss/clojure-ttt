(ns clojure-ttt.core
  (:gen-class))

(defn new-board []
  (into {} (for [space (range 9) value [{:marked false, :mark nil}]] [space value])))

(defn marked? [board space]
  (get-in board [space :marked]))

(defn mark-space [board space marker]
  (assoc-in board [space] {:marked true :mark marker}))

(defn remove-marker [board space]
  "unmark the board at space (necessary for minimax algorithm)"
  (assoc-in board [space] {:marked false :mark nil}))

(defn empty-spaces [board]
  (keep (fn [[space-index space-info]] (if-not (:marked space-info) space-index)) board))

(defn empty-spaces-alt [board]
  (for [[space-index space-info] board :when (not (:marked space-info))] space-index))

(defn full? [board]
  (= 0 (count (empty-spaces board))))

(defn rows [board]
  (mapv vec (partition 3 (into (sorted-map) board))))

(defn columns [board]
  (apply mapv vector (rows board)))

  ; columns
  ; diagonals
  ; winner
  ; has winner?
  ; game over?
  ; tied?
