(ns clojure-ttt.view
  (:require [clojure-ttt.string-renderer :as renderer]
            [clojure-ttt.io :as io]
            [clojure-ttt.validator :as validator]
            [clojure-ttt.utilities :as utils]))

(defn invalid-marker-msg [input-marker opponent-marker]
  (cond
    (> (count input-marker) 1)
      (renderer/invalid-mark-too-long input-marker)
    (not (re-matches #"^[a-zA-Z]$" input-marker))
      (renderer/invalid-mark-special-char input-marker)
    (= input-marker opponent-marker)
      (renderer/invalid-mark-already-taken input-marker)
    :else (renderer/invalid-mark input-marker)))

(defn get-marker
  [& {:keys [order-num opponent-marker] :or {order-num 1 opponent-marker ""}}]
    (let [marker (io/prompt (renderer/marker-selection order-num))]
      (if (validator/valid-marker? marker opponent-marker)
           marker
           (do
             (io/show (invalid-marker-msg marker opponent-marker))
             (recur {:order-num order-num :opponent-marker opponent-marker})))))

(defn get-move [board]
  (let [move (io/prompt renderer/choose-space)]
    (if (validator/valid-move? board move)
        (utils/to-num move)
        (do
          (io/show renderer/invalid-move-msg)
          (recur board)))))

(defn get-game-selection [game-options]
  (let [selection (io/prompt renderer/game-selection-msg)]
    (if (validator/valid-game-selection? selection game-options)
        ((keyword selection) game-options)
        (do
          (io/show renderer/invalid-choice-msg)
          (recur game-options)))))
