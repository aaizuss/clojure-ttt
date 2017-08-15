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
  (mapv vec (partition 3 board)))

(defn columns [board]
  (apply mapv vector (rows board)))

(defn diagonal-top-left [board]
  (vec (take 3 (take-nth 4 board))))

(defn diagonal-top-right [board]
  (vec (select-keys board [2 4 6])))

(defn diagonals [board]
  [(diagonal-top-left board) (diagonal-top-right board)])

(defn- consecutive-marks? [row]
  (let [mark-status (map :marked (vals row))]
    (every? true? mark-status)))

(defn winner-on-row? [board row-index]
  (let [row (apply assoc {} (interleave (flatten ((rows board) row-index))))]
    (if (consecutive-marks? row) (apply = (map :mark (vals row))) false)))

(defn row-winner? [board]
  (loop [row-index 0]
    (cond
      (> row-index 2) false ; try not to rely on literals!
      (winner-on-row? board row-index) true
      :else (recur (inc row-index)))))

; nearly the same as winner-on-row?
; later: refactor to has-winner?
(defn winner-on-column? [board col-index]
  (let [column (apply assoc {} (interleave (flatten ((columns board) col-index))))]
    (if (consecutive-marks? column) (apply = (map :mark (vals column))) false)))

; nearly same as row-winner?
(defn column-winner? [board]
  (loop [col-index 0]
    (cond
      (> col-index 2) false
      (winner-on-column? board col-index) true
      :else (recur (inc col-index)))))

(defn winner-on-diag? [board diag-index]
  (let [diag (apply assoc {} (interleave (flatten ((diagonals board) diag-index))))]
    (if (consecutive-marks? diag) (apply = (map :mark (vals diag))) false)))

(defn diag-winner? [board]
  (loop [diag-index 0]
    (cond
      (> diag-index 1) false ; this is awful
      (winner-on-diag? board diag-index) true
      :else (recur (inc diag-index)))))
