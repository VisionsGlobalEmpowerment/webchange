(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.phrase.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.section-block.views :refer [section-block]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.options-list.views :refer [options-list]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.phrase.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn- destination
  []
  (let [available-destinations @(re-frame/subscribe [::state/available-destinations])
        handle-click #(re-frame/dispatch [::state/set-current-destination %])]
    [:div
     [:span.input-label "Select destination:"]
     [options-list {:options  available-destinations
                    :on-click handle-click}]]))

(defn- actions
  []
  (let [available-actions (->> @(re-frame/subscribe [::state/available-actions])
                               (map (fn [action]
                                      (get {:before   ["Before" "insert-before" #(re-frame/dispatch [::state/add-phrase-action :before])]
                                            :after    ["After" "insert-after" #(re-frame/dispatch [::state/add-phrase-action :after])]
                                            :parallel ["Parallel" "insert-parallel" #(re-frame/dispatch [::state/add-phrase-action :parallel])]}
                                           action))))]
    [:div.actions
     [:span.input-label "Add:"]
     (for [[idx [text icon handler]] (map-indexed vector available-actions)]
       ^{:key idx}
       [icon-button
        {:icon     icon
         :size     "small"
         :on-click handler}
        text])]))

(defn form
  []
  [:div.phrase-form
   [section-block {:title "Add phrase"}
    [destination]
    [actions]]])
