(ns webchange.book-creator.asset-form.image-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.book-creator.asset-form.image-form.state :as state]
    [webchange.ui-framework.components.index :refer [file]]))

(defn form
  [{:keys [id object-data]}]
  (r/with-let [_ (re-frame/dispatch [::state/init id object-data])]
    (let [handle-change (fn [src]
                          (re-frame/dispatch [::state/set-image-src id src]))]
      [file {:type       "image"
             :show-icon? false
             :on-change  handle-change}])))
