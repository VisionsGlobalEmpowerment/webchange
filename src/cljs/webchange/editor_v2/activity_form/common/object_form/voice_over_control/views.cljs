(ns webchange.editor-v2.activity-form.common.object-form.voice-over-control.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.voice-over-control.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]))

(defn voice-over-control
  [{:keys [id object-name]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id object-name])
               handle-click #(re-frame/dispatch [::state/open-dialog-window id])]
    (let [visible? @(re-frame/subscribe [::state/visible? id])]
      (when visible?
        [icon-button {:icon       "mic"
                      :color      "primary"
                      :class-name "voice-over"
                      :on-click   handle-click}]))))
