(ns webchange.editor-v2.activity-dialogs.form.action-unit.views-background-music
  (:require
    [webchange.ui-framework.components.index :refer [icon]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn background-music-unit
  [{:keys [action class-name]}]
  (let [[icon-name effect-name] (if (= action "mute-background-music")
                                  ["music-off" "Mute background music"]
                                  ["music" "Unmute background music"])]
    [:div {:class-name (get-class-name (merge class-name
                                              {"effect-unit" true}))}
     [icon {:icon       icon-name
            :class-name "effect-icon"}]
     effect-name]))
