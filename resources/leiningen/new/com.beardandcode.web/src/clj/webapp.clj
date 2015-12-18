(ns com.beardandcode.{{name}}.webapp
  (:require [clojure.tools.logging :as log]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware
             [params :refer [wrap-params]]
             [session :refer [wrap-session]]
             [anti-forgery :refer [wrap-anti-forgery]]]
            [selmer.parser :refer [render-file]]))

(defn routes-fn [dependencies options]
  (-> (routes
       
       (GET "/" [] (render-file "templates/home.html" {}))

       (route/resources "/static/"))
      
      wrap-anti-forgery
      wrap-params
      wrap-session))
