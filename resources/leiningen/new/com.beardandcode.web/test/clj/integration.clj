(ns com.beardandcode.{{name}}.integration
  (:require [clojure.test :refer :all]
            [clj-webdriver.taxi :as wd]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]
            [com.beardandcode.components.web-server :as web-server]
            [com.beardandcode.{{name}}.system :refer [new-system]]))

(def ^:private browser-count (atom 0))

(defn browser-retain []
  (when (= 1 (swap! browser-count inc))
    (wd/set-driver! {:browser :phantomjs})))

(defn browser-release [& {:keys [force] :or {force false}}]
  (when (zero? (swap! browser-count (if force (constantly 0) dec)))
    (wd/quit)))

(defn wrap-test [system]
  (fn [test-fn]
    (browser-retain)
    (test-fn)
    (browser-release)))

(defn store-system! [system]
  (fn [ns-fn]
    (let [system-map (new-system (assoc env :port 0))]
      (reset! system (component/start system-map))
      (ns-fn)
      (component/stop @system))))

(defn url
  ([system] (url system "/"))
  ([system path]
   (str "http://127.0.0.1:" (-> system :web web-server/port) path)))

(defn goto [system path]
  (wd/to (url @system path)))

(defn current-path [system]
  (let [base-url (url system "")
        base-re (re-pattern base-url)
        current-url (wd/current-url)]
        (clojure.string/replace-first current-url base-re "")))

(defmacro assert-path [system path]
  `(let [~'current-path (current-path @~system)]
     (if (instance? java.util.regex.Pattern ~path)
       (is (re-matches ~path ~'current-path))
       (is (= ~path ~'current-path)))))
