(ns starter.badges
  (:require
   [kushi.core :refer [sx merge-attrs]]
   [kushi.ui.core :refer [opts+children]]))


;; DEFINING COMPONENTS
;; Also see https://github.com/kushidesign/kushi#defining-components
;; ...............................................................

(defn icon-badge-link [& args]
  (let [[opts attrs children] (opts+children args)]
    [:a attrs
     [:button
      (sx :.pointer :o--0.85 :hover:o--1)]
     [:img
      (merge-attrs
       (sx :.grayscale
           :.small-badge
           :max-height--100%
           :max-width--100%
           :object-fit--contain)
       {:src (:badge-src opts)})]]))

;; Usage of the components defined above.
(defn links []
  [:div
   (sx :.flex-row-c )
   [:div
    (sx :.flex-row-sa :w--100px :>button:display--inline-flex)
    [icon-badge-link
     {:href       "https://github.com/kushidesign/kushi"
      :target     :_blank
      :-badge-src "graphics/github.svg"}]
    [icon-badge-link
     {:href       "https://clojars.org/design.kushi/kushi"
      :target     :_blank
      :-badge-src "graphics/clojars-logo-bw2.png"}]]])
