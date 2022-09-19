(ns webchange.lesson-builder.layout.toolbox.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.layout.index :refer [tools-data]]
    [webchange.lesson-builder.layout.toolbox.state :as state]
    [webchange.ui.index :as ui]))

(defn block-toolbox
  [{:keys [class-name]}]
  (let [current-widget @(re-frame/subscribe [::state/current-widget])
        component (->> (get-in tools-data [:default :toolbox])
                       (get-in tools-data [current-widget :toolbox]))]
    [:div {:class-name (ui/get-class-name {"block-toolbox" true
                                           class-name      (some? class-name)})}
     [component]]))
