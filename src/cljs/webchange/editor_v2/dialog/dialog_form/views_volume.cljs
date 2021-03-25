(ns webchange.editor-v2.dialog.dialog-form.views-volume
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.dialog.dialog-form.state.actions-defaults :refer [get-inner-action]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.ui-framework.components.index :refer [icon range-input]]))

(defn- volume-view
  [{:keys [value on-change]}]
  (r/with-let [current-value (r/atom value)]
    [:div.volume-control
     [icon {:icon       "volume"
            :class-name "volume-icon"}]
     [range-input {:value       @current-value
                   :type        "range"
                   :class-name  "volume-input"
                   :on-change   #(reset! current-value %)
                   :on-mouse-up #(on-change @current-value)
                   :min         0
                   :max         1
                   :step        0.01}]]))

(defn volume
  []
  (let [phrase-action (re-frame/subscribe [::translator-form.actions/current-phrase-action])
        handle-change #(re-frame/dispatch [::dialog-form.actions/set-phrase-action-volume %])]
    [volume-view {:value     (-> @phrase-action get-inner-action :volume)
                  :on-change handle-change}]))
