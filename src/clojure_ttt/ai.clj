(ns clojure-ttt.ai
  (:require [clojure-ttt.board :as board]))

(declare explore-and-score-moves)
(def depth 0)

; http://neverstopbuilding.com/minimax

(defn change-turn [markers]
  [(second markers) (first markers)])

(defn current-player-marker [players]
  (first players))

(defn next-player-marker [players]
  (second players))

(defn ai? [player ai-marker]
  (= player ai-marker))

(defn ai-win? [board ai-marker]
  (= (board/get-winner board) ai-marker))

(defn opponent-win? [board ai-marker]
  (not= ai-marker (board/get-winner board)))

(defn calculate-game-score [board ai-marker depth]
  (cond
    (ai-win? board ai-marker) (- 10 depth)
    (opponent-win? board ai-marker) (- depth 10)
    :else 0))

(defn best-move-and-score [current-player-marker ai-marker moves-and-scores]
  (if (ai? current-player-marker ai-marker)
      (apply max-key val moves-and-scores)
      (apply min-key val moves-and-scores)))

(defn best-score [current-player ai-marker moves-and-scores]
  (val (best-move-and-score current-player ai-marker moves-and-scores)))

(defn best-move [current-player ai-marker moves-and-scores]
  (key (best-move-and-score current-player ai-marker moves-and-scores)))

(defn get-score [board ai-marker players depth]
  (if (board/game-over? board)
      (calculate-game-score board ai-marker depth)
      (best-score (next-player-marker players) ai-marker
        (explore-and-score-moves board ai-marker (change-turn players) (inc depth)))))

(def speed-scoring (memoize get-score))

(defn explore-and-score-moves [board ai-marker players depth]
  (let [possible-moves (board/empty-spaces board)
        current-marker (current-player-marker players)
        scores (map #(speed-scoring (board/mark-space board % current-marker) ai-marker players depth) possible-moves)]
    (zipmap possible-moves scores)))

(defn get-ai-move [ai-marker board players]
  (let [scored-moves (explore-and-score-moves board ai-marker players depth)
        player (current-player-marker players)]
    (best-move player ai-marker scored-moves)))
