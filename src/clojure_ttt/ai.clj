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

(defn best-move-and-score [is-ai moves-and-scores]
  (if is-ai
      (apply max-key val moves-and-scores)
      (apply min-key val moves-and-scores)))

(defn best-score [is-ai moves-and-scores]
  (val (best-move-and-score is-ai moves-and-scores)))

(defn best-move [is-ai moves-and-scores]
  (key (best-move-and-score is-ai moves-and-scores)))

(defn update-alpha [is-ai score alpha]
  (if is-ai (max score alpha) alpha))

(defn update-beta [is-ai score beta]
  (if (not is-ai) (min score beta) beta))

(defn prune? [alpha beta]
  (>= alpha beta))

(defn stop-search? [boards alpha beta]
  (or (prune? alpha beta) (empty? boards)))

(defn generate-next-boards [board marker]
  (let [spaces (board/empty-spaces board)
        boards (map #(board/mark-space board % marker) spaces)]
    (map #(assoc {} :board %1 :move %2) boards spaces)))

(declare think-fast examine-boards-fast)

(defn return-score-or-move [depth moves-and-scores is-ai]
  (if (= depth 0)
      (best-move is-ai moves-and-scores)
      (best-score is-ai moves-and-scores)))

; maybe it's stopping too soon??
(defn examine-boards
  ([boards players depth alpha beta is-ai ai-marker]
    (examine-boards-fast {} boards players depth alpha beta is-ai ai-marker))
  ([moves-and-scores boards players depth alpha beta is-ai ai-marker]
    (let [board (:board (first boards))
          move (:move (first boards))
          new-score (think-fast board ai-marker (change-turn players) (inc depth) alpha beta (not is-ai))
          new-alpha (update-alpha is-ai new-score alpha)
          new-beta (update-beta is-ai new-score beta)
          new-moves-and-scores (assoc moves-and-scores move new-score)]
      (if (stop-search? (rest boards) new-alpha new-beta)
          new-moves-and-scores
          (recur new-moves-and-scores (rest boards) players depth new-alpha new-beta is-ai ai-marker)))))

(defn think
  ([board ai-marker players] (think-fast board ai-marker players 0 -100 100 true))
  ([board ai-marker players depth alpha beta is-ai]
    (if (board/game-over? board)
        (score-game board ai-marker depth)
        (let [player (current-player-marker players)
              boards (generate-next-boards board player)]
            (return-score-or-move depth (examine-boards boards players depth alpha beta is-ai ai-marker) is-ai)))))

(def think-fast (memoize think))
(def examine-boards-fast (memoize examine-boards))
