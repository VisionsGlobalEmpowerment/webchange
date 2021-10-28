(ns webchange.editor-v2.dialog.dialog-form.views-volume
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.translator.translator-form.state.actions :as translator-form.actions]
    [webchange.editor-v2.dialog.utils.dialog-action :refer [get-inner-action]]
    [webchange.editor-v2.dialog.dialog-form.state.actions :as dialog-form.actions]
    [webchange.ui-framework.components.index :refer [icon range-input]]))

(defn volume-view
  [{:keys [value on-change]}]
  [:div.volume-control
   [icon {:icon       "volume"
          :class-name "volume-icon"}]
   [range-input {:value       @value
                 :class-name  "volume-input"
                 :on-change   #(reset! value %)
                 :on-mouse-up #(on-change @value)
                 :min         0
                 :max         1
                 :step        0.01}]])

(re-frame/reg-sub
  ::current-volume
  (fn []
    (let [phrase-action (re-frame/subscribe [::translator-form.actions/current-phrase-action])]
      (or (-> @phrase-action get-inner-action :volume) 1))))

(defn volume
  []
  (let [value @(re-frame/subscribe [::current-volume])
        handle-change #(re-frame/dispatch [::dialog-form.actions/set-phrase-action-volume %])]
    (let [current-value (r/atom value)]
      [volume-view {:value     current-value
                    :on-change handle-change}])))
