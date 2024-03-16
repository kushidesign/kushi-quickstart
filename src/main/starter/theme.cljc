(ns starter.theme)


(def user-theme
  {
   ;; An industry-standared css reset is provided via kushi.ui.basetheme/css-reset.
   ;; However, you can create a customized version of kushi.ui.basetheme/css-reset
   ;; and provide it here, and that will be used instead.

   ;;  :css-reset css-reset



   ;; You can augment or customize the global and alias tokens provided via
   ;; kushi.ui.tokens/global and kushi.ui.tokens/alias
   ;; These maps will get merged with the baseline maps for kushi.ui.tokens


   :design-tokens [:--howlite-blue             :#00adef
                   :--deep-fuscsia             :#ec018b
                   :--canary-yellow            :#fef200
                   :--tooltip-font-size        :2rem
                   :--tooltip-border-radius    :0.5rem
                   :--tooltip-offset           :10px
                   :--tooltip-arrow-x-offset   :8px
                   :--tooltip-arrow-y-offset   :4px
                   :--tooltip-delay-duration   :0ms

                   ;; :--tooltip-color            :white
                  ;;  :--tooltip-background-color "#4b346d"
                   :--tooltip-background-color "#dedede"
                   :--tooltip-color            "#dedede"

                   ;; -- NEW !!!
                   :--tooltip-initial-scale                    0.97
                   :--tooltip-offset-transition-distance       :-30px
                  ;;  :--tooltip-transition-duration              :$xxxfast
                   :--tooltip-transition-duration              :$instant
                   :--tooltip-transition-timing-function       :$tooltip-transition-timing-function
                   :--tooltip-transition-timing-function       "cubic-bezier(0.190, 0.510, 0.125, 0.905)"
                   :--tooltip-arrow-transition-timing-function :$tooltip-transition-timing-function
                   :--tooltip-shift-transition-duration        :$fast
                   ]



   ;; Instead of putting font loading code in your front-end application code,
   ;; you can do it all here instead.

  ;;  :font-loading {
  ;;                 :add-system-font-stack?           true
  ;;                 :system-font-stack-weights        [300 700]
  ;;                 :google-fonts                     [{:family "Public Sans"
  ;;                                                     :styles {:normal [100]
  ;;                                                              :italic [300]}}]


  ;;                 ;; The :google-fonts* option can be used as a sugary alterative to the above
  ;;                 ;; :google-fonts option to load all the weights for normal and italic cuts

  ;;                 :google-fonts*                    ["Fira Code" "Inter"]
  ;;                 }


   ;; The :ui entry is intended for theming.
   ;; This is the place to override/specify global styles that will get applied to the body element.
   ;; You can also override or extend default theming values for buttons, tags, etc. across light and dark themes.
   ;; Look at kushi.ui.basetheme/kushi-ui for examples of these kinds of theming values.
   :ui            ["body"
                   {:font-family      :--sans-serif-font-stack
                    :background-color :white}]
   })
