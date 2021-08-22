(ns starter.badges
  (:require
   [kushi.core :refer (sx cssfn)]))

(defn octocat []
[:div
   (sx :.absolute
       :.flex-row-c
       :.twirl
       :top--20px
       :right--100px
       [:filter (str "invert(49%) sepia(91%) saturate(1200%) hue-rotate(199deg) brightness(0.6)")]
       [:transform (cssfn :translateY "calc(-100vh / 8)")])
   [:a {:href "https://github.com/paintparty/kushi" :target :_blank}
    [:img
     (sx :h--25px
         :w--25px
         {:src "github.svg"})]]])
