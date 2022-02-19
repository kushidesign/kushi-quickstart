(ns starter.badges
  (:require
   [kushi.core :refer (sx cssfn)]
   [kushi.gui :refer (gui)]))


(def icon-image*
  [:img
   (sx {:style {:max-height :100%
                :max-width  :100%
                :object-fit :contain
                :font-size  0}})])

(def dark-blue-icon-image
  (gui icon-image*
       (sx {:style {:filter [[(cssfn :invert :49%)
                              (cssfn :sepia :91)
                              (cssfn :saturate :1200%)
                              (cssfn :hue-rotate :199deg)
                              (cssfn :brightness 0.6)]]}})))

(defn twirling-badge [href src]
 [:a
  (sx {:href href :target :_blank})
  [dark-blue-icon-image
   (sx {:class [:twirl]
        :style {:w       :25px
                :h       :25px
                :o       0.7
                :hover:o 1}
        :src   src})]])

(defn links []
  [:div
   (sx {:class [:absolute
                :flex-row-sb]
        :style {:w          :70px
                :top        :20px
                :x:right    :52px
                :>a:display :inline-flex}})
   [twirling-badge
    "https://github.com/paintparty/kushi"
    "graphics/github.svg"]
   [twirling-badge
    "https://clojars.org/org.clojars.paintparty/kushi"
    "graphics/clojars-logo.png"]])
