(ns starter.badges
  (:require
   [clojure.string :as string] 
   [domo.emoji]
   [kushi.core :refer [sx merge-attrs defclass keyed]]
   [kushi.ui.core :refer [defcom]]
   [kushi.ui.icon.mui.svg :as mui.svg ]
   [kushi.ui.button.core :refer [button]]
   ;; [kushi.ui.tooltip.core :refer [tooltip-attrs user-placement]]
   [kushi.ui.tooltip.core :refer [tooltip-attrs]]
   [kushi.ui.popover.core :refer [popover-attrs]]
   [kushi.ui.popover.demo :refer [popover-content]]
   [kushi.ui.toast.core :refer [toast-attrs]]
   [kushi.ui.toast.demo :refer [toast-content]]
   [kushi.ui.icon.core :refer [icon]]
   [kushi.ui.input.text.core :refer [input]]
   [reagent.dom :as rdom]
   [shadow.css :refer (css)]
   ))



;; DEFINING COMPONENTS
;; Also see https://github.com/kushidesign/kushi#defining-components
;; ...............................................................

;; The commented code below is a contrived example of creating a reusable,
;; stateless, and composable component using `kushi.ui.core/defcom`.

;; (ns myapp.core
;;   (:require
;;    [kushi.core :refer [sx]]
;;    [kushi.ui.core :refer [defcom]]))

;; (defcom my-section
;;   (let [{:keys [label label-attrs body-attrs]} &opts]
;;     [:section
;;      &attrs
;;      (when label
;;        [:div label-attrs label])
;;      [:div body-attrs &children]]))

;; `defcom` is a macro that returns a component rendering function which
;; accepts an optional attributes map, plus any number of children.
;; This means the signature at the call site mirrros hiccup itself.

;; Under the hood, defcom pulls out any keys in attr map that start with
;; `:-` and put them in a separate `opts` map. This allows passing in various
;; custom options within the attributes map that will not clash with existing
;  html attributes. This opts map can referenced be referenced in the defcom
;; body with the `&opts` binding. `&attrs` and `&children` are also available.
;; This ampersand-leading naming convention takes its cue from the special
;; `&form` and `&env` bindings used by Clojure's own `defmacro`.

;; Assuming you are using something like Reagent, you can use the resulting
;; `my-section` component in your application code like so:

;; Basic, no label
;; [my-section [:p "Child one"] [:p "Child two"]]

;; With optional label
;; [my-section (sx {:-label "My Label"}) [:p "Child one"] [:p "Child two"]]

;; With all the options and additional styling
;; [my-section
;;  (sx
;;   'my-section-wrapper    ; Provides custom classname (not auto-generated).
;;   :.xsmall               ; Font-size utility class.
;;   :p--1rem               ; Padding inside component.
;;   :b--1px:solid-black    ; Border around component.
;;   {:-label "My Label"
;;    :-label-attrs (sx :.huge :c--red)
;;    :-body-attrs (sx :bgc--#efefef)})
;;  [:p "Child one"]


;; For more in-depth info on defmacro and its underlying pattern, see:
;; https://github.com/kushidesign/kushi#manually-defining-complex-components.


;; Below, `contained-image` and `twirling-badge` both use defmacro to create
;; reusable components. They also use kushi.core/merge-attrs to properly merge
;; user-passed attributes.

(defcom contained-image
  [:img
   (merge-attrs
    (sx 'grayscale-icon-image
        :max-height--100%
        :max-width--100%
        :object-fit--contain)
    &attrs)])

;; Uses kushi.ui.tooltip.core/tooltip-attrs to create tooltip
(defcom icon-badge-link
  (let [{:keys [tooltip-text tooltip-placement]} &opts]
    [:a &attrs
        [button
         (merge-attrs
          (sx :hover:bgc--transparent
              :hover:c--white
              :bgc--transparent
              :p--0
              :after:ff--FiraCodeRegular|monospace|sans-serif
              :before:ff--FiraCodeRegular|monospace|sans-serif)
          (tooltip-attrs {:-text      ["tip1" "tip2"]
                          :-placement :t}))
         &children]]))


(defn tipped
  ([pk]
   (tipped pk nil))
  ([pk t]
   (tipped pk t nil))
  ([pk t nesw]
   [button (merge-attrs
            (sx :.code
                :.no-shrink
                :.xxxfast
                :.debug-red
                :width--344px
                ;; :width--auto
                :pb--0.5em
                :pi--0.75em
                :hover:bgc--lime
                ;; :b--1px:solid:#230ad8
                ;; :border-radius--10px
                ;; :>span:c--lime!important
                :>.code:bgc--transparent
                ["[aria-describedby^='kushi-G__']:bgc" :lime]
                ["has-ancestor(.n-trg)&_.kushi-tooltip:writing-mode" :tb]
                {:data-kushi-ui-fune pk} ;; this is for testing, not necessary
                )

            #_(toast-attrs
             {:-auto-dismiss? false
              :-placement     pk
              :-f             (fn [toast-el]
                                (rdom/render toast-content
                                             toast-el))})
            #_(popover-attrs {:-placement pk
                            :-f         #(rdom/render popover-content %)})
            ;; Comment out for testing
            (tooltip-attrs 
               (sx {:-text      (if (contains? #{:n :s} nesw)
                                  #_["👹"
                                     "👹"
                                     "👹"]
                                  #_["-" "-" "-"]
                                  (domo.emoji/random-emojis 3)
                                  #_(string/join " " (rando-mojis 3))
                                  #_"-----"
                                  (string/join " " (domo.emoji/random-emojis 3))
                                  #_"👹👹👹" 
                                  #_"👹👹👹" #_"My Tooltip Text abcXYZ")
                    :-placement pk
                  ;; :-placement :auto
                    :-arrow?    true
                  ;; :-tooltip-class :lime-bg
                  ;; :-tooltip-class (when nesw
                  ;;                  (first (:class (sx 'ktt-wm-tb :writing-mode--tb))))
                    })))
                
    [:span {:class (css :text-red-500 :text-xl)} (str pk)]
    ;; optional icon
    #_(when nesw
      (let [k (case nesw
                :flip-horiz  :swap-horiz
                :flip-vert   :swap-vert
                :flip-diag   :aspect-ratio
                :shift-up    :arrow-upward
                :shift-down  :arrow-downward
                :shift-left  :arrow-back
                :shift-right :arrow-forward
                nil)]
        (when k [icon k])))]))



(def my-shared-class
  (css {:level    :user-shared-styles
        :selector ".my-shared-class, .my-other-thing"}
       :text-red-200
       :text-xs))



(def n-ks [:tlc :br :b :blc :lb :lt :l :tl :t :tr :rt :r :rb :brc :b :bl :trc])

(defclass n-trg
  :.absolute
  :.flex-row-sb
  :gap--0.5em
  :w--100%
  :top--0
  :&_.kushi-floating-toolip:writing-mode--tb!important)

(defclass s-trg
  :.absolute
  :.flex-row-sb
  :w--100%
  :bottom--0px
  :gap--0.5em)

(defclass ns-side-trg :min-width--677px :w--90%)

(defn north-edge []
  (into 
   [:div (sx :.n-trg :.n-edge-trg)]
   (for [k n-ks]
    [tipped k])))


(defn north-side []
  (into 
   [:div (sx :.n-trg :.ns-side-trg :.n-side-trg :top--$ns-side-offset)]
   (for [k n-ks]
    [tipped k nil :n])))


;; South
(def s-ks [:tlc :tr :t :blc :lb :lt :l :bl :b :br :rt :r :rb :brc :t :tl :trc])


(defn south-edge []
  (into [:div (sx :.s-trg :.s-edge-trg)]
        (for [k s-ks]
          [tipped k])))

(defn south-side []
  (into [:div (sx :.s-trg :.ns-side-trg :.s-side-trg :bottom--$ns-side-offset)]
        (for [k s-ks]
          [tipped k nil :n])))


;; West 
(def w-ks [:ltc :tlc :tr :t :lt :l :lb :b :br :blc :lbc])
#_(def w-ks [:ltc
          ;;  :tlc :tr :t :lt :l :lb :b :br :blc :lbc
           ])

(defclass middle-trg #_:translate--200px :ai--c :.absolute :.flex-col-c :gap--1em [:h "calc(100% - (80px * 2))"])

(defclass w-trg :top--80px :left--0)

(defclass e-trg :top--80px :right--0)

(defn west-edge []
  (into [:div (sx :.middle-trg :.w-trg :.w-edge-trg :left--0)]
        (for [k w-ks]
          [tipped k])))

(defn west-side []
  (into [:div (sx :.middle-trg :.w-trg :.w-side-trg :left--80px)]
        (for [k w-ks]
          [tipped k])))

;; Middle 
;; (def middle-ks [:tlc :trc :rtc :ltc :t :tl :tr :rt :r :rb :bl :br :b :lt :l :lb :lbc :rbc :blc :brc])
(def middle-ks [:t])
#_(def middle-ks [:tl :t :tr :trc :rt :r :rb :brc :br :b :bl :blc :l])

#_(def middle-ks 
  [[:inline-start :block-start :corner]
   [:inline-start :block-start]
   [:inline-start :center]
   [:inline-start :block-end]
   [:inline-start :block-end :corner]
   [:inline-end :block-start :corner]
   [:inline-end :block-start]
   [:inline-end :center]
   [:inline-end :block-end]
   [:inline-end :block-end :corner]
   [:block-start :inline-start :corner]
   [:block-start :inline-start]
   [:block-start :center]
   [:block-start :inline-end]
   [:block-start :inline-end :corner]
   [:block-end :inline-start :corner]
   [:block-end :inline-start]
   [:block-end :center]
   [:block-end :inline-end]
   [:block-end :inline-end :corner]])

#_(def middle-ks 
  [["inline-start" "block-start" "corner"]
   ["inline-start" "block-start"]
   ["inline-start" "center"]
   ["inline-start" "block-end"]
   ["inline-start" "block-end" "corner"]
   ["inline-end" "block-start" "corner"]
   ["inline-end" "block-start"]
   ["inline-end" "center"]
   ["inline-end" "block-end"]
   ["inline-end" "block-end" "corner"]
   ["block-start" "inline-start" "corner"]
   ["block-start" "inline-start"]
   ["block-start" "center"]
   ["block-start" "inline-end"]
   ["block-start" "inline-end" "corner"]
   ["block-end" "inline-start" "corner"]
   ["block-end" "inline-start"]
   ["block-end" "center"]
   ["block-end" "inline-end"]
   ["block-end" "inline-end" "corner"]])

(defn middle []
  (into [:div (sx :.middle-trg
                  :.absolute-centered)]
        (for [k middle-ks]
         [tipped k])))

(def e-ks [:rtc :trc :tl :t :rt :r :rb :b :bl :brc :rbc])

(defn east-edge []

  (into [:div (sx :.middle-trg :.e-trg :.e-edge-trg)]
        (for [k e-ks] 
          [tipped k])))

(defn east-side []
  (into [:div (sx :.middle-trg :.e-trg :.e-side-trg :.e-edge-trg :right--80px)]
        (for [k e-ks] 
          [tipped k])))


;; Usage of the components defined above.
(defn links []
  [:div
   (sx :.absolute
       :.flex-row-fs
       :w--100%
       :pis--10px
       ;;  :mbs--38px
       :mbe--18px
       :bottom--0)
   [:div
    (sx :.flex-row-sa
        :w--100px
        :>button:display--inline-flex)

    (let [src "graphics/github.svg"]
      [icon-badge-link
       {:href               "https://github.com/kushidesign/kushi"
        :src                src
        :-tooltip-text      "View project on github "
        :-tooltip-placement :left}
       
       [contained-image (sx :.grayscale
                            :.small-badge
                            {:src src})]])

    (let [src "graphics/clojars-logo-bw2.png"]
      [icon-badge-link
       {:href               "https://clojars.org/design.kushi/kushi"
        :src                src
        :-tooltip-text      "View project at clojars.org "
        :-tooltip-placement :right}
       
       [contained-image (sx :.grayscale
                            :.small-badge
                            {:src src})]])]])

