(ns leiningen.new.com.beardandcode.web-test
  (:require [clojure.test :refer :all]
            [clojure.java.shell :refer [sh]]))

(defn assert-sh [& args]
  (let [result (apply sh args)]
    (if (-> result :exit zero? not)
      (println result))
    (is (-> result :exit zero?))))

(deftest tests-run-ok
  (assert-sh "lein" "new" "com.beardandcode.web" "test-output")
  (assert-sh "lein" "test" :dir "./test-output")
  (sh "rm" "-rf" "test-output"))
