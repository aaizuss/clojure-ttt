(ns clojure-ttt.validator
  (:require [clojure-ttt.board :as board]))

(defn space-exists? [board space]
  (and (>= space 0) (< space board/num-spaces)))

; could just check that it's in board/empty-spaces...
; but this is more efficient than getting all empty spaces...
(defn valid-move? [board proposed-move]
  (and
    (space-exists? board proposed-move)
    (= false (board/marked? board proposed-move))))
