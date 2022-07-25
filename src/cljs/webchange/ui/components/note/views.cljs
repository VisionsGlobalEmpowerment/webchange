(ns webchange.ui.components.note.views
  (:require 
    [webchange.ui.components.icon.views :refer [system-icon]]))

(defn note
  [{:keys [text]}]
  [:div.bbs--note
   [system-icon {:icon "info"}]
   text])
