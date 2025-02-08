(ns starter.extras
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


;; Injecting Stylesheets
;; ............................................................................


;; Optional.
;; Using kushi.core/add-stylesheet! to inject a static css file.
;; This stylesheet might be a css reset file, or a third-party style library.
;; This is more of an edge case, as you would typically just do this with a
;; <link> in your index.html. However, if your project uses a clj file to
;; generate the contents of your index's <head> at build time, it may be handy
;; to use this during development to inject new stylesheets without restarting
;; your build.

(add-stylesheet! {:rel  "stylesheet"
                  :href "css/my-global-styles.css"})


;; Optional.
;; Using kushi.core/inject-stylesheet to load a google font.
;; The additional "preconnect" hints will improve Google Fonts performance.

(add-stylesheet!
 {:rel          "preconnet"
  :href         "https://fonts.gstatic.com"
  :cross-origin "anonymous"})

(add-stylesheet!
 {:rel  "preconnet"
  :href "https://fonts.googleapis.com"})

(add-stylesheet!
 {:rel  "stylesheet"
  :href "https://fonts.googleapis.com/css2?family=Inter:wght@900&display=swap"})


;; If you want to add Google Fonts, you can also just use
;; `kushi.core/add-google-fonts!`, which will abstract the above pattern
;; (3 separate calls) into one call:

(add-google-fonts! {:family "Inter"
                   :styles {:normal [400 700]
                            :italic [400 700]}})


;;`kushi.core/add-google-fonts!` accepts any number of args, each one a single
;; map that represents a font-family and associated weights & styles. You can
;; as many different families as you want in a single go (although be mindful
;; of performance):

(add-google-fonts! {:family "Playfair Display"
                    :styles {:normal [400 700]
                             :italic [400 700]}}
                   {:family "Lato"
                    :styles {:normal [100 400]}}
                   {:family "Pacifico"
                    :styles {:normal [400]}})

