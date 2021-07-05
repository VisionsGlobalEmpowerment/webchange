(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views-text-animation
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.views-common :refer [target-control text-control]]
    [webchange.editor-v2.dialog.dialog-text-form.state-actions :as state-actions]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state]))

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
  [{:keys [text text-object] :as props}]
  (let [handle-text-change (fn [new-value]
                             (re-frame/dispatch [::state-actions/set-object-text {:object-name (keyword text-object)
                                                                                  :text        new-value}]))]
    [:div {:class-name "text-animation-unit"}
     [text-animation-target-control props]
     ^{:key text-object}
     [text-control {:value     text
                    :editable? true
                    :on-change handle-text-change}]]))
