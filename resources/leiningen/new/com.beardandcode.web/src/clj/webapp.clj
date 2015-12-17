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
       
       (GET "/" [] "
<!DOCTYPE html>
<html>
  <head>
    <link href=\"/static/css/bundle.css\" rel=\"stylesheet\">
  </head>
  <body>
     <div class=\"container\">
       <h1>Hello world!</h1>
     </div>
  </body>
</h1>")

       (route/resources "/static/"))
      
      wrap-anti-forgery
      wrap-params
      wrap-session))
