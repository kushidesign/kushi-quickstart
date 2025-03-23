(ns starter.browser
  (:require
   ;; Require various functions and macros from kushi.core
   [kushi.core :refer [?sx sx merge-attrs]]

   ;; Require some components
   [kushi.playground.ui :refer [light-dark-mode-switch]]
   [kushi.ui.core :refer [opts+children]]
   [kushi.ui.button.core :refer [button icon-button]]
   [kushi.ui.callout.core :refer [callout]]
   [kushi.ui.collapse.core :refer [collapse]]
   [kushi.ui.card.core :refer [card]]
   [kushi.ui.icon.core :refer [icon]]
   [kushi.ui.radio.core :refer [radio]]
   [kushi.ui.switch.core :refer [switch]]
   [kushi.ui.slider.core :refer [slider]]
   [kushi.ui.link.core :refer [link]]
   [kushi.ui.checkbox.core :refer [checkbox]]
   [kushi.ui.divisor.core :refer [divisor]]
   [kushi.ui.text-field.core :refer [text-field]]
   [kushi.ui.label.core :refer [label]]
   [kushi.ui.tag.core :refer [tag]]
   [kushi.ui.spinner.core :refer [spinner propeller donut thinking]]

   ;; Require your apps shared classes and component namespaces
   [starter.badges :as badges]

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
        :.small
        :.wireframe
        {:width             "calc(320px + (var(--padding-inline) * 2))"         
         :sm:flex-direction :row
         :sm:width          "calc((320px * 2 ) + var(--padding-inline) + (var(--padding-inline) * 2))"
         :xl:width          :1200px
         :flex-wrap         :wrap
         :gap               :2rem
         :pi                :$padding-inline
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
                   :>.kushi-input:flex-shrink--0
                   )
      [text-field
       (merge-attrs
        ;; (sx :.small)
        {:-start-enhancer [icon :search]
         :placeholder     "Search"})]
      [button 
       (merge-attrs
        ;; (sx :.small)
        {:-surface  :solid
         :-colorway :neutral})
       "Search"]]
     
     ;; Callouts
     (into [:section.flex-col-fs]
          ;;  (for [colorway [:neutral :accent :positive :warning :negative]]
           (for [surface [:soft :solid :outline]]
             [callout 
              (merge-attrs
              ;;  (sx :.small)
               {:-surface     surface
                ;; :-icon        [icon (if (contains? #{:warning :negative} colorway) :error :info)]
                :-icon        [icon (if (contains? #{:warning :negative} surface) :error :info)]
                :-header-text (str "This is a " (name surface) " callout")
                :-colorway    :neutral}) ]))
     
     ;; Icon buttons
     (into [:section.flex-row-sb]
           (for [surface  [:solid :soft :outline :minimal]
                 icon-tag [:favorite :auto-awesome]]
             [icon-button 
              (merge-attrs
               (sx :.medium)
               {:-surface  surface
                :-colorway :neutral
                :-packing  :compact})
              [icon icon-tag]]))

     

     ;; Buttons
     (into [:section.flex-row-sb (sx :flex-wrap--wrap)]
           (for [surface  [:soft :solid :outline :minimal]]
             [button
              (merge-attrs
               (sx :.small)
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
               (sx :.xsmall)
               {:-surface  surface
                :-colorway :neutral
                ;; :-packing  :compact
                })
              icon-tag]))
     
     [:section (sx :min-height--107px)
      [collapse
       (merge-attrs 
        (sx :bbe--1px:solid:$neutral-800
            :dark:bbe--1px:solid:$neutral-400)
        {:-label        "Collapsable section label "
         :-header-attrs (sx :bbs--1px:solid:$neutral-800
                            :dark:bbs--1px:solid:$neutral-400)})
       [:p "Child 1"]
       [:p "Child 2"]]]

     ;; Card
     (into [:section.flex-col-fs ]
           (for [elevated ["elevated-0" #_"elevated-3"]]
             [card
              (sx elevated
                  :dark:b--1px:solid:$neutral-700)
              [:div (sx :.flex-row-fs
                        :.neutralize
                        :ai--stretch
                        :gap--0.8em)
               [:div (sx :.rounded
                         :.transition
                         :position--relative
                         :overflow--hidden
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
                 "polar.bear@example.com"]]]]))]
    

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
                             :border-radius :$rounded-xsmall})
                        {:-label-attrs (sx :.light)}) l]))

     
     ;; Checkboxes with weight
     #_(into [:section 
              (sx :.flex-col-fs
                  :.rounded
                  :gap--0.85rem!important
                  :bgc--$neutral-50
                  :dark:bgc--$neutral-900
                  :p--1.25rem)]
             (let [weights [:light :normal :wee-bold :semi-bold :bold]]
               (for [s weights]
                 [checkbox 
                  {:-label-attrs {:class (name s)}}
                  [label "Labeled checkbox with trailing icon, " (name s) [icon :auto-awesome]]])))
     
     
     ;; Sliders
     (into [:section
            (sx :.grid
                :gtc--1fr:5fr
                :row-gap--2rem!important
                :mbs--1rem)]
           (for [[s m] [["Points" {:min           1
                                   :max           8
                                   :default-value 8}]
                        ["Variants" {:min           1
                                     :max           3 
                                     :default-value 2}]]]
             [:<>
              [label (sx :.xxxsmall :ai--fe) s]
              [slider 
               (merge-attrs 
                {:-step-marker label}
                m)]]))

     ;; Spinners
     (into [:section.flex-row-sa
            (sx :flex-wrap--wrap
                :pb--0.75rem)]
           (for [comp       [thinking donut propeller]
                 size-class ["small" "xxlarge"]]
             [comp (sx #_:.foreground-blue size-class)]))

     ;; Radio group 
     [:section
      (sx
       :.rounded
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
     

     ;; Radio group 
     
     [:section.flex-row-sb (sx :gap--2em
                               :.xlarge
                               :>section:gap--0.5em)
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
                  :xl:order--2
                  :ai--c
                  :gap--1rem
                  :width--100%
                  :xl:width--432px
                  :>section:gap--0.5em
                  :bgc--$neutral-50
                  :dark:bgc--$neutral-950
                  :p--3rem:3rem:2.5rem)
     [:div.flex-col-fs
      (sx :ai--c
          :gap--1rem
          :width--400px
          :xl:min-width--400px)
      [:h2.semi-bold.xlarge "Sign up"]
      [:section.flex-col-fs.grow
       (sx :.elevated-3
           :w--100%
           :p--2rem
           :gap--1.5rem
           :br--$rounded-xxlarge
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
                    :.xsmall
                    :bgc--$background-color
                    :dark:bgc--$background-color-dark-mode)
          "OR"]]]
       [button 
        (merge-attrs 
         (sx :w--100%)
         {:-surface  :outline
          :-colorway :neutral})
        "Continue with Github"]]]]

    ]
   #_[:section 
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
     [switch (merge-attrs (sx :.xxlarge) {:-on? true :-colorway :neutral})]
     [switch (merge-attrs (sx :.xxlarge) {:-on? true :-colorway :accent})]
     [switch (merge-attrs (sx :.xxlarge) {:-on? true :-colorway :positive})]
     [switch (merge-attrs (sx :.xxlarge) {:-on? true :-colorway :warning})]
     [switch (merge-attrs (sx :.xxlarge) {:-on? true :-colorway :negative})]]

    [samples
     {:-label "Radio Group"}
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
