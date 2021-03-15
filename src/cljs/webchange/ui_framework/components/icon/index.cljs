(ns webchange.ui-framework.components.icon.index
  (:require
    [webchange.ui-framework.components.icon.icon-arrow-left :as arrow-left]
    [webchange.ui-framework.components.icon.icon-arrow-right :as arrow-right]
    [webchange.ui-framework.components.icon.icon-font-family :as font-family]
    [webchange.ui-framework.components.icon.icon-font-size :as font-size]))

(defn component
  [{:keys [icon]}]
  [:div.wc-icon
   (case icon
     "arrow-left" arrow-left/data
     "arrow-right" arrow-right/data
     "font-family" font-family/data
     "font-size" font-size/data)])
