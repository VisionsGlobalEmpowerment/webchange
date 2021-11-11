(ns webchange.editor-v2.activity-dialogs.form.action-unit.views-text-animation
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-common :refer [target-control text-control]]
    [webchange.editor-v2.activity-dialogs.form.state-actions :as state-actions]
    [webchange.editor-v2.activity-dialogs.form.state :as state]
    [webchange.ui-framework.components.index :refer [icon]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- text-animation-target-control
  [{:keys [path source text-object]}]
  (r/with-let [_ (re-frame/dispatch [::state/set-current-text-animation-target path text-object])]
    (let [current-target @(re-frame/subscribe [::state/current-text-animation-target path])
          available-targets @(re-frame/subscribe [::state/available-text-animation-targets])
          handle-target-change (fn [target]
                                 (re-frame/dispatch [::state/set-text-animation-action-target path source target]))]
      [target-control {:value     current-target
                       :options   available-targets
                       :on-change handle-target-change}])))

(defn text-animation-unit
  [{:keys [chunked-text path text text-object] :as props}]
  (let [current-target @(re-frame/subscribe [::state/current-text-animation-target path])
        handle-text-change (fn [new-value]
                             (re-frame/dispatch [::state-actions/set-object-text {:object-name (keyword text-object)
                                                                                  :text        new-value}]))
        full-defined? (->> chunked-text
                           (map :filled?)
                           (every? true?))]
    [:div {:class-name (get-class-name {"unit-content"        true
                                        "text-animation-unit" true})}
     [text-animation-target-control props]
     ^{:key text-object}
     [text-control {:value     text
                    :editable? (some? current-target)
                    :on-change handle-text-change}]
     (when-not full-defined?
       [icon {:icon       "warning"
              :title      "Not all text chunks defined"
              :class-name "chunks-warn"}])]))
