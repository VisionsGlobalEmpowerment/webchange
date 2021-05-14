(ns webchange.editor-v2.layout.components.sandbox.views-modal
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.sandbox.state :as state]
    [webchange.editor-v2.layout.components.sandbox.state-modal :as state-modal]
    [webchange.ui-framework.components.index :refer [button dialog icon-button select text-input]]))

(defn- select-lesson-set-form
  [{:keys [name]}]
  (let [current-dataset-id @(re-frame/subscribe [::state/current-dataset-id name])
        dataset-options @(re-frame/subscribe [::state/datasets-options])
        handle-dataset-change #(re-frame/dispatch [::state/set-current-dataset-id name %])

        current-dataset-items-ids @(re-frame/subscribe [::state/current-dataset-items-ids name])
        dataset-items-options @(re-frame/subscribe [::state/datasets-items-options name])
        handle-dataset-items-change #(re-frame/dispatch [::state/set-current-dataset-items-ids name %])]
    [:div.lesson-set-row
     [:div.lesson-set-name name]
     [select {:value       current-dataset-id
              :options     dataset-options
              :on-change   handle-dataset-change
              :type        "int"
              :variant     "outlined"
              :placeholder "Select Dataset"}]
     [select {:value     current-dataset-items-ids
              :options   dataset-items-options
              :on-change handle-dataset-items-change
              :multiple? true
              :type      "int"
              :variant   "outlined"
              :placeholder "Select Dataset Items"}]]))

(defn- select-lesson-sets-form
  []
  (let [lesson-sets (->> @(re-frame/subscribe [::state/lesson-sets])
                         (map keyword))]
    [:div.lesson-sets-form
     (for [lesson-set-name lesson-sets]
       ^{:key lesson-set-name}
       [select-lesson-set-form {:name lesson-set-name}])]))

(defn share-form
  []
  (let [link @(re-frame/subscribe [::state/link])]
    [:div.share-form
     [select-lesson-sets-form]
     [:div.link-row
      [text-input {:value       link
                   :disabled?   true
                   :placeholder "Fill form above to get share link"}]]]))

(defn- copy-link-button
  []
  (let [link @(re-frame/subscribe [::state/link])
        copy-link (fn []
                    (let [clipboard (.-clipboard js/navigator)]
                      (-> (.writeText clipboard link)
                          (.then #(re-frame/dispatch [::state-modal/close])))))]
    [button {:on-click  copy-link
             :disabled? (empty? link)
             :size      "big"
             :color     "default"
             :variant   "outlined"}
     "Copy Link"]))

(defn- open-link-button
  []
  (let [link @(re-frame/subscribe [::state/link])]
    [button {:href       link
             :disabled?  (empty? link)
             :size       "big"
             :class-name "open-link"}
     "Preview"]))

(defn share-modal
  []
  (let [open? @(re-frame/subscribe [::state-modal/modal-state])
        close #(re-frame/dispatch [::state-modal/close])]
    (when open?
      [dialog
       {:title    "Share"
        :on-close close
        :actions  [[copy-link-button]
                   [open-link-button]]}
       [share-form]])))
