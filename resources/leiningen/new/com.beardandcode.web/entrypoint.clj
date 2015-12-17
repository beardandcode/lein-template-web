(ns com.beardandcode.{{name}}
  (:gen-class)
  (:require [environ.core :refer [env]]
            [com.stuartsierra.component :as component]
            [com.beardandcode.{{name}}.system :refer [new-system]]))

(defn -main []
  (component/start (new-system env)))
