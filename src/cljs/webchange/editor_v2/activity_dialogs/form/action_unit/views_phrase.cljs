(ns webchange.editor-v2.activity-dialogs.form.action-unit.views-phrase
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-common :refer [target-control text-control]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-menu :refer [add-concept-action add-scene-action]]
    [webchange.editor-v2.activity-dialogs.form.state :as state]
    [webchange.editor-v2.activity-dialogs.form.state-actions :as state-actions]
    [webchange.editor-v2.dialog.utils.dialog-action :as dialog-action]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- phrase-target-control
  [{:keys [path source character]}]
  (r/with-let [_ (re-frame/dispatch [::state/set-current-target path character])]
    (let [current-target @(re-frame/subscribe [::state/current-target path])
          available-targets @(re-frame/subscribe [::state/available-targets])
          handle-target-change (fn [target]
                                 (re-frame/dispatch [::state/set-phrase-action-target path source target]))]
      [target-control {:value     current-target
                       :options   available-targets
                       :on-change handle-target-change}])))

(defn- phrase-text-control
  [{:keys [action-data path source text placeholder]}]
  (let [handle-enter-press (fn [] (add-scene-action {:action-data action-data}))
        handle-ctrl-enter-press (fn [] (add-concept-action {:action-data action-data}))
        handle-text-change (fn [new-value] (re-frame/dispatch [::state-actions/set-phrase-text {:action-path path
                                                                                                :action-type source
                                                                                                :value       new-value}]))]
    [text-control {:value               (if (= text dialog-action/default-phrase-text) nil text)
                   :placeholder         placeholder
                   :editable?           true
                   :on-change           handle-text-change
                   :on-enter-press      handle-enter-press
                   :on-ctrl-enter-press handle-ctrl-enter-press}]))

(defn phrase-unit
  [{:keys [source concept-name] :as props}]
  (let [concept? (= source :concept)]
    [:div (cond-> {:class-name (get-class-name {"unit-content" true
                                                "phrase-unit"  true
                                                "concept-unit" concept?})}
                  concept? (assoc :title (str "Concept «" concept-name "»")))
     [phrase-target-control props]
     [phrase-text-control props]]))
