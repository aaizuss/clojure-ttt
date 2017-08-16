(ns clojure-ttt.board
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

(defn winner-on-column? [board col-index]
  (let [column (apply assoc {} (interleave (flatten ((columns board) col-index))))]
    (if (consecutive-marks? column) (apply = (map :mark (vals column))) false)))

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
      (> diag-index 1) false ; need to figure out a better way...
      (winner-on-diag? board diag-index) true
      :else (recur (inc diag-index)))))

(defn has-winner? [board]
  (or (row-winner? board) (column-winner? board) (diag-winner? board)))

(defn tie? [board]
  (and (full? board) (not (has-winner? board))))

;;;;;;;;;;; printing - will move to a different namespace ;;;;;;;;;;;
;; note to tom: i've just been testing thee in the repl - i have a lot to figure out

(defn get-row [board row-index]
  (apply assoc {} (interleave (flatten ((rows board) row-index)))))

; {6 {:marked false, :mark nil}, 7 {:marked false, :mark nil}, 8 {:marked true, :mark "o"}} print this!
; for key in map, if :marked is false, add key to string. else add :mark.
; then interpose the string with " | "
; (for [[space-index space-info] board :when (not (:marked space-info))] space-index))

; map this over the row
(defn render-space [row position] ; change row to board?
  (let [info (row position)
        marked (:marked info)]
    (if marked (:mark info) (str position))))

(defn indices-for-row [row-index]
  (get (vec (map vec (partition 3 (range 9))))) row-index)


(defn row-divider []
  (let [part (str (apply str (take 3 (repeat "-"))) " ")
        parts (take 3 (repeat part))]
      (apply str parts)))


;  0 | 1 | 2
; ---*---*---
;  3 | 4 | 5
; ---*---*---
;  6 | 7 | 8
;
;  0 | 1 | 2
; --- --- ---
;  3 | 4 | 5
; --- --- ---
;  6 | 7 | 8
;
