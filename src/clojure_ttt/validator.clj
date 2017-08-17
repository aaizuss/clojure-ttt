(ns clojure-ttt.validator
  (:require [clojure-ttt.board :as board]
            [clojure-ttt.utilities :as utils]))

(defn is-num? [input]
  (try
    (Integer/parseInt input)
    true
  (catch Exception e false)))

; should probably be private...
(defn space-exists? [board space]
  (and (>= space 0) (< space board/num-spaces)))

; might be better to put a variation of this in board ns
(defn valid-board-position? [board space]
  (and (is-num? space)
       (space-exists? board (utils/input-to-num space))))

; could just check that it's in board/empty-spaces...
; but using is-num and space-exists is more efficient than getting all empty spaces...
; might want to refactor where i call utils/input-to-num
(defn valid-move? [board proposed-move]
  (and
    (valid-board-position? board proposed-move)
    (= false (board/marked? board (utils/input-to-num proposed-move)))))
