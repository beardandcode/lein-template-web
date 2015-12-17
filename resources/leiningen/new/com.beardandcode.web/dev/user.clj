(ns user
  (:require [clojure.java.shell :as shell]
            [clojure.repl :refer :all]
            [clojure.test :refer [run-all-tests]]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [environ.core :refer [env]]
            [reloaded.repl :refer [system init start stop go reset clear]]
            [com.beardandcode.components.web-server :refer [port]]
            [com.beardandcode.{{name}}.system :refer [new-system]]))

(reloaded.repl/set-init!
 #(new-system env))

(defn url [] (str "http://localhost:" (-> system :web port) "/"))
(defn open! [] (shell/sh "open" (url)))

(defn refresh-and [f]
    (refresh :after (symbol "user" f)))

(defn test-all [] (run-all-tests #"^com.beardandcode.{{name}}.*-test$"))
(defn test-unit [] (run-all-tests #"^com.beardandcode.{{name}}.(?!integration).*-test$"))
(defn test-integration [] (run-all-tests #"^com.beardandcode.{{name}}.integration.*-test$"))
