(ns webchange.lesson-builder.widgets.welcome.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn- card
  [{:keys [action idx icon]}]
  [:div {:class-name (ui/get-class-name {"widget--welcome-card"                 true
                                         (str "welcome-card--background-v" idx) true})}
   [ui/icon {:icon       icon
             :class-name "welcome-card--icon"}]
   (->> (r/current-component)
        (r/children)
        (into [:div.welcome-card--text]))
   (when (some? action)
     [ui/button (merge {:shape      "rounded"
                        :class-name "welcome-card--button"}
                       action)])])

(defn welcome
  []
  [:div.widget--welcome
   [card {:icon "build"
          :idx  1}
    "In the right sidebar, use the "
    [:b "Build"]
    " section to add any new elements to the activity"]
   [card {:icon "levels"
          :idx  2}
    "Many components have already been filled in for you. To make change, go to "
    [:b "Scene Layers"]
    " and click edit"]
   [card {:icon "translate"
          :idx  3}
    "Within the script you can add dialogue and then use the "
    [:b "Voice & Translate"]
    " section to add the audio"]
   [card {:icon   "navigation/play"
          :idx    4
          :action {:text "See Videos"
                   :href "#"}}
    "For more tips on building activities check out our help videos"]])
