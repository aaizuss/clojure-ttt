(defproject clojure-ttt "0.1.0-SNAPSHOT"
  :description "tic tac toe"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]]
  :main ^:skip-aot clojure-ttt.main
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
