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

; thought the below would be helpful - apparently not
; (defn get-marked-spaces [board]
;   (for [[space info] board :when (:marked info)] {space info}))
;
; (defn get-spaces-with-mark [board mark]
;   (apply assoc {} (interleave (flatten (for [[space info] board :when (= (:mark info) mark)] [space info])))))

; helper for row-winner
(defn row-marked? [row]
  (let [mark-status (map :marked (vals row))]
    (every? true? mark-status)))

; design consideration: might be better to rename func and return the actual mark
; todo: rewrite this so i can map it over result of (rows board)
; or rewrite (rows board) to return a more useful result and rewrite this
(defn row-winner? [board row-index]
  (let [row (apply assoc {} (interleave (flatten ((rows board) row-index))))]
    (if (row-marked? row) (apply = (map :mark (vals row))) false)))

; not sure if this is more confusing than above (just used an extra let)
; (defn row-winner? [board row-index]
;   (let [row (apply assoc {} (interleave (flatten ((rows board) row-index))))]
;     (if (row-marked? row)
;       (let [marks (map :mark (vals row))]
;         (apply = marks))
;         false)))
