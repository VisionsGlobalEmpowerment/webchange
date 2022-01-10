(ns webchange.editor-v2.activity-dialogs.menu.sections.phrase.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-dialogs.menu.state :as parent-state]
    [webchange.editor-v2.activity-dialogs.form.state :as state-dialog]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:phrase])
       (parent-state/path-to-db)))

;; Actions

(re-frame/reg-sub
  ::available-actions
  (fn []
    [(re-frame/subscribe [::state-dialog/show-concepts?])
     (re-frame/subscribe [::state-dialog/current-concept])])
  (fn [[show-concepts? current-concept]]
    (cond-> [{:text  "Add to scene"
              :value "scene"}]
            (and show-concepts?
                 (some? current-concept)) (conj {:text  "Add to concept"
                                                 :value "concept"}))))
