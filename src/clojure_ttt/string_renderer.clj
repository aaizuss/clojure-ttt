(ns clojure-ttt.string-renderer
  (:require [clojure.string :as string]
            [clojure-ttt.board :as board]))

(defn wrap-newline [s]
  (str "\n" s "\n"))

(defn row-divider []
  (let [part (str (apply str (take 3 (repeat "-"))) " ")
        parts (take 3 (repeat part))]
      (wrap-newline (apply str parts))))


(defn board-to-indexed-vec [board]
  (vec (interleave board)))

(defn render-space [[index marker]]
  (if (= marker board/empty-space)
      (str " " index " ")
      (str " " marker " ")))

(defn string-list-from-board [board]
  (let [board-vector (board-to-indexed-vec board)]
    (map render-space board-vector)))


(defn board-to-string [board]
  (let [board-vector (board-to-indexed-vec board)]
    (apply str (map render-space board-vector))))

(defn row-strings [board]
  (let [board-string-list (string-list-from-board board)
        partitioned (partition 3 board-string-list)
        rows (map (fn [item] (interpose "|" item)) partitioned)
        divider (list (row-divider))]
      (interpose divider rows)))

(defn render-board [board]
  (let [pieces (row-strings board)]
    (apply str (flatten pieces))))
