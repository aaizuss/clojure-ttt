(ns clojure-ttt.io-test
  (require [clojure-ttt.io :refer :all]
           [clojure.test :refer :all]))

(deftest show-test
  (testing "shows the message"
    (is (= "Hello!\n" (with-out-str (show "Hello!"))))))

; note: not sure how to stop the test from printing the prompt
(deftest prompt-test
  (testing "returns user input"
    (is (= "Nothing"
        (with-in-str "Nothing"
          (prompt "What's up?"))))))
