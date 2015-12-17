(ns leiningen.new.com.beardandcode.web
  (:require [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "com.beardandcode.web"))

(defn com.beardandcode.web
  [name & args]
  (let [data {:name name
              :name-path (name-to-path name)}]
    (main/info "Generating fresh 'lein new' com.beardandcode.web project.")
    (->files data
             ["LICENSE" (render "LICENSE" data)]
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["dev/user.clj" (render "dev/user.clj" data)]
             ["resources/logback.xml" (render "resources/logback.xml")]
             ["src/clj/com/beardandcode/{{name-path}}.clj" (render "entrypoint.clj" data)]
             ["src/clj/com/beardandcode/{{name-path}}/webapp.clj" (render "src/clj/webapp.clj" data)]
             ["src/clj/com/beardandcode/{{name-path}}/system.clj" (render "src/clj/system.clj" data)]
             ["test/clj/com/beardandcode/{{name-path}}/integration.clj" (render "test/clj/integration.clj" data)]
             ["test/clj/com/beardandcode/{{name-path}}/integration/webapp_test.clj" (render "test/clj/integration/webapp_test.clj" data)])))
