(ns leiningen.new.com.beardandcode.web
  (:require [clojure.java.io :as io]
            [cpath-clj.core :as cp]
            [me.raynes.fs :as fs]
            [leiningen.new.templates :refer [renderer name-to-path ->files]]
            [leiningen.core.main :as main]))

(def render (renderer "com.beardandcode.web"))

(defn base-path [name]
  (-> (System/getProperty "leiningen.original.pwd")
      (io/file name) (.getPath)))

(defn copy-resource-dir [from-path to-path]
  (doseq [[path uris] (cp/resources (io/resource from-path))
          :let [uri (first uris)
                relative-path (subs path 1)
                output-file (io/file to-path relative-path)]]
    (fs/mkdirs (fs/parent (.getCanonicalPath output-file)))
    (with-open [in (io/input-stream uri)]
      (io/copy in output-file))))

(defn humanise [name]
  (-> name (clojure.string/replace #"[-_]" " ")
      (clojure.string/capitalize)))

(defn com.beardandcode.web
  [name & args]
  (let [data {:name name
              :name-path (name-to-path name)
              :human-name (humanise name)}]
    (main/info "Generating fresh 'lein new' com.beardandcode.web project.")
    (->files data
             [".gitignore" (render "gitignore" data)]
             ["LICENSE" (render "LICENSE" data)]
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["dev/user.clj" (render "dev/user.clj" data)]
             ["resources/logback.xml" (render "resources/logback.xml")]
             ["resources/public/css/.gitkeep" ""]
             ["src/clj/com/beardandcode/tools.clj" (render "tools.clj" data)]
             ["src/clj/com/beardandcode/{{name-path}}.clj" (render "entrypoint.clj" data)]
             ["src/clj/com/beardandcode/{{name-path}}/health.clj" (render "src/clj/health.clj" data)]
             ["src/clj/com/beardandcode/{{name-path}}/system.clj" (render "src/clj/system.clj" data)]
             ["src/clj/com/beardandcode/{{name-path}}/webapp.clj" (render "src/clj/webapp.clj" data)]
             ["src/templates/base.html" (render "src/templates/base.html" data)]
             ["src/templates/home.html" (render "src/templates/home.html" data)]
             ["test/clj/com/beardandcode/{{name-path}}/integration.clj" (render "test/clj/integration.clj" data)]
             ["test/clj/com/beardandcode/{{name-path}}/integration/health_test.clj" (render "test/clj/integration/health_test.clj" data)]
             ["test/clj/com/beardandcode/{{name-path}}/integration/webapp_test.clj" (render "test/clj/integration/webapp_test.clj" data)])
    (copy-resource-dir "leiningen/new/com.beardandcode.web/src/scss"
                       (str (base-path name) "/src/scss"))))
