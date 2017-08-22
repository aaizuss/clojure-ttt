(ns clojure-ttt.game
  (:require [clojure-ttt.view :as view]
            [clojure-ttt.board :as board]
            [clojure-ttt.io :as io]
            [clojure-ttt.string-renderer :as renderer]
            [clojure-ttt.player :as player]))

(def game-options {:1 :human-v-human :2 :human-v-cpu :3 :cpu-v-human})

(defn stub-players [game-type]
  (cond
    (= :human-v-human game-type)
      {:current-player (player/stub-human-first)
       :opponent (player/stub-human-second)}
    (= :human-v-cpu game-type)
      {:current-player (player/stub-human-first)
       :opponent (player/stub-computer-second)}
    (= :cpu-v-human game-type)
      {:current-player (player/stub-computer-first)
       :opponent (player/stub-human-second)}
    :else "this should never happen")) ; raise exception here?

(defn setup-players [game-options]
  (let [game-type (view/get-game-selection game-options)
        incomplete-players (stub-players game-type)
        p1-marker (view/get-marker)
        p2-marker (view/get-marker :order-num 2 :opponent-marker p1-marker)
        player1 (assoc (:current-player incomplete-players) :marker p1-marker)
        player2 (assoc (:opponent incomplete-players) :marker p2-marker)]
      {:current-player player1 :opponent player2}))

(defn setup-game [game-options]
  (let [players (setup-players game-options)]
    (assoc players :board (board/new-board))))

(defn show-board-before-move [board current-player]
  (io/show (renderer/render-board board))
  (io/show (renderer/turn-message (player/get-marker current-player))))

; want to edit this so the message is special depending on game type
(defn show-game-over [board]
  (io/show (renderer/render-board board))
  (if (board/tie? board)
      (io/show (renderer/tie-message))
      (let [winner (board/get-winner board)]
        (io/show (renderer/win-message winner)))))

; TODO: move this function - this is temporary until i write an ai
(defn random-space [empty-spaces]
  (rand-nth empty-spaces))

(defn get-player-move [player board]
  (if (player/is-human? player)
      (view/get-move board)
      (random-space (board/empty-spaces board)))) ;; will use minimax

(defn game-loop [{:keys [board current-player opponent]}]
  (show-board-before-move board current-player)
  (let [current-player-marker (player/get-marker current-player)
        opponent-marker (player/get-marker opponent)
        move (get-player-move current-player board)
        marked-board (board/mark-space board move current-player-marker)]
      (if (board/game-over? marked-board)
          (show-game-over marked-board)
          (recur
            {:board marked-board
             :current-player opponent
             :opponent current-player}))))

(defn play []
  (io/show renderer/welcome)
  (game-loop (setup-game game-options)))
