(ns com.beardandcode.{{name}}.integration.health-test
  (:require [clojure.test :refer :all]
            [clj-http.client :as client]
            [com.beardandcode.{{name}}.integration :refer :all]))

(def system (atom nil))

(use-fixtures :once (store-system! system))

(deftest ack
  (let [response (client/get (url @system "/health") {:as :json :basic-auth "dummy:dummy"
                                                      :throw-exceptions false})]
    (is (= (:status response) 200))
    (is (-> response :body :app))))

(deftest ack-fails-with-wrong-creds
  (let [response (client/get (url @system "/health") {:as :json :basic-auth "user:pass"
                                                      :throw-exceptions false})]
    (is (= (:status response) 401))))

(deftest ack-fails-with-no-creds
  (let [response (client/get (url @system "/health") {:as :json :throw-exceptions false})]
        (is (= (:status response) 401))))
