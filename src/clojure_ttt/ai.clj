(ns clojure-ttt.ai
  (:require [clojure-ttt.board :as board]))

(declare think think-fast)
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

(defn generate-next-boards [board marker]
  (let [spaces (board/empty-spaces board)
        boards (map #(board/mark-space board % marker) spaces)]
    (map #(assoc {} :board %1 :move %2) boards spaces)))

(defn examine-board-states-max [boards moves-and-scores players depth alpha beta ai-marker is-ai]
  (let [new-players (change-turn players)
        board (:board (first boards))
        move (:move (first boards))
        new-score (think-fast {:board board
                          :depth (inc depth)
                          :alpha alpha
                          :beta beta
                          :players new-players
                          :is-ai false
                          :ai-marker ai-marker})
        new-alpha (max alpha new-score)
        new-moves-and-scores (assoc moves-and-scores move new-score)]
    (if (or (<= beta new-alpha) (empty? (rest boards)))
        new-moves-and-scores ;short circuit
        ; continue working through the possible board states
        (recur (rest boards) new-moves-and-scores players depth new-alpha beta ai-marker is-ai))))

(defn examine-board-states-min [boards moves-and-scores players depth alpha beta ai-marker is-ai]
  (let [new-players (change-turn players)
        board (:board (first boards))
        move (:move (first boards))
        new-score (think-fast {:board board :players new-players
                          :depth (inc depth)
                          :alpha alpha :beta beta :is-ai true :ai-marker ai-marker})
        new-beta (min beta new-score)
        new-moves-and-scores (assoc moves-and-scores move new-score)]
    (if (or (<= new-beta alpha) (empty? (rest boards)))
        new-moves-and-scores
        (recur (rest boards) new-moves-and-scores players depth alpha new-beta ai-marker is-ai))))

(defn build-moves-and-scores
  [{:keys [moves-and-scores boards players depth alpha beta is-ai ai-marker]
    :or {moves-and-scores {}}}]
    (if is-ai
      (examine-board-states-max boards moves-and-scores players depth alpha beta ai-marker is-ai)
      (examine-board-states-min boards moves-and-scores players depth alpha beta ai-marker is-ai)))

(defn best-move-and-score [player ai-marker moves-and-scores]
  (if (ai? player ai-marker)
      (apply max-key val moves-and-scores)
      (apply min-key val moves-and-scores)))

(defn best-score [player ai-marker moves-and-scores]
  (val (best-move-and-score player ai-marker moves-and-scores)))

(defn best-move [player ai-marker moves-and-scores]
  (key (best-move-and-score player ai-marker moves-and-scores)))

(defn get-score-or-move [depth player ai-marker moves-and-scores]
  (if (= 0 depth)
      (best-move player ai-marker moves-and-scores)
      (best-score player ai-marker moves-and-scores)))

; i think the problem is the way i analyze the moves-and-scores/how i recur but
; i can't figure it out :(
(defn think [{:keys [board ai-marker depth alpha beta players is-ai]
                :or {depth 0, alpha -1000, beta 1000, is-ai true}}]
  (if (or (> depth max-depth) (board/game-over? board))
      (score-game board ai-marker depth)
      (let [player (current-player-marker players)
            boards (generate-next-boards board player)
            moves-and-scores (build-moves-and-scores
                              {:boards boards
                               :players players
                               :depth depth
                               :is-ai is-ai
                               :ai-marker ai-marker
                               :alpha alpha
                               :beta beta})]
        ;(println (str "depth: " depth " player " player "\nmoves-and-scores: " moves-and-scores))
        (get-score-or-move depth player ai-marker moves-and-scores))))

(def think-fast (memoize think))
