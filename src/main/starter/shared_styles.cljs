(ns starter.shared-styles
  (:require
   [kushi.core :refer (defclass)]))

;; Use kushi.core/defclass macro to define shared styles.
;; defclasses can compose other defclasses. Use the name of the defclass (as keyword prefixed with ".").
;; The example below composes on top of :.absolute, a pre-defined defclass that ships with kushi.

;; Typically, you would want to define all your shared styles in a dedicated namespace. This namespace
;;   must then be required in your core namespace to make all of your defclasses available globally.

;; You can also define defclasses in the namespace you are using them in.
(defclass headline
  :.absolute
  :ta--center
  :w--100%
  :top--0
  :left---10px
  :sm:left---16px
  :md:left---24px
  :ff--Inter|sans-serif
  :fs--120px
  :sm:fs--182px
  :md:fs--249px
  :tt--u
  :font-style--italic
  :mix-blend-mode--darken)

(defclass twirl
  :animation--y-axis-spinner:12s:linear:infinite)

(defclass twirl2x
  :animation--y-axis-spinner:12s:linear:infinite)

(defclass twirl4x
  :animation--y-axis-spinner:12s:linear:infinite)


