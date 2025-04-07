(ns starter.badges
  (:require
   [kushi.core :refer [sx merge-attrs defcss]]
   [kushi.ui.core :refer [opts+children]]))


;; DEFINING SHARED STYLES with defcss
;; Use kushi.core/defcss macro to define shared styles.

;; Typically, you would want to define all your shared styles in a dedicated
;; namespace. This namespace must then be required in your core namespace to
;; make all of your defclasses available globally. You can also define classes 
;; with defcss in the namespace you are using them in.

(defcss ".small-badge"
  :w--16px
  :h--16px
  :o--1)


;; DEFINING COMPONENTS
;; Also see https://github.com/kushidesign/kushi#defining-components
;; ...............................................................

(defn icon-badge-link [& args]
  (let [[opts attrs children] (opts+children args)]
    [:a attrs
     [:button
      (sx :.pointer :hover:o--0.75)
      [:img
       (merge-attrs
        (sx :.small-badge
            :max-height--100%
            :max-width--100%
            :object-fit--contain
            ["filter" "grayscale(1) contrast(1) brightness(1)"]
            ["dark:filter" "grayscale(1) contrast(1) brightness(1) invert()"])
        {:src (:badge-src opts)})]]]))

;; Usage of the components defined above.
(defn links [& args]
  (let [[opts attrs children] (opts+children args)]
    [:div
     (merge-attrs (sx :.flex-row-c) attrs)
     [:div
      (sx :.flex-row-sa :w--100px :>button:display--inline-flex)
      [icon-badge-link
       {:href       "https://github.com/kushidesign/kushi"
        :target     :_blank
        :-badge-src "graphics/github.svg"}]
      [icon-badge-link
       {:href       "https://clojars.org/design.kushi/kushi"
        :target     :_blank
        :-badge-src "graphics/clojars-logo-bw2.png"}]]]))
