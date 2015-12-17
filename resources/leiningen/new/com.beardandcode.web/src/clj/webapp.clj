(ns com.beardandcode.{{name}}.webapp
  (:require [clojure.tools.logging :as log]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware
             [params :refer [wrap-params]]
             [session :refer [wrap-session]]
             [anti-forgery :refer [wrap-anti-forgery]]]))

(defn routes-fn [dependencies options]
  (-> (routes
       
       (GET "/" [] "Hello world!")

       (route/resources "/static/"))
      
      wrap-anti-forgery
      wrap-params
      wrap-session))
