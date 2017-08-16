(ns clojure-ttt.board
  (:gen-class))

(def empty-space :_)

(defn new-board []
  (into (sorted-map) (for [space (range 9) value [empty-space]] [space value])))

(defn marked? [board space]
  (not (= (get board space) empty-space)))

(defn mark-space [board space marker]
  (assoc board space marker))

(defn empty-spaces [board]
  (for [[space value] board :when (= empty-space value)] space))

(defn full? [board]
  (= 0 (count (empty-spaces board))))

(defn rows [board]
  (into [] (partition 3 (vals board))))

; (defn indexed-rows [board]
;   (map-indexed vector (rows board)))

(defn columns [board]
  (apply mapv vector (rows board)))

; (defn indexed-columns [board]
;   (map-indexed vector (columns board)))
;
(defn diagonal-top-left [board]
  (vals (select-keys board [0 4 8])))

(defn diagonal-top-right [board]
  (vals (select-keys board [2 4 6])))

(defn diagonals [board]
  [(diagonal-top-left board) (diagonal-top-right board)])

(defn in-a-row? [row]
  (let [mark-status (map (fn [space] (= space empty-space)) row)
        all-marked (every? false? mark-status)]
    (and all-marked (apply = row))))

(defn all-rows [board]
  (let [rows (rows board)
        columns (columns board)
        diagonals (diagonals board)]
    (into [] (concat rows columns diagonals))))

; todo: figure out what is the most efficient and cleanest
; way to check if there is a winner
(defn- find-first
         [f collection]
         (first (filter f collection)))

; move this to another namespace (might be useful when returning winner)
(defn winning-row [board]
  (find-first in-a-row? (all-rows board)))

(defn has-winner? [board]
  (let [rows (all-rows board)
        results (filter in-a-row? rows)]
      (> (count results) 0)))


; (defn tie? [board]
;   (and (full? board) (not (has-winner? board))))
;
; ;;;;;;;;;;; printing - will move to a different namespace ;;;;;;;;;;;
; ;; note to tom: i've just been testing thee in the repl - i have a lot to figure out
;
; (defn get-row [board row-index]
;   (apply assoc {} (interleave (flatten ((rows board) row-index)))))
;
; ; {6 {:marked false, :mark nil}, 7 {:marked false, :mark nil}, 8 {:marked true, :mark "o"}} print this!
; ; for key in map, if :marked is false, add key to string. else add :mark.
; ; then interpose the string with " | "
; ; (for [[space-index space-info] board :when (not (:marked space-info))] space-index))
;
; ; map this over the row
; (defn render-space [row position] ; change row to board?
;   (let [info (row position)
;         marked (:marked info)]
;     (if marked (:mark info) (str position))))
;
; (defn indices-for-row [row-index]
;   (get (vec (map vec (partition 3 (range 9))))) row-index)
;
;
; (defn row-divider []
;   (let [part (str (apply str (take 3 (repeat "-"))) " ")
;         parts (take 3 (repeat part))]
;       (apply str parts)))
;
;
; ;  0 | 1 | 2
; ; ---*---*---
; ;  3 | 4 | 5
; ; ---*---*---
; ;  6 | 7 | 8
; ;
; ;  0 | 1 | 2
; ; --- --- ---
; ;  3 | 4 | 5
; ; --- --- ---
; ;  6 | 7 | 8
; ;
