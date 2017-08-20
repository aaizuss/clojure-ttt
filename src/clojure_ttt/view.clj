(ns clojure-ttt.view
  (:require [clojure-ttt.string-renderer :as renderer]
            [clojure-ttt.io :as io]
            [clojure-ttt.validator :as validator]
            [clojure-ttt.utilities :as utils]))

; is this silly?
(defn show-board [board]
  (io/show (renderer/render-board board)))

(defn show-welcome []
  (io/show renderer/welcome))

(defn get-marker
  [& {:keys [order-num opponent-marker] :or {order-num 1 opponent-marker ""}}]
    (let [marker (io/prompt (renderer/marker-selection order-num))]
      (if (validator/valid-marker? marker opponent-marker)
           marker
           (do
             (io/show (renderer/invalid-marker-msg marker opponent-marker))
             (recur {:order-num order-num :opponent-marker opponent-marker})))))

(defn get-move [board]
  (let [move (io/prompt renderer/choose-space)]
    (if (validator/valid-move? board move)
        (utils/to-num move)
        (do
          (io/show renderer/invalid-move)
          (recur board)))))
