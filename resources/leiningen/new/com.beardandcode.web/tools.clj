(ns com.beardandcode.tools
  (:require [clojure.java.shell :as shell]))

(defn scss
  ([] (scss "src/scss/bundle.scss" "resources/public/css/bundle.css"))
  ([input output] (scss input output "nested" "src/scss"))
  ([input output style import]
   (let [result (shell/sh "sassc" "-t" style "-I" import input output)]
     (println result)
     (:exit result))))

(defn scss-main [& _]
  (System/exit (scss)))
