(ns starter.badges
  (:require
   [kushi.core :refer (sx cssfn defclass)]
   [reagent.core :as r]
   [kushi.gui :refer (gui)]
   ))


(defn monotone-filter [deg]
    (str "invert(49%) sepia(91%) saturate(1200%) hue-rotate(" deg ") brightness(0.6)"))

;; Below is a contrived example of creating a reusable, primitive, stateless component using the kushi.gui/gui helper fn.
;; This component, caled `monotone-icon-image`, renders an image and applies a monotone filter to it.
;; In the use case below, it is used inside an application-specific, custom component called `twirling-badge`.

;; First, we will define a base "template" that we till build our component on top of.
;; This isn't strictly necessary, but good practice if the base is something generic that will most likely be used again.
;; In this case, our base template renders an image that will shrink, if necessary, to fit its parent.
;; Please see `Defining Components` section of docs in main kushi repo for more info.
(def icon-image-base
  [:img
   (sx {:style {:max-height :100%
                :max-width  :100%
                :object-fit :contain}})])


;; In this example, we are using a css filter to make a
;; colorless (grayscale) variant on top of icon-image-base.
;; kushi.gui/gui takes a hiccup template like the one we've defined above,
;; as well as an attributes map to augment the base template's styling & attributes.
;; A function is returned, the signature of which mirrors hiccup
;; itself - it expects an optional map of attributes, and any number of children.
(def grayscale-icon-image
    (gui icon-image-base
         (sx {:style {:filter [[(cssfn :grayscale 1)
                                (cssfn :contrast 1.5)
                                (cssfn :brightness 1.5)]]}})))

;; Now we can use it inside our app-specific, custom component.
(defn twirling-badge [href src]
    [:a
     (sx {:href href :target :_blank})
     [grayscale-icon-image
      (sx {:class         [:.twirl :.mummy]
           :data-selected true
           :style         {:w       :25px
                           :h       :25px
                           :o       0.7
                           :hover:o 1}
           :src           src})]])

(defn links []
  #_[:div
   (sx :mt--20px)

   #_[ui/primary-button (sx {:on-click lightswitch!}) "¯\\_(ツ)_/¯"]
   #_[ui/secondary-button (sx {:style {:hover:bgc :aliceblue}}) "hi" "bye"]
   ]
  [:div
     (sx {:class [:absolute :flex-row-c]
          :style {:w  :100%
                  :mt :28px}})
     [:div
      (sx {:class [:flex-row-c]
           :style {:w          :70px
                   :>a:display :inline-flex}})
      [twirling-badge
       "https://github.com/paintparty/kushi"
       "graphics/github.svg"]
    ;; Example usage below of our example component
    ;; Uncomment to see them on page
      #_[twirling-badge
         "https://clojars.org/org.clojars.paintparty/kushi"
         "graphics/clojars-logo.png"]
      #_[twirling-badge
         "https://twitter.svg"
         "graphics/twitter.svg"]]])
