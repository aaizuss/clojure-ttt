(ns clojure-ttt.io
  (require [clojure-ttt.utilities :as utils]))

(defn show [message]
  (println message))

(defn prompt [message]
  (println message) (flush) (utils/clean-string (read-line)))
