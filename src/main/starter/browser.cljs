(ns starter.browser
  (:require
   [shadow.cljs.modern :refer (defclass)]
   ;;  [sci.core :as sci]
   [bling.core :refer [callout]]
   [bling.explain]
   [kushi.ui.variants]
   [kushi.css.schemas]
   [clojure.string :as string]
   [clojure.math :as math]
   [fireworks.core :refer [? !? ?> !?> pprint]]
   [fireworks.defs]

   ;;  [bling.core :refer [bling print-bling]]
   ;;  [bling.hifi :refer [print-hifi hifi]]
   ;;  [bling.explain :refer [explain-malli]]

   ;; Require various functions and macros from kushi.core
   [kushi.core :refer [sx sx2 merge-attrs]]

   ;; Require some components
   ;;  [kushi.playground.ui :refer [light-dark-mode-switch]]
   ;;  [kushi.ui.core :refer [opts+children]]
   ;;  [kushi.ui.button.core :refer [button icon-button]]
   ;;  [kushi.ui.callout.core :refer [callout]]
   ;;  [kushi.ui.collapse.core :refer [collapse]]
   ;;  [kushi.ui.card.core :refer [card]]
    ;; [kushi.ui.icon.core :refer [icon]]
   ;;  [kushi.ui.radio.core :refer [radio]]
   ;;  [kushi.ui.switch.core :refer [switch]]

  ;;  [kushi.ui.slider :refer [slider]]
  ;;  [kushi.ui.box :refer [box]]
   [kushi.ui.variants :as props]

   ;;  [kushi.ui.link.core :refer [link]]
   ;;  [kushi.ui.checkbox.core :refer [checkbox]]
   ;;  [kushi.ui.divisor.core :refer [divisor]]
   ;;  [kushi.ui.text-field.core :refer [text-field]]
   ;;  [kushi.ui.label.core :refer [label]]
   ;;  [kushi.ui.tag.core :refer [tag]]
   ;;  [kushi.ui.spinner.core :refer [propeller donut thinking]]

   ;;  ;; Require your apps shared classes and component namespaces
   ;;  [starter.badges :as badges]

   ;; This example uses reagent
   [reagent.dom :as rdom]
   [reagent.ratom]
   [lasertag.core :as lasertag :refer [tag tag-map]]



   ;; Need these for color lib -------------------------------------------------
  ;;  [kushi.ui.slider :refer [slider]]
  ;;  [kushi.ui.box :refer [box]]
  ;;  ["culori" :refer [clampChroma]]
  ;;  ["CanvasSpliner" :refer [CanvasSpliner]]

   ;; Need these for color lib -------------------------------------------------


   [malli.core :as malli]
   [domo.core :as domo]
   [lasertag.cached :as cached]
   [devtools.core :as devtools]))

(devtools/install!)

#_(js/console.clear)

;;  (? :no-file lasertag/js-built-ins-by-built-in)
;;  (? :no-file (lasertag/tag-map (aget "hi" "concat")))
;;  (? :no-file (lasertag/tag-map (aget #js [1 2 3] "concat")))
;;  (? :no-file (lasertag/tag-map js/String))

;; This is all the cached tag-maps you need for js/built-ins
;; but more in all-tags like :js :built-in :non-constructor
;;  (!? (lasertag/tag-map juxt))
;;  (!? (lasertag/tag-map hi))
;;  (!? (lasertag/tag-map #()))
;;  (!? (lasertag/tag-map (fn wtf [])))

;;  (? jsi/js-built-ins-cached-tag-maps)
;;  (? (lasertag/tag-map js/RangeError))

;;  (? {:print-length 300 :scalar-max-length 300} jsi/js-built-ins-cached-tag-maps)

;;  (? (lasertag/tag-map js/Math))

;;  #_(-> (? :data {:print-length 300 :scalar-max-length 300} jsi/js-built-ins-cached-tag-maps)
;;      :formatted
;;      :string
;;      println)


;; #_(-> (? {:truncate? false} jsi/js-built-ins-cached-tag-maps))


;; const littleEndian = (() => {
;;   const buffer = new ArrayBuffer(2);
;;   new DataView(buffer).setInt16(0, 256, true /* littleEndian */);
;;   // Int16Array uses the platform's endianness.
;;   return new Int16Array(buffer)[0] === 256;
;; })();
;; console.log(littleEndian); // true or false

;; (? :log js/Atomics)

;; Run this in codegen phase to produce array of prop-names and attach to :resolver-fn or :instance-properties

(def sgm (new js/Intl.Segmenter "fr" #js{:granularity "word"} "fr"))
;; (def m (tag-map sgm))
;; (def arrBuff (new js/ArrayBuffer 2))

#_(? [(some-> m :resolver (apply [sgm]))])
#_(? [sgm])
;; (? [arrBuff])
;; (? (aget sgm js/Symbol.toStringTag))
;; (? (= (js/Boolean nil) false))
;; (? (= (js/Boolean 1) true))

;; (? (type js/Error))
;; (? (lasertag.core/tag-map js/Intl.Locale))
;; (? (lasertag.core/tag-map js/Array.concat))
;; (? (lasertag.core/tag-map #(inc %)))
;; (? (lasertag.core/tag-map (aget "string" "concat")))


(def korean (new js/Intl.Locale "ko" #js{:script "Kore" :region "KR" :hourCycle "h23" :calendar "gregory"}))
;; (? [#js{:one 1 :two 2 :three 3}])
;; (? [korean])


(defn built-in? [obj]
  (if-let [ctor (.-constructor obj)]
    (let [ctor-str (js/Function.prototype.toString.call ctor)]
      (string/includes? ctor-str " [native code] "))
    false))


(defclass HelloWorld
  (extends js/HTMLElement)

  (constructor [this]
               (super))

  Object
  (connectedCallback [this]
                     (js/console.log "hey from CLJS" this)))

#_(js/window.customElements.define "hello-world" HelloWorld)


(defn foo [x] nil)

;; (? (contains? jsi/built-ins HelloWorld))

;; (? (built-in? foo))

;; (? (aget foo js/Symbol.toStringTag))
;; (? (aget sgm js/Symbol.toStringTag))

;; Try this to suss out js-built-in situation
;; (? (aget (new js/Int8Array #js[1 2 3]) js/Symbol.toStringTag))
;; (? :js (new js/Int8Array #js[1 2 3]))
;; (? :js ( (js/Function. "a" "b" "return a + b")  1 2))
;; (? :js #(inc %))
;; (? {:display-metadata? true} (with-meta (symbol "hi") {:bling.theme/token :js-object-key}))

;; (? [(.resolvedOptions (new js/Intl.Segmenter "fr" #js{:granularity "word"} "fr"))])
;; (? [#js{:a 1}])


;; (doseq [m (-> jsi/built-ins vals)]
;;   (let [example (some-> m :example*)
;;         string-tag (boolean (some-> example (aget js/Symbol.toStringTag)))]
;;     (when example
;;       (!? (object? example))
;;       [(:sym m) string-tag])))


(!? (instance? js/Object korean))
#_(js/console.log (aget (.values #js [1 2 3]) js/Symbol.toStringTag))
;; (? (uuid "00000000-0000-0000-0000-000000000000"))

;; (? (lasertag.core/tag #"hey"))

;; (? (implements? IReversible [1 2 3]))
;; (? (implements? ICounted [1 2 3]))
;; (? (implements? ICollection [1 2 3]))
;; (? (.-name (type [1 2 3])))

;; (js/console.log (.hasOwnProperty [1 2 3] "cljs$lang$protocol_mask$partition0$"))

;; (js/console.log (.hasOwnProperty (delay (println "whhhhh") 1000) "cljs$lang$protocol_mask$partition0$"))

;; (println (delay (println "whhhhh") 1000))

;; (println (lasertag.core/tag-map (delay (println "whhhhh") 1000)))
;; (? :pp (lasertag.core/tag-map (atom 1000)))

;; (println (lasertag.core/tag-map [1 2 3] #_(volatile! 1000)))

;; (? (instance? cljs.core/IReversible (reverse [1 2 3])))
;; (? (reversible? (reverse [1 2 3])))
;; (? (type (reverse [1 2 3])))

;; (? (tag-map true))

;; ;; (? (tag-map 1))
;; ;; (? (tag-map "hey"))
;; (? (tag-map 'go))
;; (? (tag-map #"mn"))

;; #_(-> (? :data
;;         {:print-length                   300
;;          :scalar-max-length        300
;;          :scalar-mapkey-max-length 300}
;;         jsi/js-built-ins)
;;      :formatted
;;      :string
;;      println)

;; (? (type ##NaN))

;; (? (tag-map (js/BigInt 9007199254740991)))

;; (? (double? 42))
;; (? (int? 42.05))


;; (? (int? (double 42)))
;; (? (lasertag.core/tag-map ##NaN))



#_(? [{:name "Integer" :desc "ClojureScript integer literal" :example 42}])

#_(? :+ {:print-length 80
         ;; :quote-symbols? true
         }
     (reduce
      (fn [acc m]
        (assoc acc
               (type (:example m))
               (lasertag.core/tag-map (:example m))))
      {}
      [;;  {:name "BigInt"
       ;;   :desc "Big Integer (max safe integer)"
       ;;   :quoted '(clojure.lang.BigInt 9007199254740991)
       ;;   :example (clojure.lang.BigInt 9007199254740991)}

       {:name "Boolean True"
        :desc "Boolean true value"
        :example true}

       {:name "Boolean False"
        :desc "Boolean false value"
        :example false}

       {:name "Nil"
        :desc "ClojureScript nil value (represents absence of value)"
        :example nil}

       {:name "Atom"
        :desc "Mutable reference type, created with initial value"
        :quoted '(atom 1)
        :example (atom 1)}

       {:name "Positive Infinity"
        :desc "Special numeric value representing positive infinity"
        :quoted '##Inf
        :example ##Inf}

       {:name "Negative Infinity"
        :desc "Special numeric value representing negative infinity"
        :quoted '##-Inf
        :example ##-Inf}

       {:name "NaN"
        :desc "Special numeric value representing Not-a-Number"
        :quoted '##NaN
        :example ##NaN}

       {:name "String"
        :desc "ClojureScript string literal"
        :example "hello"}

       {:name "Boolean"
        :desc "ClojureScript string literal"
        :example "hello"}

       {:name "Keyword"
        :desc "ClojureScript keyword"
        :example :hello}

       {:name "Symbol"
        :desc "ClojureScript symbol, quoted"
        :quoted  'hello
        :example 'hello}

       {:name    "Range"
        :desc    "Lazy sequence of integers from 0 to n-1"
        :quoted  '(range 5)
        :example (range 5)}

       {:name    "Reverse"
        :desc    "Returns a sequence in reverse order"
        :quoted  '(reverse (range 5))
        :example (reverse (range 5))}

       {:name    "Cons"
        :desc    "Constructs a new sequence with element prepended to collection"
        :quoted  '(cons 1 '(2 3))
        :example (cons 1 '(2 3))}

       {:name    "Cons"
        :desc    "Constructs a new sequence with element prepended to collection"
        :quoted  '(cons 1 '(2 3))
        :example (cons 1 '(2 3))}

       ;; throws
       #_{:name    "Assert"
          :desc    "Throws an error if condition is false"
          :quoted  '(assert false "thrown error")
          :example (assert false "thrown error")}

       {:name    "Repeat"
        :desc    "Returns a lazy infinite sequence of x"
        :quoted  '(repeat 3)
        :example (take 10 (repeat 3))}

       {:name    "Repeat with count"
        :desc    "Returns a lazy sequence of n repetitions of x"
        :quoted  '(repeat 5 :x)
        :example (repeat 5 :x)}

       {:name    "Repeatedly"
        :desc    "Returns a lazy infinite sequence of calls to function f"
        :quoted  '(take 10 (repeatedly rand))
        :example (take 10 (repeatedly rand))}

       {:name    "Cycle"
        :desc    "Returns a lazy infinite sequence of repetitions of the items in collection"
        :quoted  '(take 10 (cycle [1 2 3]))
        :example (take 10 (cycle [1 2 3]))}

       ;;  ;; infinite
       ;;  ;; {:name "Iterate"
       ;;  ;;   :desc "Returns a lazy infinite sequence of x, (f x), (f (f x)), etc."
       ;;  ;;   :quoted '(iterate inc 0)
       ;;  ;;   :example (iterate inc 0)}

       {:name    "Take"
        :desc    "Returns a lazy sequence of the first n items"
        :quoted  '(take 3 (range 10))
        :example (take 3 (range 10))}

       {:name    "Drop"
        :desc    "Returns a lazy sequence with the first n items removed"
        :quoted  '(drop 3 (range 10))
        :example (drop 3 (range 10))}

       {:name    "List (function)"
        :desc    "Creates a list from arguments"
        :quoted  '(list 1 2 3)
        :example (list 1 2 3)}

       {:name    "Vector (function)"
        :desc    "Creates a vector from arguments"
        :quoted  '(vector 1 2 3)
        :example (vector 1 2 3)}

       {:name    "Array Map"
        :desc    "Creates a small map optimized for sequential lookup"
        :quoted  '(array-map :a 1 :b 2)
        :example (array-map :a 1 :b 2)}

       {:name    "Queue"
        :desc    "Persistent queue (FIFO)"
        :quoted  '(conj cljs.core.PersistentQueue.EMPTY 1 2 3)
        :example (conj cljs.core.PersistentQueue.EMPTY 1 2 3)}

       {:name    "Lazy Seq"
        :desc    "Creates a lazy sequence from a body of expressions"
        :quoted  '(lazy-seq (cons 1 (lazy-seq (cons 2 nil))))
        :example (lazy-seq (cons 1 (lazy-seq (cons 2 nil))))}

       {:name    "Interleave"
        :desc    "Returns a lazy seq of first item from each coll, then second, etc."
        :quoted  '(interleave [1 2 3] [:a :b :c])
        :example (interleave [1 2 3] [:a :b :c])}

       {:name    "Interpose"
        :desc    "Returns a lazy seq with separator interposed between elements"
        :quoted  '(interpose "," ["a" "b" "c"])
        :example (interpose "," ["a" "b" "c"])}

       {:name    "Concat"
        :desc    "Returns a lazy seq representing the concatenation of collections"
        :quoted  '(concat [1 2] [3 4] [5 6])
        :example (concat [1 2] [3 4] [5 6])}

       {:name    "Mapcat"
        :desc    "Returns the result of applying concat to the result of applying map"
        :quoted  '(mapcat reverse [[1 2] [3 4]])
        :example (mapcat reverse [[1 2] [3 4]])}

       {:name    "Flatten"
        :desc    "Takes any nested combination of sequential things and returns a flat sequence"
        :quoted  '(flatten [1 [2 [3 4]] 5])
        :example (flatten [1 [2 [3 4]] 5])}

       {:name    "Partition"
        :desc    "Returns a lazy sequence of lists of n items each"
        :quoted  '(partition 2 [1 2 3 4 5 6])
        :example (partition 2 [1 2 3 4 5 6])}

       {:name    "Partition-all"
        :desc    "Returns a lazy sequence of lists like partition, but includes final partial partition"
        :quoted  '(partition-all 2 [1 2 3 4 5])
        :example (partition-all 2 [1 2 3 4 5])}

       {:name    "Split-at"
        :desc    "Returns a vector of [(take n coll) (drop n coll)]"
        :quoted  '(split-at 2 [1 2 3 4 5])
        :example (split-at 2 [1 2 3 4 5])}

       {:name    "Subvec"
        :desc    "Returns a subvector of vector v from start (inclusive) to end (exclusive)"
        :quoted  '(subvec [1 2 3 4 5] 1 3)
        :example (subvec [1 2 3 4 5] 1 3)}

       {:name    "Transient"
        :desc    "Creates a transient version of a collection for efficient batch updates"
        :quoted  '(transient [1 2 3])
        :example (transient [1 2 3])}

       {:name    "Persistent!"
        :desc    "Converts a transient collection back to persistent"
        :quoted  '(persistent! (transient [1 2 3]))
        :example (persistent! (transient [1 2 3]))}

       {:name    "Character Literal"
        :desc    "Character literal notation"
        :quoted  '\h
        :example \h}

       {:name    "Newline Character"
        :desc    "Newline character literal"
        :quoted  '\n
        :example \n}

       {:name    "Tab Character"
        :desc    "Tab character literal"
        :quoted  '\tab
        :example \tab}

       {:name    "Scientific Notation"
        :desc    "Number in scientific notation"
        :quoted  (symbol "1.5e3")
        :example 1.5e3}

       {:name    "Hexadecimal"
        :desc    "Hexadecimal number literal"
        :quoted  (symbol "0xFF")
        :example 0xFF}

       {:name    "Octal"
        :desc    "Octal number literal"
        :quoted  (symbol "077")
        :example 077}

       {:name    "Binary"
        :desc    "Binary number literal"
        :quoted  (symbol "2r1010")
        :example 2r1010}

       {:name    "Arbitrary Radix"
        :desc    "Number in arbitrary base (radix)"
        :quoted  (symbol "36rZZ")
        :example 36rZZ}

       {:name    "Tagged Literal"
        :desc    "Extensible data notation tagged literal"
        :quoted  '#inst "2024-01-01"
        :example #inst "2024-01-01"}


       {:name    "Anonymous Function"
        :desc    "Anonymous function literal syntax"
        :quoted  (symbol "#(+ % 1)")
        :example #(+ % 1)}

       {:name    "Metadata"
        :desc    "Attaches metadata to an object"
        :quoted  '(with-meta [1 2 3] {:doc "My vector"})
        :example (with-meta [1 2 3] {:doc "My vector"})}

       {:name    "Deref"
        :desc    "Dereferences a reference type (atom, delay, etc.)"
        :quoted  '(deref (atom 42))
        :example (deref (atom 42))}]))






;; FOR CLAWED
;; [{:type     clojure.lang.PersistentList
;;   :examples [{:quoted  '(1 2 3)
;;               :literal '(1 2 3)}
;;              {:quoted  '(list 1 2 3)
;;               :literal (list 1 2 3)}]}
;;  {:type     clojure.lang.PersistentHashMap
;;   :examples [{:quoted  '{:a 1 :b 2}
;;               :literal {:a 1 :b 2}}
;;              {:quoted  '(hash-map :a 1 :b 2)
;;               :literal (hash-map :a 1 :b 2)}]}]



;;  (? (apply array-map [js/Math 1]))
;;  (? js/Math)   

;;  (? (apply array-map [js/Math 1 js/JSON 2]))


;;  Lasertag TODO

;;  Figure out how to print js/Math like js/Math{...} consistently, whether it is alone or part of a collection
;;  Maybe you should detect :js :built-in :non-constructor, and cast it as a symbol like (symbol (str "js/Atomics"))
;;           and then if there is a user's symbol that is 'js/Atomics (conflicting as key or something) make sure it gets quoted, so internally it would be (symbol (str "'js/Atomics"))

;; Problem vals:
;; js/NaN
;; js/Infinity
;; js/globalThis
;; js/Intl.Segmenter (namespace dot thing)


;; Make sure this still works!
;; You need to use the make it work if the object is not coll-like, such as below

;; (? {(new js/Intl.Segmenter "fr" #js{:granularity "word"} "fr")
;;     (new js/Intl.Segmenter "de" #js{:granularity "word"} "de")})

;;  Would be sweet to show resolved options for js objects like above such as - how hard would this be? resolver fn to def in lasertag?
;; js/Intl.Segmenter
;; {'locale': "fr", 'granularity': "word"}

;;  Solve 2 fireworks issues

;;  Go through all the interops and create sample for each one, then author cached tag-map by hand

;;  Print hashmap contents of js-built-ins like you want - Math, JSON, Atomics, Intl, will not survive truncate

;;  Just use that for js/built-in logic

;;  Move the method-of-built-in stuff to another namespace

;;  Do the same thing for java built-ins

;;  Go back to control-flow and start there

;;  Do current approach, and try to get to exhaustive test suite

;;  The maybe consider the following api surface, if you can achive parity apart from tag-map approach
;;  - lasertag.tag     ; <- just for `tag`, lightweight
;;  - lasertag.tag-map ; <- just for `tag-map`, pulls in more stuff to deal with java and js values
;;  - lasertag.reflect ; <- just for cljs `built-in-method-of`, etc.



;;  js/WeakRef
;;  {:sym      WeakRef
;;   :args     [f]
;;   :category "Managing memory"
;;   :tag-map  {:tag       :function
;;              :type      js/Function
;;              :all-tags  #{:function :js :built-in}
;;              :fn-name   "WeakRef"
;;              :fn-args   [f]
;;              :classname "Function"}}

;;  js/FinalizationRe...
;;  {:sym      FinalizationRegistry
;;   :args     [f]
;;   :category "Managing memory"
;;   :tag-map  {:tag       :function
;;              :type      js/Function
;;              :all-tags  #{:function :js :built-in}
;;              :fn-name   "FinalizationRegistry"
;;              :fn-args   [f]
;;              :classname "Function"}}

;;  js/Proxy
;;  {:sym      Proxy
;;   :args     [...]
;;   :category "Reflection"
;;   :tag-map  {:tag       :function
;;              :type      js/Function
;;              :all-tags  #{:function :js :built-in}
;;              :fn-name   "Proxy"
;;              :fn-args   []
;;              :classname "Function"}}

;;  #_(? :no-file 
;;     (reduce-kv (fn [m built-in v]
;;                  (assoc m
;;                         built-in 
;;                         (assoc v
;;                                :cached-tag-map 
;;                                (lasertag/tag-map built-in))))
;;                {} 
;;                jsi/js-built-ins-by-built-in))


;;  (? :no-file (lasertag/tag-map 10))
;;  (? :no-file (lasertag/tag-map 100))
;;  (? :no-file (lasertag/tag-map 3.14))
;;  (? :no-file (lasertag/tag-map 42))
;;  (? :no-file (lasertag/tag-map 99.99M))
;;  (? :no-file (lasertag/tag-map (js/BigInt "999999999999")))
;;  (? :no-file (lasertag/tag-map ##Inf))
;;  (? :no-file (lasertag/tag-map ##-Inf))


;; Styling component elements with kushi.core/sx
;; ............................................................................

;; You can use kushi.core/sx to co-locate styles within your components.

;; Media queries, pseudo-classes, pseudo-elements, combo-selectors, and dynamic
;; runtime values are all supported.

;; kushi.core/sx is a macro that returns an attribute map.
;; This map contains:
;;   - a class property containing an auto-generated classname based on the
;;     namespace, line, and colomn number.


;; The build hook kushi.css.build.analyze/hook for the :compile-prepare
;; stage in shadow-cljs (or similar) will automatically transpile the values you
;; pass to the sx macro and generate the CSS to a static file.




;; SOME NOTES ON SYNTAX:

;; You can passed a quoted symbol as the first arg to defclass if you want to
;; give the generated class a specific name.
;; (sx 'myfirstclass :c--red :fs--3rem)

;; A keyword starting with a "." represents a classname that will get attached
;; to the element. Typically you would use this for predefined classes created
;; with defclass, but you can also use it to attach arbritary classes to the
;; element. Kushi ships with a small handful of useful pre-defined defclasses.

;; You can apply classes conditionally like so:
;; (when my-binding :.my-class)
;; (if my-binding :.my-class :.my-other-class)


;; A keyword containing "--" represents a css prop and value pair which is
;; split on the "--".

;; Kushi shorthand notation is optionally available for the most commonly
;; used css-props:
;;
;;     :c--red   => :color--red
;;     :ai--c    => :align-items--center
;;     :ai--e    => :align-items--end
;;     :ta--r    => :text-align--right
;;     :fs--18px => :font-size--18px
;;     :d--b     => :display--block
;;     :d--f     => :display--flex
;;     :bgs--50% => :background-size--50%
;;

;; A partial list of these is available here:

;; https://github.com/kushidesign/kushi#syntax


;; Standard CSS shorthand-values are written like this:
;;
;;     :b--1px:solid:#efefef
;;
;; The above is equivalent to `border: 1px solid #efefef`


;; CSS list-like values are written like this:
;;
;;     :ff--FiraCodeRegular|monospace|sans-serif
;;
;; The above is equivalent to:
;; `font-family: FiraCodeRegular, monospace, sans-serif`


;; Media queries, pseudo-classes, pseudo-elements, and combo selectors can be
;; used like this:
;;
;;     (sx sm:c--red
;;         sm:hover:c--blue
;;         :>a:hover:c--gold
;;         :_a:hover:c--gold)


;; In the example below, because "(" and ")" chars are not valid
;; in keywords, the 2-element tuple syntax is required, with
;; the prop being expressed as a string:
;;     (sx ["nth:child(2):c" :red])

;; You can also use a map instead of the 2-element syntax:
;;     (sx {"nth:child(2):c" :red})


;; The tuple (or map) syntax is also necessary for css functions and string values.
;;
;; Expressing a value as a css function:
;;     (sx [:transform "translateY(-100px)"]
;;         [:before:content "\"*\""])
;; 
;; Same as above, using a map
;;     (sx {:transform      "translateY(-100px)"
;;          :before:content "\"*\""})


;; (defn samples [& args]
;;   (let [[opts attrs & children] (opts+children args)
;;         {:keys [label]} opts]
;;    [:section
;;     attrs
;;     [:h2 
;;      (sx {:fs         :$size-xs 
;;           :font-style :italic
;;           :ff         :$serif-font-stack
;;           :mb         :1em})
;;      label]
;;     (into [:section
;;            (sx :.flex-row-fs
;;                :.examples
;;                {:gap       :1rem})]
;;           children)]))

;; (defn profile-card [full-name email]
;;   [:section.flex-col-fs 
;;    [card
;;     (sx :.elevated-3
;;         :dark:b--1px:solid:$neutral-700)
;;     [:div (sx :.flex-row-fs
;;               :.neutralize
;;               :ai--stretch
;;               :gap--0.8em)
;;      [:div (sx :.rounded
;;                :.transition
;;                :position--relative
;;                :overflow--hidden
;;                :bgc--$neutral-200
;;                :dark:bgc--$neutral-800
;;                :w--3.5em
;;                :h--3.5em)
;;       [:span (sx :.absolute-centered
;;                  [:transform "translate(0, 0.045em)"]
;;                  :display--block
;;                  :scale--2.55)
;;        "🐻‍❄"]]
;;      [:section (sx :.flex-col-sa) 
;;       [:p (sx :fs--1.25em :fw--$wee-bold) full-name] 
;;       [:p (sx :c--$secondary-foreground-color
;;               :dark:c--$secondary-foreground-dark-mode)
;;        email]]]]])


;; -----------------------------------------------------------------------------
;; OK MATRIX START -------------------------------------------------------------
;; -----------------------------------------------------------------------------

;; (def colors-from-0
;;   [:ruby
;;    :red
;;    :coral
;;    :orange
;;    :amber
;;    :apricot
;;    :gold
;;    :banana
;;    :yellow
;;    :citron    ; 100.1818   round2 -  98.181818
;;    :acid      ; 112.09     round2 -  109.090909 
;;    :lime      ; 123        round2 -  121.75     0.975
;;    :matcha    ; 132.9      round2 -  130.9090   0.95
;;    :green     ; 138.8181   round2 -  140.0      0.888
;;    :jade      ; 148.7272   round2 -  151        
;;    :emerald
;;    :mint
;;    :teal
;;    :cyan
;;    :aqua
;;    :glacier
;;    :azure
;;    :sky     ; 
;;    :blue    ; 250.9
;;    :lapis   ; 261.8181
;;    :indigo  ; 272.7272
;;    :iris    ; 284.636364
;;    :violet  ; 297.545455
;;    :purple  ; 309.4545
;;    :plum    ; 319.36
;;    :magenta ; 328.7
;;    :pink    ; 339.1818
;;    :rose])

;; (defn deformed-range2 [start end n]
;;   (let [range-size (- end start)
;;         num-points (inc range-size)
;;         strength (/ (double n) 100.0)]
;;     (for [i (range num-points)]
;;       (let [index (+ start i)
;;             t (/ i (double (dec num-points)))
;;             ;; Corrected: use a formula that preserves endpoints
;;             ;; deformed-t must be 0 when t=0 and 1 when t=1
;;             deformed-t (+ t (* strength t (- 1 t)))
;;             value (+ start (* deformed-t range-size))]
;;         [index value]))))

;; (defn vec-range-replace
;;   [v start-index coll]
;;   (let [end-index (+ start-index (count coll))]
;;     (vec (concat (subvec v 0 start-index)
;;                  coll
;;                  (subvec v end-index)))))

;; ;; Use to a scale for the lower bound of the chromatic scale, different at edges vs middle
;; (defn smooth-curve [n min-val max-val peak-position]
;;   (let [peak-idx (* peak-position (dec n))
;;         range-val (- max-val min-val)]
;;     (mapv (fn [i]
;;             (let [distance-before peak-idx
;;                   distance-after (- (dec n) peak-idx)
;;                   distance (if (<= i peak-idx)
;;                              (- peak-idx i)
;;                              (- i peak-idx))
;;                   max-distance (if (<= i peak-idx)
;;                                  distance-before
;;                                  distance-after)
;;                   ratio (if (zero? max-distance) 0 (/ distance max-distance))
;;                   curve-value (* range-val (- 1 (* ratio ratio)))]
;;               (+ min-val curve-value)))
;;           (range n))))

;; ;; exponential progression
;; (defn deformed-scale-2 [number-of-indices base-index shift]
;;   (let [shifted-index (+ base-index shift)
;;         ;; Invert the power so max effect is at shifted-index
;;         power-below (if (> shifted-index 0)
;;                       (/ 1.0 (+ 1.0 (/ (Math/abs (double shift)) 5.0)))
;;                       1.0)
;;         power-above (if (< shifted-index number-of-indices)
;;                       (/ 1.0 (+ 1.0 (/ (Math/abs (double shift)) 5.0)))
;;                       1.0)]
;;     (for [i (range number-of-indices)]
;;       (cond
;;         ;; Below or at the shifted index
;;         (<= i shifted-index)
;;         (let [t (if (> shifted-index 0)
;;                   (/ i (double shifted-index))
;;                   0.0)
;;               ;; Invert: 1 - curve gives max compression near shifted-index
;;               deformed-t (- 1.0 (Math/pow (- 1.0 t) power-below))]
;;           [i (* deformed-t base-index)])
        
;;         ;; Above the shifted index
;;         :else
;;         (let [distance (- i shifted-index)
;;               remaining (- (dec number-of-indices) shifted-index)
;;               t (/ distance (double remaining))
;;               ;; Start with max stretch, diminish toward edge
;;               deformed-t (- 1.0 (Math/pow (- 1.0 t) power-above))
;;               value-range (- (dec number-of-indices) base-index)]
;;           [i (+ base-index (* deformed-t value-range))])))))


;; ;; linear progression
;; (defn deformed-scale-linear [number-of-indices base-index shift]
;;   (let [shifted-index (+ base-index shift)]
;;     (for [i (range number-of-indices)]
;;       (cond
;;         ;; Below or at the shifted index
;;         (<= i shifted-index)
;;         (let [t (if (> shifted-index 0)
;;                   (/ i (double shifted-index))
;;                   0.0)]
;;           [i (* t base-index)])
        
;;         ;; Above the shifted index
;;         :else
;;         (let [distance (- i shifted-index)
;;               remaining (- (dec number-of-indices) shifted-index)
;;               t (/ distance (double remaining))
;;               value-range (- (dec number-of-indices) base-index)]
;;           [i (+ base-index (* t value-range))])))))


;; ;; Use to a scale for the lower bound of the chromatic scale, different at edges vs middle
;; (defn linear-peak [n min-val max-val peak-position]
;;   (let [peak-idx (* peak-position (dec n))
;;         range-val (- max-val min-val)]
;;     (mapv (fn [i]
;;             (let [distance-before peak-idx
;;                   distance-after (- (dec n) peak-idx)
;;                   ratio (if (<= i peak-idx)
;;                           (if (zero? distance-before) 1 (/ i distance-before))
;;                           (if (zero? distance-after) 1 (/ (- (dec n) i) distance-after)))
;;                   value (* range-val ratio)]
;;               (+ min-val value)))
;;           (range n))))

;; ;; Used for equalizer ?
;; (defn- progression-1 [n start end x]
;;   (if (<= n 1)
;;     [start]
;;     (let [x-clamped (max -1.0 (min 1.0 x))
;;           exponent  (if (zero? x-clamped)
;;                       1.0
;;                       (math/pow 2.0 (* 3.0 x-clamped)))]
;;       (vec (for [i (range n)]
;;              (let [t        (/ i (dec n))
;;                    curved-t (math/pow t exponent)
;;                    value    (+ start (* curved-t (- end start)))]
;;                value))))))

;; ;; Used for equalizer
;; (defn- progression 
;;   [n start end x & {:keys [curve-type]
;;                     :or   {curve-type :exponential}}]
;;   (if (<= n 1)
;;     [start]
;;     (let [x-clamped (max -1.0 (min 1.0 x))
;;           exponent  (case curve-type
;;                       :linear 1.0
;;                       :quadratic 2.0
;;                       :cubic 3.0
;;                       :exponential (if (zero? x-clamped)
;;                                      1.0
;;                                      (math/pow 2.0 (* 3.0 x-clamped)))
;;                       1.0)
;;           curve-fn  (if (or (zero? x-clamped) (= curve-type :linear))
;;                       identity
;;                       (if (pos? x-clamped)
;;                          ;; Ease-in: apply power directly
;;                         #(math/pow % exponent)
;;                          ;; Ease-out: apply power to (1-t) then flip
;;                         #(- 1.0 (math/pow (- 1.0 %) exponent))))]
;;       (vec (for [i (range n)]
;;              (let [t        (/ i (dec n))
;;                    curved-t (curve-fn t)
;;                    value    (+ start (* curved-t (- end start)))]
;;                value))))))


;; (defn indexes-with-shifted-by-color*
;;   [{:keys [coll tuning-coll]}]
;;   (let [hue-fr                  (->> coll count (/ 360))
;;         indexes-and-shifts-base (vec (map-indexed (fn [i _] [i i]) coll))]
;;     (map (fn [[i i-shifted] color-name]
;;            (let [hue         (* i hue-fr)
;;                  shifted-hue (* i-shifted hue-fr)
;;                  color-name  (name color-name)]
;;              [color-name [hue shifted-hue]]))

;;          (if tuning-coll
;;            (reduce (fn [coll [start-tweak-idx end-tweak-idx strength]]
;;                      (vec-range-replace coll
;;                                         start-tweak-idx
;;                                         (deformed-range2 start-tweak-idx
;;                                           end-tweak-idx strength)))
;;                    indexes-and-shifts-base
;;                    tuning-coll)
;;            indexes-and-shifts-base)
;;          coll)))

;; (defn indexes-by-color*
;;   [coll global-hue-shift]
;;   (let [hue-fr (->> coll count (/ 360))]
;;     (map-indexed (fn [i color-name]
;;                    (let [hue        (+ (* i hue-fr) global-hue-shift)
;;                          color-name (name color-name)]
;;                      [color-name i hue]))
;;                  coll)))

;; (defn reduced-into-array-map [f coll]
;;   (apply array-map (reduce f [] coll)))


;; ;; DEFS ------------------------------------------------------------------------
;; (defonce S (reagent.ratom/atom {}))

;; (def global-hue-shift 0)

;; (def indexes-by-color
;;   (!? (indexes-by-color* colors-from-0 global-hue-shift)))

;; (def MAX_CHROMA_REC2020 0.467)

;; #_(def gamut-mapping
;;     {"srgb"       "rgb"
;;      "display-p3" "p3"
;;      "p3"         "p3"
;;      "rec2020"    "rec2020"})


;; (def lightness-range (range 25 1000 25))

;; (def max-chroma-state-ceilings (atom {}))

;; (def max-chroma-lowest-ceiling (atom nil))

;; (def swatches (atom []))

;; (def chroma-floor-max 0.05)

;; (def hue-tail-shift-range 80)
            
;; (def hue-shift-range 15)

;; (def chroma-tail-shift-range 0.1)

;; (def chroma-tail-shift-step 0.005)

;; ;; DEFS ------------------------------------------------------------------------
 

;; (defn- swap-max-chroma! [level chroma k i hue color-name]
;;   (swap! max-chroma-state-ceilings
;;          update-in
;;          [level k]
;;          #(if (or (nil? %) (< chroma (:c %)))
;;             {:i     i
;;              :hue   hue
;;              :color color-name
;;              :c     chroma}
;;             %))
;;   (reset! max-chroma-lowest-ceiling
;;           (let [cur @max-chroma-lowest-ceiling]
;;             (if (or (nil? cur) (< chroma (:c cur)))
;;               {:i     i
;;                :hue   hue
;;                :color color-name
;;                :level level
;;                :c     chroma}
;;               cur))))

;; (defn- clamped-chroma [l h gamut i hue color-name level]
;;   (let [chroma (some-> (clampChroma #js{:mode "oklch"
;;                                         :l    l
;;                                         :c    MAX_CHROMA_REC2020
;;                                         :h    h}
;;                                     "oklch"
;;                                     gamut)
;;                        .-c)]
;;     (swap-max-chroma! level chroma gamut i hue color-name)
;;     chroma))

;; (defn lightness-level [i hue color-name acc level]
;;   (let [l       (- 1 (/ level 1000))
;;         rgb     (clamped-chroma l hue "rgb" i hue color-name level)
;;         p3      (clamped-chroma l hue "p3" i hue color-name level)
;;         rec2020 (clamped-chroma l hue "rec2020" i hue color-name level)]
;;     (conj acc
;;           level
;;           {:hue-index  i
;;            :hue        hue
;;            :color      color-name
;;            :max-chroma {:rgb     rgb
;;                         :p3      p3
;;                         :rec2020 rec2020}
;;            :l          l
;;            :l-level    level})))

;; (defn- oklch-color-css [{:keys [l c h]}]
;;   #_(str "oklch(" l " " c " " h ")")
;;   (str "oklch(" l " calc(var(--chroma-factor, 1) * " c ") "  "var(--hue, " h "))"))

;; (defn ->array-map [& colls]
;;   (apply array-map (apply concat colls)))

;; (defn lightness-level-2 [i hue color-name by-color chroma-floors-by-level acc level]
;;   (!? :pp (get-in by-color [color-name]))
;;   (let [debug? (or (= ["red" 25] [color-name level])
;;                    (= ["cyan" 25] [color-name level]))]
;;     (conj acc
;;           level
;;           (let [m                (get-in by-color [color-name :l-levels level])
;;                 c-floor          (get chroma-floors-by-level level)
;;                 max-chroma-rgb   (get-in m [:max-chroma :rgb])
;;                 soft-scale       ["soft-3" "soft-2" "soft" "medium"]
;;                 soft-scale-count (count soft-scale)
;;                 chroma-step      (/ (- max-chroma-rgb c-floor) soft-scale-count)
;;                 medium-to-floor  (reverse
;;                                   (mapv vector soft-scale
;;                                         (if (pos? chroma-step)
;;                                           (range c-floor
;;                                                  max-chroma-rgb
;;                                                  chroma-step)
;;                                           (repeat soft-scale-count c-floor))))
;;                 c-variant        {:l (:l m)
;;                                   :h hue}
;;                 f                #(assoc c-variant :c %)
;;                 soft-c-variants  (reduce (fn [vc [k c]]
;;                                            (conj vc k (f c)))
;;                                          []
;;                                          medium-to-floor)]
;;             (assoc
;;              m
;;              :c-floor
;;              c-floor
;;              :c-variants (->array-map
;;                           ["hard-3"
;;                            (f (get-in m [:max-chroma :rec2020]))

;;                            "hard-2"
;;                            (f (get-in m [:max-chroma :p3]))

;;                            "hard"
;;                            (f max-chroma-rgb)]
;;                           soft-c-variants))))))



;; (defn- spectrum-at-chroma 
;;   [padding-px swatch-width swatch-height by-color-2 chromatic-level]
;;   (into 
;;    [:div.spectrum-at-chroma
;;     {:style {:padding-inline   padding-px
;;              :width            :fit-content
;;              :background-image (str "linear-gradient(to left in oklch, oklch(0 0 0) calc(" padding-px " + " swatch-width "px), oklch(1 0 0) calc(100% - (" padding-px " + " swatch-width "px)))")
;;              }}]
;;    (let [rows
;;          (for [[color-name m] by-color-2]
;;            (into [:div (merge-attrs (sx ".swatch-row"
;;                                         {:display  :flex
;;                                          :position :relative
;;                                          "--hue"     :$hue
;;                                          :before   {:content   :$color-name
;;                                                     :color     :$gray-600
;;                                                     :position  :absolute
;;                                                     :top       "calc(var(--swatch-height) / 2)"
;;                                                     :translate :0:-50%
;;                                                     :right     "calc(100% + 20px)"}})
;;                                     {:class [color-name :swatch-row]
;;                                      :style {"--hue"           (:base-hue m)
;;                                              "--swatch-height" (str swatch-height "px")
;;                                              "--chroma-factor" 1
;;                                              "--color-name"    (str "\"" color-name "\"")}})]
;;                  (for [[level {{chromatic-m chromatic-level} :c-variants
;;                                :as                           m2}]
;;                        (:l-levels m)]
;;                    (let [okmatrix-name
;;                          (str color-name "-"
;;                               level
;;                               (when-not (= "medium"
;;                                            chromatic-level)
;;                                 (str "-" chromatic-level)))]
;;                      (swap! swatches conj okmatrix-name)
;;                      [:div {:data-okmatrix okmatrix-name
;;                             :data-25-500   (when (<= level 500) "")
;;                             :data-500-975  (when (>= level 500) "")
;;                             :data-l        (:l chromatic-m)
;;                             :data-h        (:h chromatic-m)
;;                             :data-c        (:c chromatic-m)
;;                             :style         {:width            (str swatch-width "px")
;;                                             :height           (str swatch-height "px")
;;                                             :background-color (oklch-color-css 
;;                                                                chromatic-m)}}]))))]
;;      (into rows
;;            (reverse rows)))))

;; (defn- labeled-vertical-slide [{:keys [label level color-name k]} opts]
;;   [:div
;;    (sx2 {:display                     :flex-col
;;          "[data-l-level$=\"25\"]:not([data-l-level=\"25\"]):display" :none
;;          "[data-l-level$=\"75\"]:not([data-l-level=\"75\"]):display" :none
;;          :data-l-level                level
;;          :flex-direction              :column
;;          :align-content               :center
;;          :width                       :4ch
;;          :height                      :100%})
;;    [:span (sx2 {:width   :4ch
;;                 :ta      :c
;;                 ;; :opacity :0.5
;;                 :height  :fit-content})
;;     label]
;;    [:div
;;     (sx2 {:max-width   :5ch
;;           :flex-grow   1
;;           :flex-shrink 0})
;;     [slider (merge-attrs
;;              {:wrapper-attrs                (sx2 {:w           :150px
;;                                                   :flex-grow   1
;;                                                   :flex-shrink 0
;;                                                   :translate   :0.5ch})
;;               :default-value                (or (get-in @S [color-name k]) 0)
;;               :value                        (get-in @S [color-name k])
;;               :step                         1 
;;               :display-current-value-label? false
;;               :current-value-label-offset   :0em
;;               :orientation                  :vertical}
;;              (when-not (:on-change opts)
;;                {:on-change #(swap! S
;;                                    assoc-in
;;                                    [color-name k] 
;;                                    (-> % domo/cetv js/parseFloat))})
;;              opts)]]])

;; (defn- chroma-equalizer [coll chroma-variant-name color-name]
;;   [:div (sx2 {:display               :flex-row-space-between
;;               :ai                    :c
;;               :w                     :100%
;;               :h                     :600px
;;               " [data-l-level]>span:d" :none})

;;    (into [:div (sx2 {:display        :flex-row
;;                      :flex-direction :row-reverse
;;                      :jc             :space-around
;;                      :ai             :c
;;                      :height         :200px
;;                                    ;; :border  :1px:solid:$gray-800
;;                      })]
;;          (map-indexed (fn [i [level]]
;;                         #_(? :pp (get-in coll [level :c-variants chroma-variant-name]))
;;                         (let [{:keys [l h c]}        (get-in coll [level :c-variants chroma-variant-name])
;;                               base-chroma-at-level   c
;;                               tail-shift             0
;;                               with-tail-shift        (+ base-chroma-at-level tail-shift)
;;                               manual-shift           (or (get-in @S [color-name level :c/manual-shift]) 0)
;;                               with-tail+manual-shift (+ with-tail-shift manual-shift)
;;                               min                    (- base-chroma-at-level chroma-tail-shift-range)
;;                               max                    (+ base-chroma-at-level chroma-tail-shift-range)]

;;                           #_{:level                  level
;;                              :base-hue               base-hue
;;                              :tail-shift             tail-shift
;;                              :with-tail-shift        with-tail-shift
;;                              :manual-shift           manual-shift
;;                              :with-tail+manual-shift with-tail+manual-shift
;;                              :min                    (- base-hue 30)
;;                              :max                    (+ base-hue 30)
;;                              }

;;                           [labeled-vertical-slide
;;                            {:label level
;;                             :level level}
;;                            {
;;                             :min                              min
;;                             :max                              max
;;                             :step                             chroma-tail-shift-step
;;                             :on-change                        #(swap! S
;;                                                                       assoc-in
;;                                                                       [color-name level :c/manual-shift] 
;;                                                                       (-> % domo/cetv js/parseFloat (- with-tail-shift)))
;;                             :default-value                    with-tail+manual-shift
;;                             :value                            with-tail+manual-shift
;;                             :display-current-value-label?     true
;;                             :current-value-label-display-mode :visible-on-interaction
;;                             :current-value-label-position     :track-inline-end
;;                             }]))
;;                       coll))

;;    #_[:div (sx2 {:display   :flex-row-space-around
;;                  :flex-grow 1
;;                  :pi        :2rem
;;                  :height    :200px})
      
;;       [labeled-vertical-slide
;;        {:label      "t"
;;         :color-name color-name
;;         :k          :hue-shift-progression-type}
;;        {:min                          0
;;         :max                          3
;;         :step                         1    
;;         :on-change                    #(swap! S
;;                                               assoc-in
;;                                               [color-name :hue-shift-progression-type] 
;;                                               (-> % domo/cetv js/parseInt))
;;         :display-current-value-label? true}]
      
;;       [labeled-vertical-slide
;;        {:label      "f"
;;         :color-name color-name
;;         :k          :hue-shift-progression-factor}
;;        {:min                          -1
;;         :max                          1
;;         :step                         0.1    
;;         :display-current-value-label? true}]

;;       [labeled-vertical-slide
;;        {:label      "s2"
;;         :color-name color-name
;;         :k          :hue-shift-tail}
;;        {:min                          (- tail-shift-range)
;;         :max                          tail-shift-range
;;         :step                         1     
;;         :display-current-value-label? true}]
      
;;       [labeled-vertical-slide
;;        {:label      "s1"
;;         :color-name color-name
;;         :k          :hue-shift}
;;        {:min                          (- shift-range)
;;         :max                          shift-range
;;         :step                         0.125     
;;         :display-current-value-label? true}]]])

;; (defn- hue-equalizer [coll chroma-variant-name color-name base-hue]
;;   [:div (sx2 {:display :flex-row-space-between
;;                        :gap     :25px
;;                        :ai      :c
;;                        :w       :100%
;;                        :h       :600px})

;;             (into [:div (sx2 {:display        :flex-row
;;                               :flex-direction :row-reverse
;;                               :jc             :space-around
;;                               :ai             :c
;;                               :height         :200px
;;                                    ;; :border  :1px:solid:$gray-800
;;                               })]
;;                   (map-indexed (fn [i [level]]
;;                                  (let [{:keys [l h c]}        (get-in coll [level :c-variants chroma-variant-name])
;;                                        tail-shift             (get-in @S [color-name level :h/tail-shift])
;;                                        with-tail-shift        (+ base-hue tail-shift)
;;                                        manual-shift           (or (get-in @S [color-name level :h/manual-shift]) 0)
;;                                        with-tail+manual-shift (+ with-tail-shift manual-shift)
;;                                        min                    (- base-hue hue-tail-shift-range)
;;                                        max                    (+ base-hue hue-tail-shift-range)]

;;                                    #_{:level                  level
;;                                       :base-hue               base-hue
;;                                       :tail-shift             tail-shift
;;                                       :with-tail-shift        with-tail-shift
;;                                       :manual-shift           manual-shift
;;                                       :with-tail+manual-shift with-tail+manual-shift
;;                                       :min                    (- base-hue 30)
;;                                       :max                    (+ base-hue 30)
;;                                       }

;;                                    [labeled-vertical-slide
;;                                     {:label level
;;                                      :level level}
;;                                     {
;;                                      :min                              min
;;                                      :max                              max
;;                                      :step                             0.125
;;                                      :on-change                        #(swap! S
;;                                                                                assoc-in
;;                                                                                [color-name level :h/manual-shift] 
;;                                                                                (-> % domo/cetv js/parseFloat (- with-tail-shift)))
;;                                      :default-value                    with-tail+manual-shift
;;                                      :value                            with-tail+manual-shift
;;                                      :display-current-value-label?     true
;;                                      :current-value-label-display-mode :visible-on-interaction
;;                                      :current-value-label-position     :track-inline-end
;;                                      }]))
;;                                coll))
;;             [:div (sx2 {:display   :flex-row-space-around
;;                         :flex-grow 1
;;                         :pi        :2rem
;;                         :height    :200px})
             
;;              [labeled-vertical-slide
;;               {:label      "t"
;;                :color-name color-name
;;                :k          :hue-shift-progression-type}
;;               {:min                          0
;;                :max                          3
;;                :step                         1    
;;                :on-change                    #(swap! S
;;                                                      assoc-in
;;                                                      [color-name :hue-shift-progression-type] 
;;                                                      (-> % domo/cetv js/parseInt))
;;                :display-current-value-label? true}]
             
;;              [labeled-vertical-slide
;;               {:label      "f"
;;                :color-name color-name
;;                :k          :hue-shift-progression-factor}
;;               {:min                          -1
;;                :max                          1
;;                :step                         0.1    
;;                :display-current-value-label? true}]

;;              [labeled-vertical-slide
;;               {:label      "s2"
;;                :color-name color-name
;;                :k          :hue-shift-tail}
;;               {:min                          (- hue-tail-shift-range)
;;                :max                          hue-tail-shift-range
;;                :step                         1     
;;                :display-current-value-label? true}]
             
;;              [labeled-vertical-slide
;;               {:label      "s1"
;;                :color-name color-name
;;                :k          :hue-shift}
;;               {:min                          (- hue-shift-range)
;;                :max                          hue-shift-range
;;                :step                         0.125     
;;                :display-current-value-label? true}]]])


;; (defn- okm-spectrum []
;;   (let [by-color               (reduced-into-array-map
;;                                 (fn [acc [color-name i hue]]
;;                                   (conj acc
;;                                         color-name
;;                                         {:hue-index i
;;                                          :base-hue  hue
;;                                          :color     color-name
;;                                          :l-levels  (reduced-into-array-map
;;                                                      (partial lightness-level i hue color-name)
;;                                                      lightness-range)}))
;;                                 indexes-by-color)

;;         chroma-floor-min       (:c @max-chroma-lowest-ceiling)

;;         chroma-floors-by-level (reduce-kv
;;                                 (fn [m i v]
;;                                   (assoc m (nth lightness-range i) v))
;;                                 {}
;;                                 (linear-peak (count lightness-range)
;;                                              chroma-floor-min
;;                                              chroma-floor-max
;;                                              0.5))

;;         by-color-2             (reduced-into-array-map
;;                                 (fn [acc [color-name i hue]]
;;                                   (conj acc
;;                                         color-name
;;                                         {:hue-index i
;;                                          :base-hue  hue
;;                                          :color     color-name
;;                                          :l-levels  (reduced-into-array-map
;;                                                      (partial lightness-level-2
;;                                                               i
;;                                                               hue
;;                                                               color-name
;;                                                               by-color
;;                                                               chroma-floors-by-level)
;;                                                      lightness-range)}))
;;                                 indexes-by-color)]

;;     ;; (? :+ (get by-color "red"))
    
;;     (!? :+ (get-in by-color ["cyan" :l-levels 25]))
;;     (!? :+ (get-in by-color ["red" :l-levels 25]))

;;     (!? :+ (get-in by-color-2 ["cyan" :l-levels 25]))
;;     (!? :+ (get-in by-color-2 ["red" :l-levels 25]))

;;     (!? :+ @max-chroma-state-ceilings)
;;     (!? :+ max-chroma-lowest-ceiling)


;;     (let [swatch-width  9.5 
;;           swatch-height 30
;;           padding-ratio 2
;;           padding-px    (str (* swatch-width padding-ratio) "px")

;;           ;; lights and darks
;;           just-lights   (sx ".just-lights"
;;                             {:>div:pi                 0
;;                              " [data-okmatrix]:display" :none
;;                              " [data-25-500]:display"   :block})
          
;;           just-darks    (sx  ".just-darks"
;;                              {:selector                "just-darks"
;;                               :>div:pi                 0
;;                               " [data-okmatrix]:display" :none
;;                               " [data-500-975]:display"  :block})
;;           padding-px    (str swatch-width "px") 

;;           ;; level relaxation
;;           loose-steps   (sx ".loose-steps"
;;                             {:>div:pi                                0
;;                              " [data-okmatrix]:nth-child(odd):display" :none})
          

;;           swatch-width  20
;;           ;; -------------------------------------------------------------------
          
;;           #_return        #_(into [:div (merge-attrs
;;                                          {:data-ks-display  :flex-row-center
;;                                           :data-ks-position :fixed-fill}
;;                                          (sx ".okm-spectrum-colls"
;;                                              {:display                       :flex-row-center
;;                                               :ai                            :flex-start
;;                                               ">div:nth-child(even):transform" "scaleX(-1)"
;;                                               ;;  " .spectrum-at-chroma:bgi"       "none!important"
;;                                               :bgi                           "linear-gradient(to left, black 50%, white 50% 100%)"
;;                                               :position                      :fixed-fill})

;;                                          ;; just-darks
;;                                          ;; {:style {:background-image :none
;;                                          ;;           :background-color :black}}
                                         
;;                                          ;; just-lights
;;                                          ;; {:style {:background-image :none
;;                                          ;;          :background-color "#d9d9d9"}}
                                         
;;                                          loose-steps)]
;;                                   [
;;                                    ;;  (spectrum-at-chroma padding-px swatch-width swatch-height by-color-2 "hard-3")
;;                                    ;;  (spectrum-at-chroma padding-px swatch-width swatch-height by-color-2 "hard-2")
;;                                    ;;  (spectrum-at-chroma padding-px swatch-width swatch-height by-color-2 "hard")
;;                                    ;;  (spectrum-at-chroma padding-px swatch-width swatch-height by-color-2 "hard")
;;                                    (spectrum-at-chroma padding-px swatch-width swatch-height by-color-2 "medium")
;;                                    (spectrum-at-chroma padding-px swatch-width swatch-height by-color-2 "medium")
;;                                    ;;  (spectrum-at-chroma padding-px swatch-width swatch-height by-color-2 "soft-2")
;;                                    ;;  (spectrum-at-chroma padding-px swatch-width swatch-height by-color-2 "soft-2")
;;                                    ;;  (spectrum-at-chroma padding-px swatch-width swatch-height by-color-2 "soft-3")
;;                                    ])
;;           ]



;;       (let [color-name                               "citron"

;;             chroma-variant-name                      "hard"

;;             {:keys [l-levels base-hue]
;;              :as   color-m} (get by-color-2 color-name)

;;             coll                                     l-levels 

;;             coll-count                               (count coll)

;;             curve-type-kw                            #(nth [:linear :quadratic :cubic :exponential] %)

;;             curve-type-label-display                 #(nth ["l" "q" "c" "e"] %)

;;             linear-tail                              (progression coll-count 
;;                                                                   base-hue
;;                                                                   (+ base-hue
;;                                                                      (or (get-in @S
;;                                                                                  [color-name :hue-shift-tail])
;;                                                                          0))
;;                                                                   (or (get-in @S
;;                                                                               [color-name :hue-shift-progression-factor])
;;                                                                       0)
                                                                  
;;                                                                   :curve-type
;;                                                                   (curve-type-kw 
;;                                                                    (or (get-in @S
;;                                                                                [color-name :hue-shift-progression-type])
;;                                                                        0)))

;;             ;; curved-tail
;;             ;; (? (deformed-scale-2 coll-count (/ coll-count 2) 0.5))
            
;;             shifted-vals                             (mapv #(+ % 0 #_(get-in @S [color-name :hue-shift]))
;;                                                            linear-tail)]

;;         ;; Add tail shifts to state
;;         (doseq [[l] coll]
;;           (swap! S
;;                  assoc-in
;;                  [color-name l :h/tail-shift]
;;                  (- (nth shifted-vals (dec (/ l 25 )))
;;                     base-hue)))
;;         [:div (sx2 {:position :fixed-fill})
;;          [:div (sx2 {:display  :flex-row
;;                      :position :fixed-fill
;;                      :>div:h   :100%})
;;           #_[:div (sx2 {:bgc       :black
;;                         :flex-grow 1})]
;;           [:div (sx2 {:bgi         "linear-gradient(to right, oklch(0 0 0), oklch(1 0 0) 90%)"
;;                       ;; :bgc         :$gray-800
;;                       :bgc         :white
;;                       :w           :1000px
;;                       :flex-grow   0
;;                       :flex-shrink 1})]
;;           [:div (sx2 {:bgc       :white
;;                       :flex-grow 1})]]
;;          [:div (sx2 {:display  :flex-row
;;                      :gap      :2rem
;;                      :position :absolute-inline-start-inside
;;                      :w        :2000px
;;                      :h        :700px})
;;           (into [:div (sx2 {:h              :200px
;;                             :w              :1000px
;;                             :display        :flex-row
;;                             :flex-direction :row-reverse})]
;;                 (for [[level {c-variants :c-variants}] coll]
;;                   (let [{:keys [l c h]} (get c-variants chroma-variant-name)]
;;                     [:div (merge-attrs (sx2 {"[data-l-level$=\"25\"]:not([data-l-level=\"25\"]):display" :none
;;                                              "[data-l-level$=\"75\"]:not([data-l-level=\"75\"]):display" :none})
;;                                        {:data-l-level level
;;                                         :style        {:height           :100%
;;                                                        :flex-shrink      0
;;                                                        :flex-grow        1
;;                                                        :background-color (oklch-color-css 
;;                                                                           {:l l
;;                                                                            :c (+ c
;;                                                                                  (or (get-in @S [color-name level :c/manual-shift]) 0))
;;                                                                            :h (+ h
;;                                                                                  (or (get-in @S [color-name level :h/manual-shift]) 0)
;;                                                                                  (or (get-in @S [color-name level :h/tail-shift]) 0)
;;                                                                                  (or (get-in @S [color-name :hue-shift]) 0))})}})])))
;;           [:div (sx2 {:font-size   :$font-size-xs
;;                       :weight :$font-weight-extra-light
;;                       :display     :flex-col
;;                       :translate   :0:100px
;;                       :bgc         :white
;;                       :c           :$gray-800
;;                       :w           :1000px
;;                       :h           :400px})

;;            ;; hue controls
;;            (hue-equalizer coll chroma-variant-name color-name base-hue)

;;            ;; chroma controls
;;            (chroma-equalizer coll chroma-variant-name color-name)]]]))))


;; -----------------------------------------------------------------------------
;; OK MATRIX END -------------------------------------------------------------
;; -----------------------------------------------------------------------------


#_(defn- type-sample [m s]
  [:p m "Foo bar baz" [:span {:style {:font-size :12px} } s]])



(defn main-view []
  #_(js/setTimeout 
     (fn [] 
       #_(js/console.clear)
       #_(domo-examples)

       (let [
             ;;  cs (js/window.CanvasSpliner.CanvasSpliner. "parent" 500 500 )
             cs (new CanvasSpliner "parent" 500 500)
             ]

         ;;  (js/console.log (new (aget CanvasSpliner "CanvasSpliner") "parent" 500 500))
         ;;  (js/console.log cs)
         
         ;;  (js/console.log (.-_width cs))
         
         ;;  #_(js/console.log #js{:x       0,
         ;;                        :y       0,
         ;;                        :xLocked true, 
         ;;                        :yLocked true,
         ;;                        :safe    true})
         
         (.add cs #js{:x 0
                      :y 0})
         
         (.add cs #js {:x 0.1
                       :y 0.4} );
         (.add cs #js {:x 0.3
                       :y 0.45} );
         (.add cs #js {:x 0.6
                       :y 1} );
         ;; (cs.add #js {:x 1, :y 0.6, :xLocked true, :safe true} )
         ))
     1000)


  ; try to get the system working with sx2
  ; what's up with validator stub?
  ;; 
  
  ;; Try a blank line above highlighted value
  ;; Try highlighting map key with blue if bad value
  ;; Try languaging something about the key
  ;; Maybe Invalid value supplied to: sx2 should be : -- Invalid value supplied for :size
  ;; Try kushi.core/sx2 above thing
  #_(? (malli/validate [:or
                        [:float {:min 0.0
                                 :max 1.0}]
                        [:enum 0 1]
                        [:and 
                         [:or :string :keyword]
                         [:fn (fn [x] (re-find #"^0\%$|^100%$|^[0-9][0-9]?(?:\.[0-9]+)?$" (name x)))]]]
                       :100%))
  
  ;; (def highlight-error-dark-sgr
  ;; "\033[38;2;255;224;224;1;48;2;103;0;19m")
  
  ;; (def highlight-error-light-sgr
  ;; "\033[38;2;102;0;0;1;48;2;255;219;219m" )
  
  ;; (def highlight-universal-sgr
  ;; "\033[38;2;255;255;255;1;48;2;138;138;138m")
  
  ;; (? (re-pattern 
  ;;       (str "\\033\\[38;2;255;224;224;1;48;2;103;0;19m"kkjjj
  ;;            "((?:(?!\\033).)+)" 
  ;;            "\\033\\[0?m"
  ;;            )))
  
  (js/console.clear)


  #_(callout {:type         :warning
              :label-theme  :tab
              :label        "Hey"
              :border-shape :round} 
             (bling.hifi/hifi {:a "asdfsadfasfasf"
                               :b "asfdasdfasdfasdf"
                               :c "adfdsafasdfasfas"}))
  [:div [:div.flex-col-center.items-center.absolute-center
         (let [wght :bold]
           (sx2 {
                    ;;  :style {:go []}
                 :shape           :pill
                 :colorway        :green
                 
                     :stroke-width    :5px        
                    :stroke-color    :orange
                     :stroke-align    :inside      
                     :stroke-opacity  :50%   
                 
                 :padding         :1rem
                    ;;  :shadow-size     :xl
                ;;  :shadow-opacity  :100%
                    ;;  :shadow-color    :yellow
                 :data-ks-surface :convex
                 :size            :xl

                    ;; :letter-spacing :$letter-spacing-base
                    ;; :letter-spacing :$letter-spacing-base
                    ;; :border-radius  :$shape-rounded-3xl
                 
                 :weight          wght
                    ;; :icon-style :ffed
                    ;;  []          :2xlw
                 
                 :width           :350px
                    ;;  :height         :100px
                    ;; :border         :1px:solid:red
                 }
                #_foo))
         "See"]]
  

  #_[:div#parent (sx2 {:display :flex-col})
     [:div (sx2 {:display :flex-row-space-around
                 :_span   {:pis   :8px
                           :color :$red-300}})
      [:div
       [:p {:style {:color  :$red-500
                    :weight :bold}} "data-font-size"]
       [type-sample {:data-ks-font-size :3xs} "3xs"]
       [type-sample {:data-ks-font-size :2xs} "2xs"]
       [type-sample {:data-ks-font-size :xs} "xs"]
       [type-sample {:data-ks-font-size :sm} "sm"]
       [type-sample {:data-ks-font-size :base} "md"]
       [type-sample {:data-ks-font-size :lg} "lg"]
       [type-sample {:data-ks-font-size :xl} "xl"]
       [type-sample {:data-ks-font-size :2xl} "2xl"]
       [type-sample {:data-ks-font-size :3xl} "3xl"]]


      #_[:div [:p {:style {:color  :$red-500
                           :weight :bold}} "utility classes"]
         [:p.3xs "Foo bar baz" [:span {:style {:font-size :12px}} "3xs"]]
         [:p.2xs "Foo bar baz" [:span {:style {:font-size :12px}} "2xs"]]
         [:p.xs "Foo bar baz" [:span {:style {:font-size :12px}} "xs"]]
         [:p.sm "Foo bar baz" [:span {:style {:font-size :12px}} "sm"]]
         [:p.md "Foo bar baz" [:span {:style {:font-size :12px}} "md"]]
         [:p.lg "Foo bar baz" [:span {:style {:font-size :12px}} "lg"]]
         [:p.xl "Foo bar baz" [:span {:style {:font-size :12px}} "xl"]]
         [:p.2xl "Foo bar baz" [:span {:style {:font-size :12px}} "2xl"]]
         [:p.3xl "Foo bar baz" [:span {:style {:font-size :12px}} "3xl"]]]
      

      #_[:div [:p {:style {:color  :$red-500
                           :weight :bold}} "utility classes weight"]
         [:p.xl.thin "Foo bar baz" [:span {:style {:font-size :12px}} "thin"]]
         [:p.xl.extra-light "Foo bar baz" [:span {:style {:font-size :12px}} "extra-light"]]
         [:p.xl.light "Foo bar baz" [:span {:style {:font-size :12px}} "light"]]
         [:p.xl.normal "Foo bar baz" [:span {:style {:font-size :12px}} "normal"]]
         [:p.xl.wee-bold "Foo bar baz" [:span {:style {:font-size :12px}} "wee-bold"]]
         [:p.xl.semi-bold "Foo bar baz" [:span {:style {:font-size :12px}} "semi-bold"]]
         [:p.xl.bold "Foo bar baz" [:span {:style {:font-size :12px}} "bold"]]
         [:p.xl.extra-bold "Foo bar baz" [:span {:style {:font-size :12px}} "extra-bold"]]
         [:p.xl.heavy.absolute-center "Foo bar baz" [:span {:style {:font-size :12px}} "heavy"]]]]
     
     
     #_[:div (sx2 {:display :flex-row-space-around
                   :_span   {:pis   :8px
                             :color :$red-300}})

        [:div
         [:p {:style {:color  :$red-500
                      :weight :bold}}
          "css-var"]
         [type-sample (sx2 {:font-size :$text-3xs}) "3xs"]
         [type-sample (sx2 {:font-size :$text-2xs}) "2xs"]
         [type-sample (sx2 {:font-size :$text-xs}) "xs"]
         [type-sample (sx2 {:font-size :$text-sm}) "sm"]
         [type-sample (sx2 {:font-size :$text-md}) "md"]
         [type-sample (sx2 {:font-size :$text-lg}) "lg"]
         [type-sample (sx2 {:font-size :$text-xl}) "xl"]
         [type-sample (sx2 {:font-size :$text-2xl}) "2xl"]
         [type-sample (sx2 {:font-size :$text-3xl}) "3xl"]]
        
        [:div
         [:p {:style {:color  :$red-500
                      :weight :bold}}
          "css-var b"]
         [type-sample (sx2 {:font-size :$text-3xs-b}) "3xs"]
         [type-sample (sx2 {:font-size :$text-2xs-b}) "2xs"]
         [type-sample (sx2 {:font-size :$text-xs-b}) "xs"]
         [type-sample (sx2 {:font-size :$text-sm-b}) "sm"]
         [type-sample (sx2 {:font-size :$text-md-b}) "md"]
         [type-sample (sx2 {:font-size :$text-lg-b}) "lg"]
         [type-sample (sx2 {:font-size :$text-xl-b}) "xl"]
         [type-sample (sx2 {:font-size :$text-2xl-b}) "2xl"]
         [type-sample (sx2 {:font-size :$text-3xl-b}) "3xl"]]]
     ]

  #_(okm-spectrum)

  #_[:main
     (sx {"--nav-height"            :70px
          "--padding-inline"        :2rem
          "--padding-inline-mobile" :1rem
          "--padding-block"         :1rem
          :weight                 :$weight-light})

     [:nav.grid.transition
      (sx {:gtc      :1fr:1fr
           :sm:gtc   :1fr:1fr:1fr
           :bgc      :white
           :dark:bgc :$background-color-dark-mode
           :zi       1000
           :ai       :c
           :position :fixed
           :top      :0
           :left     :0
           :right    :0
           :h        :$nav-height
           :sm:pi    :$padding-inline
           :pi       :$padding-inline-mobile
           :pb       :$padding-block})
      [:h1 (sx :.base :.semi-bold)
       "Kushi Quickstart"]
      [badges/links (sx {:d    :none
                         :sm:d :flex})]
      [:div (sx :.flex-row-sb
                :sm:jc--fe)
       [badges/links (sx {:d    :flex
                          :sm:d :none})]
       [light-dark-mode-switch]]]


     [:main.flex-row-c
      [:section.flex-col-fs.sm
       (sx {:ai                :c
            :width             "calc(320px + (var(--padding-inline-mobile) * 2))"
            :md:flex-direction :row
            :sm:width          "calc((320px * 2 ) + var(--padding-inline) + (var(--padding-inline) * 2))"
            :xl:width          :1200px
            :flex-wrap         :wrap
            :gap               :2rem
            :sm:pi             :$padding-inline
            :pi                :$padding-inline-mobile
            :mbs               :$nav-height
            :pb                :4rem})

       ;; First grouping of various UI component examples
       [:section (sx :.flex-col-fs
                     :gap--1rem
                     :width--320px
                     :>section:gap--0.5em)

        ;; Text field and search
        [:section (sx :.flex-row-fs
                      :ai--fe
                      :>button:h--34px
                      :>.kushi-input:flex-grow--1
                      :>.kushi-input:flex-shrink--0)
         [text-field
          (merge-attrs
           {:-start-enhancer [icon :search]
            :placeholder     "Search"})]
         [button
          (merge-attrs
           {:-surface  :solid
            :-colorway :neutral})
          "Search"]]

        ;; Callouts
        (into [:section.flex-col-fs]
              (for [surface [:soft :solid :outline]]
                [callout
                 (merge-attrs
                  {:-surface     surface
                   :-icon        [icon (if (contains? #{:warning :negative} surface) :error :info)]
                   :-header-text (str "This is a " (name surface) " callout")
                   :-colorway    :neutral})]))

        ;; Icon buttons
        (into [:section.flex-row-sb]
              (for [surface  [:solid :soft :outline :minimal]
                    icon-tag [:favorite :auto-awesome]]
                [icon-button
                 (merge-attrs
                  (sx :.base)
                  {:-surface  surface
                   :-colorway :neutral
                   :-packing  :compact})
                 [icon icon-tag]]))

        ;; Buttons
        (into [:section.flex-row-sb (sx :flex-wrap--wrap)]
              (for [surface  [:soft :solid :outline :minimal]]
                [button
                 (merge-attrs
                  (sx :.sm)
                  {:-surface      surface
                   :-colorway     :neutral
                   :-end-enhancer [icon :play-arrow]})
                 "Play"]))

        ;; Tags
        (into [:section.flex-row-sb (sx :flex-wrap--wrap)]
              (for [surface  [:soft :outline]
                    icon-tag ["ux/ui" "front end" "cljs" "design system" "CSS"]]
                [tag
                 (merge-attrs
                  (sx :.xs)
                  {:-surface  surface
                   :-colorway :neutral})
                 icon-tag]))

        [:section
         [collapse
          (merge-attrs
           (sx :bbe--1px:solid:$neutral-800
               :dark:bbe--1px:solid:$neutral-400)
           {:-label        "Collapsable section label "
            :-header-attrs (sx :bbs--1px:solid:$neutral-800
                               :dark:bbs--1px:solid:$neutral-400)})
          [:p "Child 1"]
          [:p "Child 2"]]]

        ;; Cards
        [profile-card "Polar Bear" "polar.bear@example.com"]
        [profile-card "Panda Bear" "panda.bear@example.com"]]


       ;; Second grouping of various UI component examples
       [:section (sx :.flex-col-fs
                     :xl:order--3
                     :gap--1rem
                     :width--320px
                     :>section:gap--0.5em)

        ;; Checkboxes
        (into [:section
               (sx :.flex-col-fs
                   :.rounded
                   :.transition
                   :gap--0.85rem!important
                   :bgc--$neutral-50
                   :dark:bgc--$neutral-950
                   :p--1.25rem)]
              (for [l [[:span "Check on comms with ROI team " [link "#348"]]
                       [:span "Circle back with CJ team and TB"]
                       [:span "Post-mortem of Q2 " [link "KPI metrics review"]]
                       [:span "Talk to legal about bullet-points from HR"]
                       [:span "Rethink org-wide " [link "mission statement"]]]]
                [checkbox (merge-attrs
                           (sx {:checked:+*    {:td :line-through
                                                :o  :0.7}
                                :border-radius :$shape-rounded-xs})
                           {:-label-attrs (sx :.light)}) l]))

        ;; Sliders
        (into [:section
               (sx :.grid
                   :gtc--1fr:5fr
                   :row-gap--2rem!important
                   :mb--1.5rem)]
              (for [[s m] [["Points" {:min           3
                                      :max           8
                                      :default-value 99}]
                           ["Variants" {:min           1
                                        :max           3
                                        :default-value 2}]]]
                [:<>
                 [label (sx :.3xs :ai--fe) s]
                 [slider
                  (merge-attrs
                   {:-step-marker label}
                   m)]]))

        ;; Spinners
        (into [:section.flex-row-sa
               (sx :flex-wrap--wrap
                   :pb--0.75rem)]
              (for [comp       [thinking donut propeller]
                    size-class ["sm" "2xl"]]
                [comp (sx size-class)]))

        ;; Radio group  
        [:section
         (sx
          :.rounded
          :.transition
          :d--grid
          :bgc--$neutral-50
          :dark:bgc--$neutral-950
          :p--1.25rem
          :xsm:gtc--1fr:1fr
          :row-gap--1em
          :column-gap--2em
          [:_.emoji
           {:fs                  :28px
            :mi                  :0.33em
            :filter              "grayscale(1)"
            :transition-property :transform
            :transition-duration :500ms}]
          [:_.kushi-radio-input:checked+.kushi-label>.emoji
           {:filter    :none
            :transform "scale(1.5)"
            :animation :jiggle2:0.5s}])
         [radio
          (merge-attrs (sx :.normal)
                       {:-input-attrs {:name           :demo-custom
                                       :defaultChecked true}})
          [label [:span.emoji "🦑"] "Squid"]]
         [radio
          (merge-attrs (sx :.normal) {:-input-attrs {:name :demo-custom}})
          [label [:span.emoji "🐋"] "Whale"]]
         [radio
          (merge-attrs (sx :.normal) {:-input-attrs {:name :demo-custom}})
          [label [:span.emoji "🦈 "] "Shark"]]
         [radio
          (merge-attrs (sx :.normal) {:-input-attrs {:name :demo-custom}})
          [label [:span.emoji "🐊"] "Croc"]]]

        ;; Switches group 
        [:section.flex-row-sb (sx :gap--2em :.xl :>section:gap--0.5em)
         [:section.flex-col-fs
          [switch {}]
          [switch {:-on? true}]]

         [:section.flex-col-fs
          [switch
           (merge-attrs (sx {"--switch-width-ratio" :2.25})
                        {:-track-content-on  "ON"
                         :-track-content-off "OFF"})]
          [switch
           (merge-attrs (sx {"--switch-width-ratio" :2.25})
                        {:-on?               true
                         :-track-content-on  "ON"
                         :-track-content-off "OFF"})]]

         [:section.flex-col-fs
          [switch
           {:-thumb-content-on  [:span (sx :.semi-bold :fs--0.3em) "ON"]
            :-thumb-content-off [:span (sx :.semi-bold :fs--0.3em) "OFF"]}]
          [switch
           {:-on?               true
            :-thumb-content-on  [:span (sx :.semi-bold :fs--0.3em) "ON"]
            :-thumb-content-off [:span (sx :.semi-bold :fs--0.3em) "OFF"]}]]

         [:section.flex-col-fs
          [switch
           {:-thumb-content-on  [icon
                                 (merge-attrs
                                  (sx :fs--0.55em)
                                  {:-icon-filled? true})
                                 :visibility]
            :-thumb-content-off [icon
                                 (merge-attrs
                                  (sx :fs--0.55em)
                                  {:-icon-filled? true})
                                 :visibility-off]}]
          [switch
           {:-on?               true
            :-thumb-content-on  [icon (merge-attrs
                                       (sx :fs--0.55em)
                                       {:-icon-filled? true})
                                 :visibility]
            :-thumb-content-off [icon (merge-attrs
                                       (sx :fs--0.55em)
                                       {:-icon-filled? true})
                                 :visibility-off]}]]]]


       ;; Third grouping of various UI component examples
       [:section (sx :.flex-col-fs
                     :.transition
                     :transition-property--background-color
                     :xl:order--2
                     :ai--c
                     :gap--1rem
                     :width--100%
                     :xl:width--432px
                     :>section:gap--0.5em
                     :bgc--$neutral-50
                     :dark:bgc--$neutral-950
                     :sm:p--3rem:3rem:2.5rem
                     :p--2rem:1rem:2.5rem)
        [:div.flex-col-fs
         (sx :ai--c
             :gap--1rem
             :width--400px
             :xl:width--320px
             :max-width--100%)
         [:h2.semi-bold.xl "Sign up"]

         [:section.flex-col-fs.grow.transition
          (sx :.elevated-3
              :w--100%
              :p--2rem
              :gap--1.5rem
              :br--$shape-rounded-2xl
              :bgc--white
              :dark:bgc--$background-color-dark-mode
              :b--1px:solid:$neutral-300
              :dark:b--1px:solid:$neutral-900)
          [text-field {:-label      "Full name"
                       :placeholder "Your full name"}]
          [text-field {:-label      "Email"
                       :placeholder "Your email"}]
          [text-field {:-label      "Password"
                       :placeholder "Choose a password"}]
          [button
           (merge-attrs
            (sx :w--100%)
            {:-surface  :solid
             :-colorway :neutral})
           "Create Account"]
          [:div (sx :.relative)
           [divisor (sx :bgc--$neutral-300
                        :dark:bgc--$neutral-700)]
           [:span (sx :.absolute
                      :.flex-row-c
                      :w--100%
                      :translate--0:-50%)
            [:span (sx :pi--1em
                       :.xs
                       :bgc--$background-color
                       :dark:bgc--$background-color-dark-mode)
             "OR"]]]
          [button
           (merge-attrs
            (sx :w--100%)
            {:-surface  :outline
             :-colorway :neutral})
           "Continue with Github"]]]]]]])


;; Below is boilerplate code from
;; https://github.com/shadow-cljs/quickstart-browser

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  #_(js/console.clear)
  (rdom/render [main-view] (.getElementById js/document "app")))

(defn init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop [])





