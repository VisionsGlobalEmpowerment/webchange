(ns webchange.ui-framework.components.icon.index
  (:require
    [webchange.ui-framework.components.icon.icon-add :as add]
    [webchange.ui-framework.components.icon.icon-arrow-left :as arrow-left]
    [webchange.ui-framework.components.icon.icon-arrow-right :as arrow-right]
    [webchange.ui-framework.components.icon.icon-close :as close]
    [webchange.ui-framework.components.icon.icon-font-family :as font-family]
    [webchange.ui-framework.components.icon.icon-font-size :as font-size]
    [webchange.ui-framework.components.icon.icon-image :as image]
    [webchange.ui-framework.components.icon.icon-mic :as mic]))

(defn component
  [{:keys [icon]}]
  [:div.wc-icon
   (case icon
     "add" add/data
     "arrow-left" arrow-left/data
     "arrow-right" arrow-right/data
     "close" close/data
     "font-family" font-family/data
     "font-size" font-size/data
     "image" image/data
     "mic" mic/data)])
