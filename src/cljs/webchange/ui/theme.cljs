(ns webchange.ui.theme
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.core :refer [get-mui-theme color]]))

(def w-colors
  {:primary        "#222342"
   :primary-darken "#191a31"
   :default        "#ffffff"
   :secondary      "#fd4142"
   :disabled       "#bababa"})

(def mui-theme
  {:mui-theme
   (get-mui-theme {:border-radius 20
                   :palette       {:text-color (get-in w-colors [:primary])}
                   :checkbox      {:checked-color (get-in w-colors [:primary])}
                   :flat-button   {:primary-text-color (get-in w-colors [:primary])}
                   :raised-button {:primary-color (get-in w-colors [:primary])}
                   :text-field    {:focus-color (get-in w-colors [:primary])}
                   })})

(defn with-mui-theme
  ([children]
   (with-mui-theme children {}))
  ([children theme]
   [ui/mui-theme-provider (merge mui-theme theme) children]))
