(ns clojure-ttt.string-renderer
  (:require [clojure.string :as string]
            [clojure-ttt.board :as board]))

; TODO: put strings in a config file, read it in

(declare row-divider row-strings)
(defn wrap-newline [s]
  (str "\n" s "\n"))

(defn render-board [board]
  (let [pieces (row-strings board)]
    (apply str (flatten pieces))))

(defn row-strings [board]
  (let [board-string-list (board/to-string-list board)
        partitioned (partition 3 board-string-list)
        rows (map (fn [item] (interpose "|" item)) partitioned)
        divider (list (row-divider))]
      (interpose divider rows)))

(defn row-divider []
  (let [part (str (apply str (take 3 (repeat "-"))) " ")
        parts (take 3 (repeat part))]
      (wrap-newline (apply str parts))))

(def welcome
  (str "|----------------------------|\n"
       "|-- Welcome to Tic Tac Toe --|\n"
       "|----------------------------|"))

(def win-message [winning-mark]
  (str winning-mark " wins!"))

(def tie-message []
  (str "Tie Game!"))

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

(defn invalid-mark-space [marker]
  "You must think you're very clever! Your mark cannot be a space.")

(defn invalid-mark-too-long [marker]
  (str (invalid-mark marker) "Markers must be a single letter."))

(defn invalid-mark-special-char [marker]
  (str (invalid-mark marker) "You must choose a letter."))

(defn invalid-mark-already-taken [marker]
  (str (invalid-mark marker) "Your opponent already chose that marker."))

(defn invalid-marker-msg [input-marker opponent-marker]
  (cond
    (= input-marker " ")
      (invalid-mark-space input-marker)
    (> (count input-marker) 1)
      (invalid-mark-too-long input-marker)
    (not (re-matches #"^[a-zA-Z]$" input-marker))
      (invalid-mark-special-char input-marker)
    (= input-marker opponent-marker)
      (invalid-mark-already-taken input-marker)
    :else "Your marker choice is invalid."))
