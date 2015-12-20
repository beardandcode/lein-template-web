(defproject com.beardandcode.web/lein-template "0.1.1"
  :description "A template to generate web applications"
  :url "https://github.com/beardandcode/lein-template-web"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :eval-in-leiningen true
  :dependencies [[org.clojure/tools.cli "0.3.3"]
                 [me.raynes/fs "1.4.6"]
                 [cpath-clj "0.1.2"]]
  :signing {:gpg-key "tom@beardandcode.com"})
