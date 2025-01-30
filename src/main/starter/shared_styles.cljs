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

(defcss ".kushi-opts-grid-row-item"
  :padding-block--1.5em
  :bbe--1px:solid:#efefef
  :bc--$gray200
  :_p:margin-block--0
  :_.kushi-ui-opt-desc_p:fs--0.775rem
  :_a:td--underline)

(defcss ".headline"
  :position--absolute
  :ta--center
  :w--100%
  :top--0
  :left---10px
  :sm:left---16px
  :md:left---24px
  :ff--Inter|sys|sans-serif
  :fw--900
  :fs--120px
  :sm:fs--182px
  :md:fs--249px
  :tt--u
  :font-style--italic
  :mix-blend-mode--darken)

(defcss ".twirl"
  :animation--y-axis-spinner:12s:linear:infinite)

(defcss ".twirl2x"
  :animation--y-axis-spinner:12s:linear:infinite)

(defcss ".twirl4x"
  :animation--y-axis-spinner:12s:linear:infinite)

(defcss ".grayscale"
  {:filter "grayscale(1) contrast(1) brightness(1) invert()"})

(defcss ".small-badge"
  :w--20px
  :h--20px
  :o--1)
