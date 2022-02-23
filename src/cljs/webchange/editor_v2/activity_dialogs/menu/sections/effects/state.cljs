(ns webchange.editor-v2.activity-dialogs.menu.sections.effects.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.menu.state :as parent-state]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:effects])
       (parent-state/path-to-db)))

;; Selected Effect

(re-frame/reg-sub
  ::available-effects
  (fn []
    [(re-frame/subscribe [::parent-state/available-effects])])
  (fn [[available-effects]]
    (map (fn [{:keys [name action type]}]
           {:text  name
            :value action
            :type  type})
         available-effects)))
