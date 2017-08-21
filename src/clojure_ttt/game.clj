(ns clojure-ttt.game
  (:require [clojure-ttt.view :as view]
            [clojure-ttt.board :as board]
            [clojure-ttt.io :as io]
            [clojure-ttt.string-renderer :as renderer]))

(defn setup-players []
  (let [p1-marker (view/get-marker)
        p2-marker (view/get-marker :order-num 2 :opponent-marker p1-marker)]
  {:current-player p1-marker :opponent p2-marker}))

(defn setup-game []
  (let [players (setup-players)]
  (assoc players :board (board/new-board))))

(defn show-board-before-move [board current-player]
  (io/show (renderer/render-board board))
  (io/show (renderer/turn-message current-player)))

(defn show-game-over [board current-player]
  (io/show (renderer/render-board board))
  (if (board/tie? board)
      (io/show (renderer/tie-message))
      (let [winner (board/get-winner board)]
        (io/show (renderer/win-message current-player)))))

(defn game-loop [{:keys [board current-player opponent]}]
  (show-board-before-move board current-player)
  (let [move (view/get-move board)
        marked-board (board/mark-space board move current-player)]
      (if (board/game-over? marked-board)
          (show-game-over marked-board current-player)
          (recur
            {:board marked-board
             :current-player opponent
             :opponent current-player}))))

(defn play []
  (view/show-welcome)
  (game-loop (setup-game)))
