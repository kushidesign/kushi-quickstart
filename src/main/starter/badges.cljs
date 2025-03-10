(ns starter.badges
  (:require
   [kushi.core :refer [css sx merge-attrs]]
   [kushi.ui.core :refer [extract]]))


;; DEFINING COMPONENTS
;; Also see https://github.com/kushidesign/kushi#defining-components
;; ...............................................................


(defn contained-image [& args]
  (let [{:keys [opts attrs children]} (extract args contained-image)]
    [:img
     (merge-attrs
      (sx :max-height--100%
          :max-width--100%
          :object-fit--contain)
      attrs)]))


(defn icon-badge-link [& args]
  (let [{:keys [opts attrs children]} (extract args icon-badge-link)]
    [:a attrs
     (into [:button
            (merge-attrs
             (sx :.pointer :o--0.85 :hover:o--1))]
           children)]))


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
       {:href   "https://github.com/kushidesign/kushi"
        :src    src
        :target :_blank}

       [contained-image {:class (css :.grayscale :.small-badge)
                         :src   src}]])

    (let [src "graphics/clojars-logo-bw2.png"]
      [icon-badge-link
       {:href   "https://clojars.org/design.kushi/kushi"
        :src    src
        :target :_blank}

       [contained-image {:class (css :.grayscale :.small-badge)
                         :src   src}]])]])

