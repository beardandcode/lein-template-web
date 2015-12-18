(ns leiningen.new.com.beardandcode.web-test
  (:require [clojure.test :refer :all]
            [clojure.java.shell :refer [sh]]
            [leiningen.new.com.beardandcode.web :as web]))

(defn assert-sh [& args]
  (println "$" (clojure.string/join " " args))
  (let [result (apply sh args)]
    (if (-> result :exit zero? not)
      (println result))
    (is (-> result :exit zero?))))

(deftest humanise
  (is (= (web/humanise "name") "Name"))
  (is (= (web/humanise "some-name") "Some name")))

(deftest tests-run-ok
  (assert-sh "lein" "new" "com.beardandcode.web" "test-output")
  (assert-sh "lein" "test" :dir "./test-output")
  (assert-sh "rm" "-rf" "test-output"))

(deftest can-compile-scss
  (assert-sh "lein" "new" "com.beardandcode.web" "test-output")
  (assert-sh "lein" "scss" :dir "./test-output")
  (assert-sh "rm" "-rf" "test-output"))
