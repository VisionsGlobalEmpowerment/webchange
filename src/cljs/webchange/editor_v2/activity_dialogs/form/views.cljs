(ns webchange.editor-v2.activity-dialogs.form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-dialogs.form.state :as state]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views :refer [action-unit]]
    [webchange.editor-v2.translator.translator-form.state.form :as translator-form]
    [webchange.ui-framework.components.index :refer [switcher]]))

(defn- dialog-form
  [{:keys [nodes title action-path]}]
  (let [interactions-blocked? @(re-frame/subscribe [::state/user-interactions-blocked? action-path])
        handle-change (fn [value]
                        (re-frame/dispatch [::state/set-user-interactions-block action-path value]))]
    [:div.sheet
     [:div.sheet-title
      [:h3 title]
      [switcher {:checked?   interactions-blocked?
                 :on-change  handle-change
                 :label      "Block interactions"
                 :class-name "title-control"}]]
     (for [[idx {:keys [path concept-name] :as action}] (map-indexed vector nodes)]
       ^{:key (concat [(count nodes)] path [concept-name])}
       [action-unit (merge action
                           {:idx idx})])]))

(defn- prompt-form
  [{:keys [title]}]
  [:div.prompt-form title])

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
    (let [script-data @(re-frame/subscribe [::state/script-data])]
      [:div {:class-name "activity-script-form"}
       [:div.work-field
        [track-selector]
        [:div.actions-script
         (for [{:keys [type] :as data} script-data]
           (case type
             "dialog" ^{:key (:action-path data)} [dialog-form data]
             "prompt" ^{:key (:title data)} [prompt-form data])
           )]]])
    (finally
      (handle-close))))
