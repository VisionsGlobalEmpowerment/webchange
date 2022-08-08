(ns webchange.lesson-builder.tools.object-form.text-tracing-pattern-form.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.object-form.text-tracing-pattern-form.state :as state]
    [webchange.ui.index :as ui]))

(defn- dashed-component
  [target]
  (let [value @(re-frame/subscribe [::state/dashed target])]
    [ui/switch {:checked?  value
                :on-change #(re-frame/dispatch [::state/set-dashed target %])
                :label     "Dashed line?"}]))

(defn- show-lines-component
  [target]
  (let [value @(re-frame/subscribe [::state/show-lines target])]
    [ui/switch {:checked?  value
                :on-change #(re-frame/dispatch [::state/set-show-lines target %])
                :label     "Show lines?"}]))

(defn fields
  [target]
  (re-frame/dispatch [::state/init target])
  (fn [target]
    [:div.text-tracing-form
     [dashed-component target]
     [show-lines-component target]]))
