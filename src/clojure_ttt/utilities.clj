(ns clojure-ttt.utilities
  (:require [clojure.string :as string]))

(defn input-to-num [input]
  (Integer/parseInt input))

(defn in-range? [num start-inc end-exc]
  (and (<= start-inc num) (< num end-exc)))

(defn clean-string [str] (string/trim str))
