(ns clojure-ttt.board
  (:gen-class))

(def empty-space :_)

(def board-dimension 3)

(def num-spaces (* board-dimension board-dimension))

(defn new-board []
  (into (sorted-map) (for [space (range num-spaces) value [empty-space]] [space value])))

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

(defn columns [board]
  (apply mapv vector (rows board)))

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

(defn tie? [board]
  (and (full? board) (not (has-winner? board))))
