(ns clojure-ttt.utilities)

(defn input-to-num [input]
  (Integer/parseInt input))

(defn in-range? [num start-inc end-exc]
  (and (<= start-inc num) (< num end-exc)))
