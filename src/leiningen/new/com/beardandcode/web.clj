(ns leiningen.new.com.beardandcode.web
  (:require [clojure.java.io :as io]
            [clojure.tools.cli :refer [parse-opts]]
            [cpath-clj.core :as cp]
            [me.raynes.fs :as fs]
            [leiningen.new.templates :refer [renderer year name-to-path ->files]]
            [leiningen.core.main :as main]
            [leiningen.new.com.beardandcode.interaction :refer [pick-value check]]))

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

(defn- keyword-to-str [k]
  (->> k str (drop 1) (apply str)))

(def licenses
  (sorted-map :mit {:name "MIT" :url "https://opensource.org/licenses/MIT"}
              :gplv3 {:name "GPLv3" :url "https://opensource.org/licenses/GPL-3.0"}))

(def cli-options
  [["-l" "--license TYPE" "License"
    :parse-fn #(-> % clojure.string/lower-case keyword)
    :validate [#(-> licenses keys set (contains? %))
               (format "Must be one of #{%s}" (clojure.string/join " " (map keyword-to-str (keys licenses))))]]
   ["-g" "--git" "Initialise as git repository"]])

(defn com.beardandcode.web
  [name & args]
  (println "
  ________________________________________
/ I see you are trying to build a web app, \\
\\ can I help you with that?                /
  ----------------------------------------
         \\   ^__^
          \\  (oo)\\_______
             (__)\\       )\\/\\
                 ||----w |
                 ||     ||
")
  (let [cli (parse-opts args cli-options)]
    (if (-> cli :errors empty?)
      (let [data {:name name
                  :name-path (name-to-path name)
                  :human-name (humanise name)
                  :year (year)
                  :username (System/getProperty "user.name")
                  :license (or (licenses (-> cli :options :license))
                               (pick-value "Which license would you like to use for this project?"
                                           (vals licenses) :name))}
            initialise-with-git? (or (-> cli :options :git)
                                     (check "Would you like the project initialised with git?"))]
        (->files data
             [".gitignore" (render "gitignore" data)]
             ["app.json" (render "app.json" data)]
             ["LICENSE" (render (str "licenses/" (-> data :license :name)) data)]
             ["Procfile" (render "Procfile" data)]
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
                           (str (base-path name) "/src/scss"))

        (if initialise-with-git?
          (clojure.java.shell/sh "git" "init" :dir (base-path name))))

      (do (doall (map println (:errors cli)))
          (System/exit 1)))))
