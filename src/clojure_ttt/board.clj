(ns clojure-ttt.board
  (:gen-class))

(def empty-space :_)

(defn board-dimension [board]
  (int (Math/sqrt (count board))))

(defn num-spaces [board]
  (count board))

(defn new-board [board-dimension]
  (let [num-spaces (* board-dimension board-dimension)]
    (into (sorted-map)
      (for [space (range num-spaces) value [empty-space]] [space value]))))

(defn marked? [board space]
  (not (= (get board space) empty-space)))

(defn mark-space [board space marker]
  (assoc board space marker))

(defn empty-spaces [board]
  (into [] (for [[space value] board :when (= empty-space value)] space)))

(defn full? [board]
  (= 0 (count (empty-spaces board))))

(defn space-exists? [board space]
  (and (<= 0 space) (< space (num-spaces board))))

(defn rows [board]
  (into [] (partition (board-dimension board) (vals board))))

(defn columns [board]
  (apply mapv vector (rows board)))

(defn diagonal-top-left [board]
  (let [size (board-dimension board)
        end (num-spaces board)
        step (+ 1 size)
        keys (range 0 end step)]
    (vals (select-keys board keys))))

(defn diagonal-top-right [board]
  (let [size (board-dimension board)
        end (- (num-spaces board) 1)
        step (- size 1)
        keys (range step end step)]
    (vals (select-keys board keys))))

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

(defn to-indexed-vec [board]
  (vec (interleave board)))

(defn render-space [[index marker]]
  (if (= marker empty-space)
      (str " " index " ")
      (str " " marker " ")))

(defn to-string-list [board]
  (let [board-vector (to-indexed-vec board)]
    (map render-space board-vector)))

(defn- find-first [f collection]
  (first (filter f collection)))

(defn- winning-row [board]
  (find-first in-a-row? (all-rows board)))

(defn get-winner [board]
  (first (winning-row board)))

(defn has-winner? [board]
  (let [rows (all-rows board)
        results (filter in-a-row? rows)]
      (> (count results) 0)))

(defn tie? [board]
  (and (full? board) (not (has-winner? board))))

(defn game-over? [board]
  (or (has-winner? board) (tie? board)))
