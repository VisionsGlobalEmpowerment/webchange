(ns webchange.editor-v2.activity-dialogs.form.action-unit.views-movement
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.common.character-movements :refer [get-action-text]]
    [webchange.editor-v2.activity-dialogs.form.state :as state]
    [webchange.ui-framework.components.index :refer [icon menu]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn movement-unit
  [{:keys [action character target class-name]}]
  (let [character-data @(re-frame/subscribe [::state/object-data character])
        target-data @(re-frame/subscribe [::state/object-data target])]
    [:div {:class-name (get-class-name (merge class-name
                                              {"movement-unit" true}))}
     [icon {:icon       "movement"
            :class-name "effect-icon"}]
     (get-action-text action {:character-name character
                              :character-data character-data
                              :target-name    target
                              :target-data    target-data})]))
