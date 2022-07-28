(ns webchange.lesson-builder.components.info.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]))

(defn info
  []
  (->> (r/current-component)
       (r/children)
       (into [:div {:class-name "component--info"}
              [ui/icon {:icon "info"
                        :class-name "info-icon"}]])))
