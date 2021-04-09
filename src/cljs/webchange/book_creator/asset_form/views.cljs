(ns webchange.book-creator.asset-form.views
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn asset-form
  [{:keys [class-name disabled? on-save-click top-controls]
    :or   {disabled? false}}]
  (r/with-let [this (r/current-component)]
    [:div {:class-name (get-class-name (-> {"asset-form" true}
                                           (assoc class-name (some? class-name))))}
     (when (some? top-controls)
       (into [:div.asset-controls]
             top-controls))
     (into [:div.asset-form-content]
           (r/children this))
     [:div.buttons-block
      (when (fn? on-save-click)
        [button {:variant   "contained"
                 :color     "primary"
                 :size      "big"
                 :disabled? disabled?
                 :on-click  on-save-click}
         "Apply"])]]))
