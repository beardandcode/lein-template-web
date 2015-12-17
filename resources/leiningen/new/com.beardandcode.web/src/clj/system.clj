(ns com.beardandcode.{{name}}.system
  (:require [com.stuartsierra.component :as component]
            [com.beardandcode.components
             [web-server :refer [new-web-server]]
             [routes :refer [new-routes]]]
            [com.beardandcode.{{name}}.webapp :as webapp]))

(defn new-system [env]
  (component/system-map
   :routes (new-routes webapp/routes-fn)
   :web (component/using
         (new-web-server (:ip-address env) (Integer/valueOf (:port env)))
         [:routes])))
