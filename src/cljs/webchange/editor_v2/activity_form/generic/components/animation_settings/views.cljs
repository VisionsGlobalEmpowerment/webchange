(ns webchange.editor-v2.activity-form.generic.components.animation-settings.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.activity-form.generic.components.animation-settings.state :as state]
    [webchange.ui-framework.components.index :refer [button dialog label switcher]]))

(defn- animation-settings-form
  []
  (let [idle-enabled? @(re-frame/subscribe [::state/idle-animation-enabled?])
        handle-idle-changed #(re-frame/dispatch [::state/set-idle-animation %])]
    [:div
     [switcher {:checked?  idle-enabled?
                :on-change handle-idle-changed
                :label     "Enable idle animation?"}]]))

(defn animation-settings-window
  []
  (let [open? @(re-frame/subscribe [::state/open?])
        handle-close #(re-frame/dispatch [::state/close])]
    (when open?
      [dialog {:open?    open?
               :on-close handle-close
               :title    "Animation Settings"
               :actions  [[button {:on-click handle-close
                                   :color    "default"
                                   :variant  "outlined"
                                   :size     "big"}
                           "Close"]]}
       [animation-settings-form]])))

(defn open-animation-settings-window
  []
  (re-frame/dispatch [::state/open]))
