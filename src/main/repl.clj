(ns repl
  (:require
    [clojure.java.io :as io]
    [shadow.css.build :as cb]
    [shadow.cljs.devtools.server.fs-watch :as fs-watch]))

(defonce css-ref (atom nil))
(defonce css-watch-ref (atom nil))

(defn generate-css []
  (let [result
        (-> @css-ref
            (cb/generate '{:ui {:include [starter*]}}) ;; <- your ns convention
            (cb/write-outputs-to (io/file "public" "css")))]

    (prn :CSS-GENERATED2)
    (doseq [mod (:outputs result)
            {:keys [warning-type] :as warning} (:warnings mod)]
      (prn [:CSS (name warning-type) (dissoc warning :warning-type)]))
    (println)))

(defn start
  {:shadow/requires-server true}
  []
  
  ;; first initialize my css
  (reset! css-ref
    (-> (cb/start)
        (cb/index-path (io/file "src" "main") {})))

  ;; then build it once
  (generate-css)

  ;; then setup the watcher that rebuilds everything on change
  (reset! css-watch-ref
    (fs-watch/start
      {}
      ;; Your files to watch -- make sure these exist or you will
      ;; get a cryptic NoSuchFileException
      [(io/file "src" "main") 
      ;;  (io/file "src" "dev")
       ] 
      ["cljs" "cljc" "clj"]
      (fn [updates]
        (try
          (doseq [{:keys [file event]} updates
                  :when (not= event :del)]
            ;; re-index all added or modified files
            (swap! css-ref cb/index-file file))

          (generate-css)
          (catch Exception e
            (prn :css-build-failure)
            (prn e))))))

  ::started)

(defn stop []
  (when-some [css-watch @css-watch-ref]
    (fs-watch/stop css-watch)
    (reset! css-ref nil))

  ::stopped)

(defn go []
  (stop)
  (start))
