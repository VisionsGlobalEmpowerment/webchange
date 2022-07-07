(ns webchange.parent.components.header-button.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn- header-button
  [{:keys [chip class-name on-click]}]
  (->> (r/current-component)
       (r/children)
       (into [ui/button
              (cond-> {:color      "blue-1"
                       :shape      "rounded"
                       :class-name (ui/get-class-name {"bbs-parent--header-button" true
                                                       class-name                  (some? class-name)})
                       :on-click   on-click}
                      (some? chip) (merge {:chip       chip
                                           :chip-color "yellow-1"}))])))
