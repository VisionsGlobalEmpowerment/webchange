(ns webchange.editor-v2.layout.components.sync-status.history.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.components.sync-status.history.state :as state]
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
  [date-str]
  (let [zone "en-US"
        options {:year   "numeric"
                 :month  "numeric"
                 :day    "numeric"
                 :hour   "numeric"
                 :minute "numeric"
                 :second "numeric"
                 :hour12 false}
        date-obj (js/Date. date-str)
        date-format (js/Intl.DateTimeFormat. zone (clj->js options))]
    (.format date-format date-obj)))

(defn- history-list-item
  [{:keys [created-at description id on-click]}]
  [:div.history-list-item
   [:div.date
    (get-date-time created-at)]
   [:div.description
    description]
   [button {:class-name "restore"
            :on-click   #(on-click id)}
    "Restore"]])

(defn- history-list
  [{:keys [data on-click]}]
  [:ul.history-list
   (for [{:keys [id] :as item} data]
     ^{:key id}
     [history-list-item (merge item
                               {:on-click on-click})])])

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
        :actions  [[update-template-button]]}
       [history-form]])))

(defn history-button
  []
  (r/with-let [_ (re-frame/dispatch [::state/load-versions])
               handle-click #(re-frame/dispatch [::state/open])]
    (let [last-update @(re-frame/subscribe [::state/last-update])]
      [:div
       (into [:div.history-button {:on-click handle-click}]
             (if (some? last-update)
               [[:span "Last saved: "]
                [:span.date (get-date-time last-update)]]
               [[:span "Show History"]]))
       [history-modal]])))
