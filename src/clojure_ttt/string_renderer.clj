(ns clojure-ttt.string-renderer
  (:require [clojure.string :as string]
            [clojure-ttt.board :as board]))

; is it better to just use the literal "--- --- ---"
(defn wrap-newline [s]
  (str "\n" s "\n"))

(defn row-divider []
  (let [part (str (apply str (take 3 (repeat "-"))) " ")
        parts (take 3 (repeat part))]
      (wrap-newline (apply str parts))))

(defn render-space [board index]
  (let [marker (board index)]
    (if (= marker board/empty-space)
      (str " " index " ")
      (str " " marker " "))))


; (defn render-spaces [board]
;   (for [[space value] board :when (if (= empty-space value) )] space))
;
;
; ;  0 | 1 | 2
; ; ---*---*---
; ;  3 | 4 | 5
; ; ---*---*---
; ;  6 | 7 | 8
; ;
; ;  0 | 1 | 2
; ; --- --- ---
; ;  3 | 4 | 5
; ; --- --- ---
; ;  6 | 7 | 8
; ;
