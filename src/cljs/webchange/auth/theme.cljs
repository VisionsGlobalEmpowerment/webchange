(ns webchange.auth.theme)

(def w-colors
  {:primary        "#222342"
   :primary-darken "#191a31"
   :default        "#ffffff"
   :secondary      "#fd4142"
   :disabled       "#bababa"})

(def theme
  {:border-radius 20
   :palette       {:type       "light"
                   :primary    {:main (get-in w-colors [:primary])}
                   :text-color (get-in w-colors [:primary])}
   :checkbox      {:checked-color (get-in w-colors [:primary])}
   :flat-button   {:primary-text-color (get-in w-colors [:primary])}
   :raised-button {:primary-color (get-in w-colors [:primary])}
   :text-field    {:focus-color (get-in w-colors [:primary])}
   :typography    {:use-next-variants true}})
