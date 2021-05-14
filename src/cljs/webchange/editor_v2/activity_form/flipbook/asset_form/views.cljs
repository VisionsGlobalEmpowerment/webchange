(ns webchange.editor-v2.activity-form.flipbook.asset-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.flipbook.asset-form.image-form.views :as image-component]
    [webchange.editor-v2.activity-form.flipbook.asset-form.text-form.views :as text-component]
    [webchange.editor-v2.activity-form.flipbook.asset-form.state :as state]
    [webchange.editor-v2.activity-form.flipbook.state :as state-book-creator]
    [webchange.ui-framework.components.index :refer [button with-confirmation]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- get-object-type
  [object-data]
  (cond
    (-> object-data :image some?) :image
    (-> object-data :text some?) :text))

(defn- asset-form-view
  [{:keys [class-name disabled? object-data on-destroy on-save-click]
    :or   {disabled? false}}]
  (r/with-let [id (->> (random-uuid) (str) (take 8) (clojure.string/join ""))]
    (let [object-type (get-object-type object-data)
          component-props {:id          id
                           :object-data object-data}
          disabled? @(re-frame/subscribe [::state/disabled? id])]
      [:div {:class-name (get-class-name (-> {"asset-form" true}
                                             (assoc class-name (some? class-name))))}
       [:div.asset-form-content
        (case object-type
          :image [image-component/form component-props]
          :text [text-component/form component-props])]
       [:div.buttons-block
        [button {:variant   "contained"
                 :color     "primary"
                 :size      "big"
                 :disabled? disabled?
                 :on-click  #(on-save-click id)}
         "Apply"]]])
    (finally
      (on-destroy id))))

(defn- get-object-name
  [object-data]
  (let [type (get-object-type object-data)]
    (get-in object-data [type :name])))

(defn asset-form
  []
  (r/with-let [handle-save #(re-frame/dispatch [::state/save %])
               handle-reset #(re-frame/dispatch [::state/reset %])
               handle-save-and-reset #(re-frame/dispatch [::state/save % {:reset? true}])
               handle-destroy (fn [id]
                                (if @(re-frame/subscribe [::state/has-changes? id])
                                  (with-confirmation {:message    "Save changes?"
                                                      :on-confirm #(handle-save-and-reset id)
                                                      :on-discard #(handle-reset id)})
                                  (handle-reset id)))]
    (let [object-data @(re-frame/subscribe [::state-book-creator/current-object-data])
          object-name (get-object-name object-data)]
      (when (some? object-name)
        ^{:key object-name}
        [asset-form-view {:object-data   object-data
                          :on-destroy    handle-destroy
                          :on-save-click handle-save}]))))
