(defproject com.beardandcode/{{name}} "0.1.0"
  :description "A web app"
  :url "https://github.com/beardandcode/{{name}}"
  :license {:name "GPLv3"
            :url "http://www.gnu.org/copyleft/gpl.html"}
  
  :min-lein-version "2.0.0"
  
  :plugins [[lein-ancient "0.6.7"]
            [jonase/eastwood "0.2.1"]
            [lein-bikeshed "0.2.0"]
            [lein-kibit "0.1.2"]
            [lein-environ "1.0.0"]]
  
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [environ "1.0.0"]
                 [com.beardandcode/components "0.1.2"]
                 [compojure "1.4.0"]
                 [ring/ring-anti-forgery "1.0.0"]
                 [selmer "0.9.5"]
                 [buddy/buddy-auth "0.8.2"]]

  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :resource-paths ["resources" "src"]

  :uberjar-name "{{name}}.jar"

  :aliases {"scss" ["run" "-m" "com.beardandcode.tools/scss-main"]}

  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]
                                  [reloaded.repl "0.1.0"]
                                  [clj-webdriver "0.7.2"]
                                  [com.codeborne/phantomjsdriver "1.2.1"
                                   :exclusion [org.seleniumhq.selenium/selenium-java
                                               org.seleniumhq.selenium/selenium-server
                                               org.seleniumhq.selenium/selenium-remote-driver]]
                                  [org.seleniumhq.selenium/selenium-java "2.48.2"]
                                  [clj-http "2.0.0"]]
                   :source-paths ["dev"]
                   :env {:ip-address "127.0.0.1"
                         :port 8080}}
             :test {:env {:ip-address "127.0.0.1"
                          :port 0}}
             :uberjar {:prep-tasks ["scss" "javac" "compile"]
                       :main com.beardandcode.{{name}}
                       :aot :all}})
