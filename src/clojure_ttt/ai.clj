(ns clojure-ttt.ai
  (:require [clojure-ttt.board :as board]))

(def max-depth 7)

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

(defn score-game [board ai-marker depth]
  (cond
    (ai-win? board ai-marker) (- 10 depth)
    (opponent-win? board ai-marker) (- depth 10)
    :else 0))

(defn init-move-and-score [is-ai]
  (if is-ai [0 -1000] [0 1000]))

(defn update-alpha [is-ai best-move-and-score alpha]
  (if is-ai (max (second best-move-and-score) alpha) alpha))

(defn update-beta [is-ai best-move-and-score beta]
  (if (not is-ai) (min (second best-move-and-score) beta) beta))

(defn prune? [alpha beta]
  (>= alpha beta))

(defn stop-search? [boards alpha beta]
  (or (prune? alpha beta) (empty? boards)))

(defn generate-next-boards [board marker]
  (let [spaces (board/empty-spaces board)
        boards (map #(board/mark-space board % marker) spaces)]
    (map #(assoc {} :board %1 :move %2) boards spaces)))

(defn find-max-or-min
  [max-or-min is-ai current-best move-and-score possible-move]
    (if (max-or-min (second current-best) (second move-and-score))
      (vector possible-move (second move-and-score))
      current-best))

(defn best-move-and-score
  [is-ai current-best move-and-score possible-move]
    (if is-ai
      (find-max-or-min < is-ai current-best move-and-score possible-move)
      (find-max-or-min > is-ai current-best move-and-score possible-move)))

(declare fast-minimax)

(defn minimax [board depth players is-ai ai-marker alpha beta]
  (if (board/game-over? board)
      [0 (score-game board ai-marker depth)]
      (do
        (loop [;[board-state & rest-of-boards] (generate-next-boards board (current-player-marker players))
                [space & rest] (board/empty-spaces board)
                best-move-score (init-move-and-score is-ai)
                alpha alpha
                beta beta]
          (let [;the-board (:board board-state)
                the-board (board/mark-space board space (current-player-marker players))
                ;the-move (:move board-state)
                move-and-score (fast-minimax the-board (dec depth) (change-turn players) (not is-ai) ai-marker alpha beta)
                new-move-and-score (best-move-and-score is-ai best-move-score move-and-score space)
                new-alpha (update-alpha is-ai new-move-and-score alpha)
                new-beta (update-beta is-ai new-move-and-score beta)]
            (if (stop-search? rest new-alpha new-beta)
                new-move-and-score
                (recur rest new-move-and-score new-alpha new-beta)))))))

(def fast-minimax (memoize minimax))

(defn choose-move [ai-marker board players]
  (let [is-ai true
        depth (count (board/empty-spaces board))
        best-move-and-score (fast-minimax board depth players is-ai ai-marker -1000 1000)]
      (first best-move-and-score)))
