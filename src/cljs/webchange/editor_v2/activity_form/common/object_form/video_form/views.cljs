(ns webchange.editor-v2.activity-form.common.object-form.video-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.video-form.state :as state]
    [webchange.ui-framework.components.index :refer [label range-input]]))

(defn- volume-control
  [{:keys [id]}]
  (let [value @(re-frame/subscribe [::state/volume id])
        handle-change #(re-frame/dispatch [::state/set-volume id %])]
    [:div
     [label "Volume"]
     [range-input {:value     value
                   :on-change handle-change
                   :step      0.05}]]))

(defn form
  [{:keys [id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    [:div.video-form
     [volume-control {:id id}]]))
