(ns starter.browser
  (:require
   ;; Require various functions and macros from kushi.core
   ;; [fireworks.core :refer [? !? ?> !?>]]
   [kushi.core :refer [sx
                       css
                       defcss
                       css-vars-map]]
   [kushi.inject :refer [add-stylesheet!
                         add-google-fonts!
                         add-stylesheet!]]
   [starter.theme]
   ;; Require your apps shared classes and component namespaces
   [starter.badges :as badges]
   [starter.shared-styles]

   ;; This example uses reagent
   [reagent.dom :as rdom]))



;; Defining animation keyframes with defcss.
;; 
;; You can also use defcss to define shared classes, see the
;; starter.browser.shared-styles namespace in this project. 
;; ............................................................................

(defcss "@keyframes y-axis-spinner"
  [:0% {:transform "rotateY(0deg)"}]
  [:100% {:transform "rotateY(360deg)"}])

(defcss "@keyframes x-axis-spinner"
  [:0% {:transform "rotateX(0deg)"}]
  [:100% {:transform "rotateX(360deg)"}])



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




;; Now, some working code...

;; First, we define a subcomponent for the rotating banner headline "layers"
;; This example component demonstrates the following:
;;
;; 1) Using shared styles, the :.headline and :.twirl classes, which are defined
;;    in browser.shared-styles.
;;
;; 2) Using kushi's shorthand syntax via tokenized keyword
;;    :animation-name--x-axis-spinner
;;
;; 3) Using kushi's shorthand syntax via tokenized keyword, with `$` syntax for 
;;    css vars.
;;    :animation-duration--$duration
;;
;; 3) Using local dynamic values for color and animation duration.

(defn headline-layer
  [color duration]
  [:div
   {:style (css-vars-map color duration)
    :class (css :.headline
                :.twirl
                :animation-name--x-axis-spinner
                :animation-duration--$duration
                :color--$color)}
   "Kushi"])


;; Next, we define a subcomponent for the sub-header.
;; This example `twirling-subheader` component demonstrates the following:

;; 1) Using kushi's shorthand syntax via tokenized keywords.

;; 2) Tokenized keyword with css "alt list" shorthand
;;    :ff--FiraCodeRegular|monospace|sans-serif

;; 3) Using a media query to provide an alternate style.
;;    In this case we are making the font-size larger at the "small" breakpoint
;;    with `:sm:fs--14px`

(defn twirling-subheader [s]
  [:div
   {:class    (css :.twirl
                   :position--relative
                   :ta--center
                   :ff--FiraCodeRegular|monospace|sans-serif
                   :fs--12px
                   :sm:fs--14px
                   :fw--800
                   :c--white)
    :on-click #(prn "clicked!")}
   s])


;; Main component. All the elements in this component are styled using
;; kushi.core/sx, because they do not need any other attributes other
;; than :class
(defn main-view []
  [:div (sx :ff--$sans-serif-font-stack)
   [:div
    (sx :.flex-col-c
        :.absolute-fill
        :ai--c
        :h--100%
        :bgc--black)

    [:div
     (sx :.flex-col-sb
         :ai--c
         :w--100%
         :h--200px
         :sm:h--375px
         :md:h--500px
         [:transform "translateY(calc(-100vh / 33))"])

     ;; The color tokens (css vars) below are defined globally in the :theme
     ;; entry in the project's kushi.edn config file.
     [:div
      [headline-layer "var(--howlite-blue)" :12s]
      [headline-layer "var(--canary-yellow)" :3s]
      [headline-layer "var(--deep-fuscsia)" :6s]]
     [twirling-subheader "kushi × shadow-cljs quickstart"]]]
   [badges/links]])


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


;; This will inject the same stylesheet that kushi writes to disk into your
;; browser, during development builds. You may not need or want to do this
;; but if you are experiencing visual jankiness on reloads when devving,
;; this can help.

;; (when ^boolean js/goog.DEBUG
;;   (inject!))
