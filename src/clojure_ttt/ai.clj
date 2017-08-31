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

; depth is like number of turns
; but i start with depth being number of spaces left - smaller as game goes on
; more turns should be lower score, so we add depth
; fewer turns should be better score (higher)
; so higher depth should be higher score (high depth means fewer turns when we decrease depth)
; each time invoke minimax, depth decremented by one
; needs to be 0 sum!!
(defn score-game [board ai-marker depth]
  (cond
    (ai-win? board ai-marker) (+ 10 depth)
    (opponent-win? board ai-marker) (- (+ 10 depth))
    :else 0))

(defn init-move-and-score [is-ai]
  (if is-ai [-1 -1000] [-1 1000]))

(defn update-alpha [is-ai best-move-and-score alpha]
  (if is-ai (max (second best-move-and-score) alpha) alpha))

(defn update-beta [is-ai best-move-and-score beta]
  (if (not is-ai) (min (second best-move-and-score) beta) beta))

(defn prune? [alpha beta]
  (>= alpha beta))

(defn stop-search? [possibilities alpha beta]
  (or (prune? alpha beta) (empty? possibilities)))

(defn ai-build-move-score-pair [best-move-and-score minimax-result possible-move]
  (let [best-score (second best-move-and-score)
        minimax-score (second minimax-result)]
    (if (> best-score minimax-score)
        best-move-and-score
        (vector possible-move minimax-score))))

(defn opponent-build-move-score-pair [best-move-and-score minimax-result possible-move]
  (let [best-score (second best-move-and-score)
        minimax-score (second minimax-result)]
    (if (< best-score minimax-score)
        best-move-and-score
        (vector possible-move minimax-score))))

(defn update-best-move-score [is-ai best-move-and-score minimax-result possible-move]
  (if is-ai
    (ai-build-move-score-pair best-move-and-score minimax-result possible-move)
    (opponent-build-move-score-pair best-move-and-score minimax-result possible-move)))

(declare fast-minimax)

; it always chooses 8 for its first move (when it goes second) and i don't know why
; when c goes second, it chooses 8, which means it will lose
(defn minimax [board depth players is-ai ai-marker alpha beta]
  (if (or (= depth 0) (board/game-over? board))
      [-1 (score-game board ai-marker depth)]
      (do
        (loop [[space & rest] (board/empty-spaces board)
                best-move-score (init-move-and-score is-ai)
                alpha alpha
                beta beta]
          (let [marked-board (board/mark-space board space (current-player-marker players))
                minimax-move-score (fast-minimax marked-board (dec depth) (change-turn players) (not is-ai) ai-marker alpha beta)
                new-move-and-score (update-best-move-score is-ai best-move-score minimax-move-score space)
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
