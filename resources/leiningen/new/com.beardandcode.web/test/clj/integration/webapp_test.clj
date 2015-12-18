(ns com.beardandcode.{{name}}.integration.webapp-test
  (:require [clojure.test :refer :all]
            [clj-webdriver.taxi :as wd]
            [com.beardandcode.{{name}}.integration :refer :all]))

(def system (atom nil))

(use-fixtures :each (wrap-test system))
(use-fixtures :once (store-system! system))

(deftest hello-world
  (goto system "/")
  (is (= (count (wd/elements "h1.hero")) 1)))
