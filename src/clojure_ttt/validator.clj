(ns clojure-ttt.validator
  (:require [clojure-ttt.board :as board]
            [clojure-ttt.utilities :as utils]))

(defn is-num? [input]
  (try
    (Integer/parseInt input)
    true
  (catch Exception e false)))

(defn valid-board-position? [board space]
  (and (is-num? space)
       (board/space-exists? board (utils/to-num space))))

(defn valid-move? [board proposed-move]
  (and
    (valid-board-position? board proposed-move)
    (= false (board/marked? board (utils/to-num proposed-move)))))

(defn valid-marker? [input-mark opponent-mark]
  (and (= 1 (count input-mark))
       (= 1 (count (re-matches #"^[a-zA-Z]$" input-mark)))
       (not (= input-mark opponent-mark))))

(defn valid-game-selection? [input game-options]
  (= true (some #(= input %) (map name (keys game-options)))))
