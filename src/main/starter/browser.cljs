(ns starter.browser
  (:require
   ;; Require various functions and macros from kushi.core
   [kushi.core :refer [sx merge-attrs]]

   ;; Require some components
   [kushi.playground.ui :refer [light-dark-mode-switch]]
   [kushi.ui.core :refer [opts+children]]
   [kushi.ui.button.core :refer [button]]
   [kushi.ui.callout.core :refer [callout]]
   [kushi.ui.card.core :refer [card]]
   [kushi.ui.icon.core :refer [icon]]
   [kushi.ui.radio.core :refer [radio]]
   [kushi.ui.switch.core :refer [switch]]
   [kushi.ui.text-field.core :refer [text-field]]
   [kushi.ui.tag.core :refer [tag]]

   ;; Require your apps shared classes and component namespaces
   [starter.badges :as badges]
   [starter.shared-styles]

   ;; This example uses reagent
   [reagent.dom :as rdom]))


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



(defn samples [& args]
  (let [[opts attrs & children] (opts+children args)
        {:keys [label]} opts]
   [:section
    attrs
    [:h2 
     (sx {:fs         :$xsmall 
          :font-style :italic
          :ff         :$serif-font-stack
          :mb         :1em})
     label]
    (into [:section
           (sx :.flex-row-fs
               :.examples
               {:gap       :1rem})]
          children)]))

(defn main-view []
  [:main
   (sx {"--nav-height"     :70px
        "--padding-inline" :2rem
        "--padding-block"  :1rem
        :font-weight       :$light})

   [:nav 
    (sx :.grid
        {:gtc      :1fr:1fr:1fr
         :ai       :c
         :position :fixed
         :top      :0
         :left     :0
         :right    :0
         :h        :$nav-height
         :pi       :$padding-inline
         :pb       :$padding-block})
    [:h1 "Kushi Quickstart"]
    [badges/links]
    [:div (sx :.flex-row-fe)
     [light-dark-mode-switch]]]


   [:section 
    (sx :.flex-col-fs
        {:gap :4rem
         :pi  :$padding-inline
         :mbs :$nav-height
         :pb  :4rem})
     
    [:section (sx :>p:margin-block--1em)
     [:p.prose "Below is a small sampling of Kushi UI components."]
     [:p.prose
      "Check out " 
      [:a (merge-attrs
           (sx :td--u)
           {:href "https://kushi.design" :_target :blank}) 
       "kushi.design"] 
      " for more component samples and documentation."]]

    [samples
     {:-label "Button"}
     [button {:-colorway :neutral :-surface :outline} "Button"]
     [button {:-colorway :accent :-surface :outline} "Button"]
     [button {:-colorway :positive :-surface :outline} "Button"]
     [button {:-colorway :warning :-surface :outline} "Button"]
     [button {:-colorway :negative :-surface :outline} "Button"]]
    
    [samples
     {:-label "Tag"}
     [tag {:-colorway :neutral :-surface :outline} "Neutral"]
     [tag {:-colorway :accent :-surface :outline} "Pending"]
     [tag {:-colorway :positive :-surface :outline} "Successful"]
     [tag {:-colorway :warning :-surface :outline} "Waiting"]
     [tag {:-colorway :negative :-surface :outline} "Rejected"]]

    [samples
     (merge-attrs {:-label "Callout"}
                  (sx {" .examples:flex-direction" :column}))
     [callout {:-header-text "This is a callout component with an icon."
               :-surface     :outline
               :-icon        [icon :info]}]
     [callout {:-colorway    :accent
               :-surface     :outline
               :-header-text "This is a callout component with an icon."
               :-icon        [icon :info]}]
     [callout {:-colorway    :positive
               :-surface     :outline
               :-header-text "This is a callout component with an icon."
               :-icon        [icon :info]}]
     [callout {:-colorway    :warning
               :-surface     :outline
               :-header-text "This is a callout component with an icon."
               :-icon        [icon :info]}]
     [callout {:-colorway    :negative
               :-surface     :outline
               :-header-text "This is a callout component with an icon."
               :-icon        [icon :info]}]]
    
    ;; A card component. The styles in this are written with Kushi's tokenized
    ;; keyword syntax (instead of a maps).
    [samples 
     {:-label "Card"}
     [card
      (sx :w--fit-content)
      [:div (sx :.flex-row-fs
                :.neutralize
                :ai--stretch
                :gap--0.8em)
       [:div (sx :.rounded
                 :position--relative
                 :overflow--hidden
                 :.transition
                 :bgc--$neutral-200
                 :dark:bgc--$neutral-800
                 :w--3.5em
                 :h--3.5em)
        [:span (sx :.absolute-centered
                   [:transform "translate(0, 0.045em)"]
                   :display--block
                   :scale--2.55)
         "🐻‍❄"]]
       [:section (sx :.flex-col-sa) 
        [:p (sx :fs--1.25em :fw--$wee-bold) "Polar Bear"] 
        [:p (sx :c--$secondary-foreground-color
                :dark:c--$secondary-foreground-dark-mode)
         "polar.bear@example.com"]]]]]
    
    [samples 
     {:-label "Switch"}
     [switch (sx :.xxlarge {:-colorway :neutral})]
     [switch (sx :.xxlarge {:-colorway :accent})]
     [switch (sx :.xxlarge {:-colorway :positive})]
     [switch (sx :.xxlarge {:-colorway :warning})]
     [switch (sx :.xxlarge {:-colorway :negative})]
     ]

    [samples
     {:-label "Radio"}
     [radio (merge-attrs (sx :.xxxlarge) 
                         {:-input-attrs {:name           :xxxlarge-sample
                                         :defaultChecked true}})]
     [radio (merge-attrs (sx :.xxxlarge) 
                         {:-input-attrs {:name :xxxlarge-sample}})]
     [radio (merge-attrs (sx :.xxxlarge) 
                         {:-input-attrs {:name :xxxlarge-sample}})]
     [radio (merge-attrs (sx :.xxxlarge) 
                         {:-input-attrs {:name :xxxlarge-sample}})]
     ]

    [samples
     (merge-attrs {:-label "Text field"}
                  (sx {" .examples:flex-direction" :column
                       " .examples:ai"             :fs
                       " .examples:gap"            :2rem}))
     [text-field
      {:placeholder "Your text here"
       :-label      "Input label"
       :-helper     "My helper text"}]

     [text-field
      {:placeholder "Your text here"
       :required    true
       :-label      "Input label"
       :-helper     "My helper text"}]
     
     [text-field
      {:placeholder "Your text here"
       :-textarea?  true
       :-label      "Input label"
       :-helper     "My helper text"}]]]])


;; Below is boilerplate code from
;; https://github.com/shadow-cljs/quickstart-browser

;; start is called by init and after code reloading finishes
(defn ^:dev/after-load start []
  (rdom/render [main-view] (.getElementById js/document "app")))

(defn init []
  ;; init is called ONCE when the page loads
  ;; this is called in the index.html and must be exported
  ;; so it is available even in :advanced release builds
  (start))

;; this is called before any code is reloaded
(defn ^:dev/before-load stop [])
