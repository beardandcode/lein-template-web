(ns com.beardandcode.{{name}}.health
  (:require [clojure.tools.logging :as log]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [buddy.auth.backends.httpbasic :refer [http-basic-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [cheshire.core :as json]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [metrics.ring.expose :refer [serve-metrics]]
            [ring.middleware.params :refer [wrap-params]]))

(defprotocol IHealthcheck
  (alive? [_]))

(defn- wrap-authentication-manditory [handler]
  (fn [request]
     (if (authenticated? request)
       (handler request)
       (throw-unauthorized))))

(defn- unauthorized-handler [_ metadata]
  {:status 401
   :headers {"WWW-Authenticate"
             (str "Basic realm=\"" (:realm metadata) "\"")
             "Content-Type" "application/json; charset=UTF-8"}
   :body "{\"error\": \"Not authorized\"}"})

(defn json-response
  ([obj] (json-response 200 obj))
  ([status obj]
   {:status status
    :headers {"Content-Type" "application/json; charset=UTF-8"}
    :body (json/generate-string obj {:pretty true})}))

(defn routes-fn [dependencies options]
  (let [username (or (:username options) "dummy")
        password (or (:password options) "dummy")
        auth-backend (http-basic-backend {:realm "{{human-name}}"
                                          :authfn (fn [_ authdata]
                                                     (if (and (-> authdata :username (= username))
                                                              (-> authdata :password (= password)))
                                                       username))
                                          :unauthorized-handler unauthorized-handler})]
    (-> (routes

         (GET "/" [:as request] (json-response {:app true}))

         (GET "/environment-variables" [] (json-response env))

         (GET "/metrics" [] (serve-metrics nil))

         (route/resources "/static/"))

        wrap-authentication-manditory
        (wrap-authentication auth-backend)
        (wrap-authorization auth-backend)
        wrap-params)))
