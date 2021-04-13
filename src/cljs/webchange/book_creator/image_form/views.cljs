(ns webchange.book-creator.image-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.book-creator.asset-form.views :refer [asset-form]]
    [webchange.book-creator.image-form.state :as state]
    [webchange.book-creator.state :as state-book-creator]
    [webchange.ui-framework.components.index :refer [file]]))

(defn- image-src-form
  [{:keys [id]}]
  (let [disabled? @(re-frame/subscribe [::state/loading? id])
        handle-change (fn [src]
                        (re-frame/dispatch [::state/set-image-src id src]))]
    [file {:type       "image"
           :show-icon? false
           :disabled?  disabled?
           :on-change  handle-change}]))

(defn- image-form-view
  [{:keys [image]}]
  (r/with-let [id (-> (random-uuid) str)
               _ (re-frame/dispatch [::state/init id {:name         (:name image)
                                                      :spread-image (get-in image [:data :spread-image-name])
                                                      :data         (select-keys (:data image) [:src])}])
               handle-save #(re-frame/dispatch [::state/save id])]
    (let [disabled? @(re-frame/subscribe [::state/loading? id])]
      [asset-form {:on-save-click handle-save
                   :class-name    "text-form"
                   :disabled?     disabled?}
       [image-src-form {:id id}]])
    (finally
      (re-frame/dispatch [::state/reset id]))))

(defn image-form
  []
  (let [{:keys [image] :as object-data} @(re-frame/subscribe [::state-book-creator/current-object-data])]
    (when (some? image)
      ^{:key (:name image)}
      [image-form-view object-data])))
