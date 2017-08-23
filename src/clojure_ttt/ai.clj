(ns clojure-ttt.ai
  (:require [clojure-ttt.board :as board]
            [clojure-ttt.game :as game]
            [clojure-ttt.player :as player]))

(declare explore-and-score-moves)
(def depth 0)

(defn human-win? [board players]
  (player/is-human? (game/winning-player-from-marker board players)))

(defn ai-win? [board players]
  (player/is-computer? (game/winning-player-from-marker board players)))

; only call this when the game is over
(defn calculate-score [board players depth]
  (cond
    (ai-win? board players) (- 10 depth)
    (human-win? board players) (- depth 10)
    :else 0))

; moves-and-scores looks like {space score, space score, etc}
(defn best-move-and-score [player moves-and-scores]
  (if (player/is-computer? player)
      (apply max-key val moves-and-scores)
      (apply min-key val moves-and-scores)))

(defn best-score [player moves-and-scores]
  (val (best-move-and-score player moves-and-scores)))

(defn best-move [player moves-and-scores]
  (key (best-move-and-score player moves-and-scores)))

(defn get-score [board players depth]
  (if (board/game-over? board)
      (calculate-score board players depth)
      (best-score (:opponent players)
        (explore-and-score-moves board (game/swap-player-order players) (inc depth)))))

(def speed-scoring (memoize get-score))

(defn explore-and-score-moves [board players depth]
  (let [possible-moves (board/empty-spaces board)
        current-marker (player/get-marker (:current-player players))
        ; iterate over moves and mark board
        scores (map #(speed-scoring (board/mark-space board % current-marker) players depth) possible-moves)]
    (zipmap possible-moves scores)))

(defn get-ai-move [board players]
  (let [scored-moves (explore-and-score-moves board players depth)
        player (:current-player players)]
    (best-move player scored-moves)))
