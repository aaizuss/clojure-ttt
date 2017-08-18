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

(def welcome
  (str "|----------------------------|\n"
       "|-- Welcome to Tic Tac Toe --|\n"
       "|----------------------------|"))

(defn marker-selection [order]
  (str "Player " order ", enter a single letter for your mark: "))

(def choose-space "Enter a number 0-8 to make a move: ")

(defn invalid-mark [mark]
  (str mark " is an invalid mark. "))

(def invalid-move
  "You can't move there.")

(defn taken-space [space]
  (str "You can't move to " space ". It's taken! "))

(defn not-a-number [space]
  (str "You can't move to " space ". It's not on the board!"))

(defn turn-message [marker]
  (str "It is " marker "'s turn."))

(defn invalid-marker-msg [input-marker opponent-marker]
  (cond
    (= input-marker " ")
      "You must think you're very clever! Your mark cannot be a space."
    (> (count input-marker) 1)
      (str (invalid-mark input-marker) "Markers must be a single letter.")
    (not (re-matches #"^[a-zA-Z]$" input-marker))
      (str (invalid-mark input-marker) "You must choose a letter.")
    (= input-marker opponent-marker)
      (str (invalid-mark input-marker) "Your opponent already chose that marker.")
    :else "Your marker choice is invalid."))
