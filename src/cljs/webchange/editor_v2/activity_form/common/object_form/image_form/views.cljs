(ns webchange.editor-v2.activity-form.common.object-form.image-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.image-form.state :as state]
    [webchange.ui-framework.components.index :refer [file]]))

(defn form
  [{:keys [id objects-data objects-names]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id objects-data objects-names])]
    (let [handle-change (fn [src]
                          (re-frame/dispatch [::state/set-image-src id src]))]
      [file {:type       "image"
             :show-icon? false
             :on-change  handle-change}])))
