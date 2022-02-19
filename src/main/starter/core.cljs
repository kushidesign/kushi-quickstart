(ns cprint.core
  (:require
   [par.core :refer-macros [!? ?]]
   [clojure.string :as string]))

;; TODO:
;; - environ variables
;; - terminal printing
;; - dark themes
;; - hifi light & dark themes

;; COLOR PRINTER START --------------------------------------------------------------------------

(def coll-count-max 5)
(def coll-str-max 30)
(def colorize? true)
(def string-tag (when colorize? "₋⋄₋"))
(def kw-tag (when colorize? "₋⋅₋"))
(def number-tag (when colorize? "₋⋆₋"))
(def atom-tag (when colorize? "₋∘₋"))
(def reset-tag (when colorize? "₋∙₋"))
(def symbol-tag (when colorize? "₋∴₋"))

(def tag-patterns [string-tag kw-tag number-tag atom-tag symbol-tag])

(def colors-by-tag
  {string-tag "#448C27"
  ;;  kw-tag "#7A3E9D"
   kw-tag "#626262"
   number-tag "#7A3E9D"
   atom-tag "yellow"
   symbol-tag "#325CC0"
  ;;  symbol-tag "blue"
   reset-tag "#626262"
   } )

(defn browser-formatted-js-vec [s]
  (let [number-of-formats (count (re-seq #"%c" s))
        fmttrs            #(repeat (/ number-of-formats 2) %)
        formatting-arg    (interleave (fmttrs "color:black;font-weight:bold")
                                      (fmttrs "color:default;font-weight:normal"))]
    (into [] (concat [s] formatting-arg))))

(defn accutre [x atom?]
  (when (coll? x)
    ;; tuple structure is:  [opening-brace closing-brace indent-space limit]
    (let [ret* (cond (set? x)
                     ["#{" "}" "  " 20]
                     (map? x)
                     ["{" "}" " " 5]
                     (vector? x)
                     ["[" "]" " " 20]
                     (coll? x)
                     ["(" ")" " " 20])
          ret  (if atom?
                 (let [[ob _ ind _] ret*]
                   (-> ret*
                       (assoc 0 (str "@" ob))
                       (assoc 2 (str " " ind))))
                 ret*)]
      ret)))

#_(defn tag-by-type [x]
  (cond ))

(defn one-line? [x+]
  (and (> coll-count-max (count x+))
       (> coll-str-max (count (str x+)))))

(defn indent-spaces [indent]
 (apply str (repeat indent " ")) )

(defn flat+str [one-line? coll trailing cb]
  (str (apply str
          (flatten (interpose (if one-line? " " "\n")
                              coll)))
       trailing
       cb))

(defn leading-char
  [idx ob one-line? indent-spaces ind]
  (if (zero? idx) ob (when-not one-line? (str indent-spaces ind))))

(defn np [x* indent]
  (let [atom? (= cljs.core/Atom (type x*))
        x     (if atom? @x* x*)]
    (!? x)
    (cond
      (map? x)
      (let [key-slot-len  (->> x keys (map #(-> % str count)) (apply max) inc)
            indent-spaces (indent-spaces indent)
            [ob
             cb
             ind
             limit]     (accutre x atom?)
            truncate?   (> (count x) limit)
            x+          (if truncate? (into {} (take limit (seq x))) x)
            num-left    (when truncate? (- (count x) limit))
            one-line?   (one-line? x+)
            kvs->strs*  (map-indexed
                         (fn [idx [k v]]
                           (let [num-extra-spaces (if one-line? 1 (- key-slot-len (-> k str count)))
                                 extra-spaces     (apply str (repeat num-extra-spaces " "))
                                 leading-char     (leading-char idx ob one-line? indent-spaces ind)
                                 new-key          (str leading-char (np k 0) extra-spaces)
                                 new-val          (np v (inc (if one-line? 0 (+ indent key-slot-len))))]
                             [new-key new-val]))
                         x+)
            trailing    (when num-left (str "  ...+" num-left " more..."))
            ret         (flat+str one-line?
                                  kvs->strs*
                                  trailing
                                  cb)]
        ret)
      (coll? x)
      (let [indent-spaces (indent-spaces indent)
            [ob
             cb
             ind
             limit]   (accutre x atom?)
            x+        (take limit x)
            next      (if (set? x) (get x (inc limit) nil) (nth x (inc limit) nil))
            one-line? (one-line? x+)
            vs*       (map-indexed
                       (fn [idx v]
                         (let [leading-char (leading-char idx ob one-line? indent-spaces ind)
                               new-val      (str leading-char (np v (inc (+ indent 0))))]
                           new-val))
                       x+)
            trailing  (when next " ...")
            ret       (flat+str one-line?
                                vs*
                                trailing
                                cb)]
        ret)
      (string? x) (if colorize? (str string-tag "\"" x "\"" reset-tag) (str "\"" x "\""))
      (keyword? x) (if colorize? (str kw-tag x reset-tag) x)
      (number? x) (if colorize? (str number-tag x reset-tag) x)
      (symbol? x) (if colorize? (str symbol-tag x reset-tag) x)
      :else x)))


(defn tag->esc [s pattern]
  (string/replace s (re-pattern pattern) "%c"))

(defn tags->esc [s]
  (let [pattern (some #(re-find (re-pattern %) s) tag-patterns)
        ret     (if pattern
                  (-> s (tag->esc pattern) (tag->esc reset-tag))
                  s)]
   ret))

(defn tags->css [s]
  (let [pattern (some #(re-find (re-pattern %) s) tag-patterns)
        color   (get colors-by-tag pattern "#8a8a8a")
        ret     (when pattern [(str "color: " color ";")
                               (str "color:" (get colors-by-tag reset-tag) ";")] )]
    ret) )

(defn tokens+styles [coll]
  (let [x (reduce
           (fn [{:keys [tokens styles] :as acc}
                s]
             (let [[style reset-style] (tags->css s)
                   token               (tags->esc s)]
               (assoc acc
                      :tokens
                      (conj tokens token)
                      :styles
                      (conj styles style reset-style))))
           {:tokens []
            :styles []}
           coll)
        result (->> x :tokens (string/join " "))
        ret    (concat [result] (:styles x))]
    ret))


(.apply (.-log  js/console)
             js/console
             (into-array
              (remove nil?
                      (-> #_{:a [1 2 3]
                             :bee "Sylvania"
                             :d {:green "grn" :red 'red}}
                          {:a (atom [1 2 3])
                           :bee "Sylvania"
                           :cy {:a1 '(:e {:firstname "Jefffn" :lastname "Gaah" :colors #{:green :blue :red}} :bbbbb 234444) :bee1 "Plppp" :cy1 23232 :d1 23232}
                           :d (atom {:green "grn" :red "red"})
                           :e "eeee"
                           :f "ffff"
                           :gg "ggg"}
                          (np 0)
                          (string/split #" ")
                          tokens+styles))))

#_(println (np (atom {:a 1}) 0))
#_(println  (np {:a (atom [1 2 3])
               :bee "Sylvania"
               :cy {:a1 '(:e {:firstname "Jeff" :lastname "Gahrn" :colors #{ :green :blue :red}} :bbbbb 234444) :bee1 "Plppp" :cy1 23232 :d1 23232}
               :d (atom {:green "grn" :red "red"})
               :e "eeee"
               :f "ffff"
               :gg "ggg"} 0))

#_(println (-> {:a [1 2 3]
              :bee "Sylvania"
              :d {:green "grn" :red "red"}}
             (np 0)))

;; COLOR PRINTER END --------------------------------------------------------------------------
