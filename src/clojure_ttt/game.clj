(ns clojure-ttt.game
  (:require [clojure-ttt.view :as view]
            [clojure-ttt.board :as board]
            [clojure-ttt.io :as io]
            [clojure-ttt.string-renderer :as renderer]))

(defn get-player-data []
  (let [p1-marker (view/get-marker)
        p2-marker (view/get-marker 2 p1-marker)]
      {:p1 p1-marker :p2 p2-marker}))


(defn game-loop [& {:keys [board current-player opponent]
                  :or {board (board/new-board) current-player "x" opponent "o"}}]
  (let [move (view/get-move board)
        marked-board (board/mark-space board move current-player)]
    (do
      (io/show (renderer/render-board board))
      (if (or (board/has-winner? marked-board) (board/tie? marked-board))
          (println "over")
          (recur {:board marked-board :current-player "o" :opponent "x"})))))
