(ns clojure-ttt.core
  (:gen-class))

(defn new-board []
  (into (sorted-map) (into {} (for [space (range 9) value [{:marked false, :mark nil}]] [space value]))))

(defn marked? [board space]
  (get-in board [space :marked]))

(defn mark-space [board space marker]
  (assoc-in board [space] {:marked true :mark marker}))

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

(defn diagonal-top-left [board]
  (vec (take 3 (take-nth 4 board))))

(defn diagonal-top-right [board]
  (vec (select-keys board [2 4 6])))

(defn diagonals [board]
  [(diagonal-top-left board) (diagonal-top-right board)])

;; todo: use this to find if there are winners
;; this will be private
(defn get-marked-spaces [board]
  (for [[space info] board :when (:marked info)] [space info]))
