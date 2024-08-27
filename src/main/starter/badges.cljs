(ns starter.badges
  (:require
   [kushi.core :refer [sx merge-attrs]]
   [kushi.ui.core :refer [defcom]]))


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


;; Below, `contained-image` and `icon-badge-link` both use defmacro to create
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


(defcom icon-badge-link
  [:a &attrs
   [:button
    (merge-attrs
     (sx 
      :.pointer
      :o--0.85
      :hover:o--1))
    &children]])


;; Usage of the components defined above.
(defn links []
  [:div
   (sx :.absolute
       :.flex-row-c
       :w--100%
       :mbs--38px)
   [:div
    (sx :.flex-row-sa
        :w--100px
        :>button:display--inline-flex)

    (let [src "graphics/github.svg"]
      [icon-badge-link
       {:href               "https://github.com/kushidesign/kushi"
        :src                src
        :target             :_blank }
       
       [contained-image (sx :.grayscale
                            :.small-badge
                            {:src src})]])

    (let [src "graphics/clojars-logo-bw2.png"]
      [icon-badge-link
       {:href               "https://clojars.org/design.kushi/kushi"
        :src                src
        :target             :_blank}
       
       [contained-image (sx :.grayscale
                            :.small-badge
                            {:src src})]])]])

