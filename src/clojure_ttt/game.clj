(ns clojure-ttt.game
  (:require [clojure-ttt.view :as view]
            [clojure-ttt.board :as board]
            [clojure-ttt.io :as io]
            [clojure-ttt.string-renderer :as renderer]
            [clojure-ttt.player :as player]
            [clojure-ttt.ai :as ai]))

(def game-options {:1 :human-v-human :2 :human-v-cpu :3 :cpu-v-human})
(def board-options {:3 :3x3 :4 :4x4})

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
    ; raise exception here?
    :else "this can't happen unless game-options names don't match"))

(defn new-board-from-choice [board-choice]
  (cond
    (= :4x4 board-choice) (board/new-board 4)
    (= :3x3 board-choice) (board/new-board 3)
    :else "this can't happen unless board-options names don't match"))

(defn setup-board [board-options]
  (let [board-choice (view/get-board-selection board-options)]
    (new-board-from-choice board-choice)))

(defn setup-players [game-options]
  (let [game-type (view/get-game-selection game-options)
        incomplete-players (stub-players game-type)
        p1-marker (view/get-marker)
        p2-marker (view/get-marker :order-num 2 :opponent-marker p1-marker)
        player1 (assoc (:current-player incomplete-players) :marker p1-marker)
        player2 (assoc (:opponent incomplete-players) :marker p2-marker)]
      {:current-player player1 :opponent player2}))

(defn player-markers [current-player opponent]
  [(player/get-marker current-player) (player/get-marker opponent)])

(defn setup-game [board-options game-options]
  (let [board (setup-board board-options)
        players (setup-players game-options)]
    (assoc players :board board :move-history [])))

(defn show-pre-move-info [board current-player opponent move-history]
  (io/show (renderer/render-board board))
  (if (empty? move-history)
    (io/show (renderer/turn-message (player/get-marker current-player)))
    (io/show (renderer/move-history-msg (player/get-marker opponent) (last move-history)))))

; want to edit this so the message is special depending on game type
(defn show-game-over [board]
  (io/show (renderer/render-board board))
  (if (board/tie? board)
      (io/show (renderer/tie-message))
      (let [winner (board/get-winner board)]
        (io/show (renderer/win-message winner)))))

(defn get-player-move [current-player opponent board]
  (let [player-markers (player-markers current-player opponent)
        current-player-marker (player/get-marker current-player)]
    (if (player/is-human? current-player)
        (view/get-move board)
        (ai/get-ai-move current-player-marker board player-markers))))

(defn game-loop [{:keys [board current-player opponent move-history]}]
  (show-pre-move-info board current-player opponent move-history)
  (let [current-player-marker (player/get-marker current-player)
        opponent-marker (player/get-marker opponent)
        move (get-player-move current-player opponent board)
        new-move-history (conj move-history move)
        marked-board (board/mark-space board move current-player-marker)]
      (if (board/game-over? marked-board)
          (show-game-over marked-board)
          (recur
            {:board marked-board
             :current-player opponent
             :opponent current-player
             :move-history new-move-history}))))

(defn play []
  (io/show renderer/welcome)
  (game-loop (setup-game board-options game-options)))
