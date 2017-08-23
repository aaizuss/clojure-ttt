(ns clojure-ttt.player)

(defn create-player [marker is-human goes-first]
  {:marker marker :human is-human :goes-first goes-first})

(defn stub-human-first []
  (create-player "" true true))

(defn stub-computer-first []
  (create-player "" false true))

(defn stub-human-second []
  (create-player "" true false))

(defn stub-computer-second []
  (create-player "" false false))

(defn is-human? [player]
  (:human player))

(defn is-computer? [player]
  (not (:human player)))

(defn goes-first? [player]
  (:goes-first player))

(defn get-marker [player]
  (:marker player))
