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
  (utils/in-range? space 0 board/num-spaces))

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

;; very similar function in string-renderer
;; because i have specific messages there...
(defn valid-marker? [input-mark opponent-mark]
  (and (= 1 (count input-mark))
       (= 1 (count (re-matches #"^[a-zA-Z]$" input-mark)))
       (not (= input-mark opponent-mark))))
