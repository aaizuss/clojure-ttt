(ns clojure-ttt.io)

(defn show [message]
  (println message))

(defn prompt [message]
  (println message) (flush) (read-line))
