(ns webchange.editor-v2.layout.components.sync-status.history.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.sync-status.history.state :as state]
    [webchange.state.state :as state-core]
    [webchange.ui-framework.components.index :refer [button dialog]]))

(defn- update-template-button
  []
  (let [update-available @(re-frame/subscribe [::state/update-available])
        handle-click #(re-frame/dispatch [::state/update-template])]
    [:div.update-activity-button
     [button {:variant  (if update-available "contained" "outlined")
              :on-click handle-click
              :size     "big"}
      "Update template"]
     (when update-available
       "Update is available")]))

(defn- get-date-time
  [date]
  (let [zone "en-US"
        options {:year   "numeric"
                 :month  "numeric"
                 :day    "numeric"
                 :hour   "numeric"
                 :minute "numeric"
                 :second "numeric"
                 :hour12 false}
        date-format (js/Intl.DateTimeFormat. zone (clj->js options))]
    (.format date-format date)))

(defn- history-list-item
  [{:keys [created-at description first-item? id on-click]}]
  [:div.history-list-item
   [:div.date
    (get-date-time (js/Date. created-at))]
   [:div.description
    description]
   (if first-item?
     [:div.current-state "Current Version"]
     [button {:class-name "restore"
              :on-click   #(on-click id)}
      "Restore"])])

(defn- history-list
  [{:keys [data on-click]}]
  [:ul.history-list
   (for [[idx {:keys [id] :as item}] (map-indexed vector data)]
     ^{:key id}
     [history-list-item (merge item
                               {:on-click    on-click
                                :first-item? (= idx 0)})])])

(defn- history-form
  []
  (let [versions @(re-frame/subscribe [::state/versions])
        handle-restore #(re-frame/dispatch [::state/restore-version %])]
    [:div.history-form
     [history-list {:data     versions
                    :on-click handle-restore}]]))

(defn history-modal
  []
  (let [open? @(re-frame/subscribe [::state/modal-state])
        close #(re-frame/dispatch [::state/close])]
    (when open?
      [dialog
       {:title    "History"
        :on-close close
        :actions  [[update-template-button]]
        :size     "small"}
       [history-form]])))

(defn history-button
  []
  (let [last-update @(re-frame/subscribe [::state-core/last-saved])
        handle-click #(re-frame/dispatch [::state/open])]
    [:div
     (into [:div.history-button {:on-click handle-click}]
           (if (some? last-update)
             [[:span "Last saved: "]
              [:span.date (get-date-time last-update)]]
             [[:span "Show History"]]))
     [history-modal]]))
