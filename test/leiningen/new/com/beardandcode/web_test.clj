(ns leiningen.new.com.beardandcode.web-test
  (:require [clojure.test :refer :all]
            [clojure.java.shell :refer [sh]]
            [me.raynes.fs :as fs]
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
  (assert-sh "lein" "new" "com.beardandcode.web" "test-output" "--" "--license" "gplv3")
  (assert-sh "lein" "test" :dir "./test-output")
  (assert-sh "rm" "-rf" "test-output"))

(deftest can-compile-scss
  (assert-sh "lein" "new" "com.beardandcode.web" "test-output" "--" "--license" "gplv3")
  (assert-sh "lein" "scss" :dir "./test-output")
  (assert-sh "rm" "-rf" "test-output"))

(deftest can-pick-license
  (assert-sh "lein" "new" "com.beardandcode.web" "test-output" "--" "--license" "gplv3")
  (let [project-clj (-> "test-output/project.clj" slurp read-string)
        project-map (->> project-clj (drop 3) (apply hash-map))]
    (is (= (-> project-map :license :name) "GPLv3")))
  (assert-sh "diff" "resources/leiningen/new/com.beardandcode.web/licenses/GPLv3"
             "test-output/LICENSE")
  (assert-sh "rm" "-rf" "test-output"))

(deftest can-pick-different-license
  (assert-sh "lein" "new" "com.beardandcode.web" "test-output" "--" "--license" "mit")
  (let [project-clj (-> "test-output/project.clj" slurp read-string)
        project-map (->> project-clj (drop 3) (apply hash-map))]
    (is (= (-> project-map :license :name) "MIT")))
  (assert-sh "grep" "The MIT License" "test-output/LICENSE")
  (assert-sh "rm" "-rf" "test-output"))

(deftest by-default-no-git
  (assert-sh "lein" "new" "com.beardandcode.web" "test-output" "--" "--license" "mit")
  (is (not (fs/exists? "test-output/.git")))
  (assert-sh "rm" "-rf" "test-output"))

(deftest initialise-with-git
  (assert-sh "lein" "new" "com.beardandcode.web" "test-output" "--" "--license" "mit" "--git")
  (is (fs/exists? "test-output/.git"))
  (assert-sh "rm" "-rf" "test-output"))
