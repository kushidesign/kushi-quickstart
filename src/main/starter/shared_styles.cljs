(ns starter.shared-styles
  (:require
   [kushi.core :refer (defcss)]))

;; Use kushi.core/defcss macro to define shared styles.

;; defcss can compose other rulesets defined with defcss. Use the name of the
;; defcss (as keyword prefixed with ".").

;; The example below composes on top of :.absolute, a pre-defined defcss that
;; ships with kushi.

;; Typically, you would want to define all your shared styles in a dedicated
;; namespace. This namespace must then be required in your core namespace to
;; make all of your defclasses available globally. You can also define classes 
;; with defcss in the namespace you are using them in.

(defcss ".grayscale"
  {:filter "grayscale(1) contrast(1) brightness(1) invert()"})

(defcss ".small-badge"
  :w--20px
  :h--20px
  :o--1)
