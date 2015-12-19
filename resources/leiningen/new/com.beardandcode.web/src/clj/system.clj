(ns com.beardandcode.{{name}}.system
  (:require [com.stuartsierra.component :as component]
            [com.beardandcode.components
             [web-server :refer [new-web-server]]
             [routes :refer [new-routes new-context-routes]]]
            [com.beardandcode.{{name}}.health :as health]
            [com.beardandcode.{{name}}.webapp :as webapp]))

(defn new-system [env]
  (component/system-map
   :health-routes (new-routes health/routes-fn {:username (:health-username env)
                                                :password (:health-password env)})
   :webapp-routes (new-routes webapp/routes-fn)
   :routes (component/using (new-context-routes {"/health" :health-routes
                                                 ""        :webapp-routes})
                            [:webapp-routes :health-routes])
   :web (component/using
         (new-web-server (:ip-address env) (Integer/valueOf (:port env)))
         [:routes])))
