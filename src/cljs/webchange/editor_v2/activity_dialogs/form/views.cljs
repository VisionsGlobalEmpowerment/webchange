(ns webchange.editor-v2.activity-dialogs.form.views
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [webchange.editor-v2.activity-dialogs.form.state :as state]
   [webchange.editor-v2.activity-dialogs.form.action-unit.views :refer [action-unit]]
   [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
   [webchange.editor-v2.activity-form.common.interpreter-stage.state :as state-stage]))

(defn- dialog-form
  [{:keys [nodes title] :as dialog-data}]
  (let [stage-key @(re-frame/subscribe [::state-stage/stage-key])]
    [:div.sheet
     [:h3 title]
     (for [[idx {:keys [path concept-name] :as action}] (map-indexed vector nodes)]
       ^{:key (concat [(count nodes)] path [stage-key concept-name])}
       [action-unit (merge action
                           {:idx idx})])]))

(defn- track-selector
  []
  (let [current-track @(re-frame/subscribe [::state/current-track])
        tracks @(re-frame/subscribe [::state/available-tracks])
        handle-click #(re-frame/dispatch [::state/set-current-track %])]
    [:div.track-selector
     [:select.drop-down {:value     current-track
                         :on-change #(handle-click (-> % .-target .-value js/parseInt))}
      (for [{:keys [text idx]} tracks]
        ^{:key idx}
        [:option {:value idx}
         text])]]))

(defn activity-dialogs-form
  []
  (r/with-let [_ (re-frame/dispatch [::state/init])
               handle-close #(re-frame/dispatch [::translator-form/reset-state])]
    (let [script-data @(re-frame/subscribe [::state/script-data])
          ]
      [:div {:class-name "activity-script-form"}
       [:div.work-field
        [track-selector]
        [:div.actions-script
         (for [{:keys [action-path] :as dialog-data} script-data]
           ^{:key (str action-path)}
           [dialog-form dialog-data])]]])
    (finally
      (handle-close))))
