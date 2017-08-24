(ns clojure-ttt.string-renderer
  (:require [clojure.string :as string]
            [clojure-ttt.board :as board]
            [clojure.data.json :as json]))

(defn- read-json-file
  ([directory filename] (slurp (str directory "/" filename)))
  ([filename] (read-json-file "." filename)))

(defn- map-from-json-string [json-string]
  (json/read-str json-string :key-fn keyword))

(def string-map
  (let [json-string (read-json-file "string-config.json")]
    (map-from-json-string json-string)))

(defn wrap-newline [s]
  (str "\n" s "\n"))

(declare row-divider row-strings)

(defn render-board [board]
  (let [pieces (row-strings board)]
    (wrap-newline (apply str (flatten pieces)))))

(defn row-strings [board]
  (let [size (board/board-dimension board)
        board-string-list (board/to-string-list board)
        partitioned (partition size board-string-list)
        rows (map (fn [item] (interpose "|" item)) partitioned)
        divider (list (row-divider size))]
      (interpose divider rows)))

(defn row-divider [board-dimension]
  (let [part (str (apply str (take 4 (repeat "-"))) " ")
        parts (take board-dimension (repeat part))]
      (wrap-newline (apply str parts))))

(def welcome
  (wrap-newline (:welcome-msg string-map)))

(defn win-message [winning-mark]
  (str winning-mark (:win-msg string-map)))

(defn tie-message []
  (:tie-msg string-map))

(defn marker-selection [order]
  (str (:marker-selection-msg string-map) order "'s mark:"))

(def choose-space (:choose-space-msg string-map))

(defn invalid-mark [mark]
  (str mark (:invalid-mark-msg string-map)))

(def invalid-move-msg (:invalid-move-msg string-map))

(defn turn-message [mark]
  (str "It is " mark "'s turn."))

(defn invalid-mark-too-long [marker]
  (str (invalid-mark marker) (:invalid-mark-too-long string-map)))

(defn invalid-mark-special-char [marker]
  (str (invalid-mark marker) (:invalid-mark-special-char string-map)))

(defn invalid-mark-already-taken [marker]
  (str (invalid-mark marker) (:invalid-mark-already-taken string-map)))

(def game-selection-msg
  (wrap-newline (:game-selection-msg string-map)))

(def board-selection-msg
  (wrap-newline (:board-selection-msg string-map)))

(def invalid-choice-msg (:invalid-choice-msg string-map))

(defn move-history-msg
  ([marker space]
    (str marker (:move-history-msg string-map) space)))
