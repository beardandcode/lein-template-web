(ns leiningen.new.com.beardandcode.interaction)

(defn- ask-until [prompt pred]
  (print prompt)
  (flush)
  (let [response (read-line)]
    (if (pred response)
      (list response)
      (lazy-seq (cons response (ask-until prompt pred))))))

(defn pick-value [question values label-fn]
  (let [labels (->> values
                    (map label-fn)
                    (map-indexed #(str "\t" (inc %1) ". " %2))
                    (clojure.string/join "\n"))
        prompt (format "Please enter a number between %d and %d: " 1 (count values))]
    (print (format "%s\n\n%s\n\n" question labels))
    (flush)
    (let [answer (-> (ask-until prompt #(if-let [match (re-matches #"^[0-9]+" %)]
                                          (let [value-index (Integer/parseInt match)]
                                            (and (> value-index 0)
                                                 (<= value-index (count values))))))
                     last Integer/parseInt dec)]
      (nth values answer))))

(defn check [question]
  (let [prompt (str question " (y/n) ")]
    (-> (ask-until prompt #(or (nil? %) (re-matches #"^[yn]$" %)))
        last (= "y"))))
