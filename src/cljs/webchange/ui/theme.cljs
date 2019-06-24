(ns webchange.ui.theme
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.core :refer [create-mui-theme]]))

(def w-colors
  {:primary        "#222342"
   :primary-darken "#191a31"
   :default        "#ffffff"
   :secondary      "#fd4142"
   :disabled       "#bababa"})

(def mui-theme (create-mui-theme {:border-radius 20
                                  :palette       {:primary {:main (get-in w-colors [:primary])}
                                                  :text-color (get-in w-colors [:primary])}
                                  :checkbox      {:checked-color (get-in w-colors [:primary])}
                                  :flat-button   {:primary-text-color (get-in w-colors [:primary])}
                                  :raised-button {:primary-color (get-in w-colors [:primary])}
                                  :text-field    {:focus-color (get-in w-colors [:primary])}
                                  :typography    {:use-next-variants true}
                                  }))

(defn get-in-theme
  [path]
  (get-in (js->clj mui-theme) (->> path
                                   (map name)
                                   (vec))))

(defn with-mui-theme
  ([children]
   (with-mui-theme children {}))
  ([children _]
   [ui/mui-theme-provider {:theme mui-theme} children]))
