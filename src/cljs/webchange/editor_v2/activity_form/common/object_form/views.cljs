(ns webchange.editor-v2.activity-form.common.object-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.views :as animation-form]
    [webchange.editor-v2.activity-form.common.object-form.image-form.views :as image-form]
    [webchange.editor-v2.activity-form.common.object-form.text-form.views :as text-form]
    [webchange.logger.index :as logger]
    [webchange.ui-framework.components.index :refer [button with-confirmation]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def form-components
  {"animation"  animation-form/form
   "text"  text-form/form
   "image" image-form/form})

(defn- available-object-type?
  [object-type]
  (contains? form-components object-type))

(defn- object-form-view
  [{:keys [class-name objects-data objects-names object-type on-destroy on-save-click]
    :or   {on-save-click #()}}]
  (r/with-let [id (->> (random-uuid) (str) (take 8) (clojure.string/join ""))]
    (let [component (get form-components object-type)
          component-props {:id            id
                           :objects-data  objects-data
                           :objects-names objects-names}
          disabled? @(re-frame/subscribe [::state/disabled? id])]
      [:div {:class-name (get-class-name (-> {"activity-object-form" true}
                                             (assoc class-name (some? class-name))))}
       [:div.object-form-content
        [component component-props]]
       [:div.buttons-block
        [button {:variant   "contained"
                 :color     "primary"
                 :size      "big"
                 :disabled? disabled?
                 :on-click  #(on-save-click id)}
         "Apply"]]])
    (finally
      (on-destroy id))))

(defn object-form
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
    (let [{:keys [data names]} @(re-frame/subscribe [::state/selected-objects])
          type (get data :type)
          component-key (->> (map clojure.core/name names)
                             (clojure.string/join "--"))]
      (if (and (some? data)
               (available-object-type? type))
        ^{:key component-key}
        [object-form-view {:object-type   type
                           :objects-data  data
                           :objects-names names
                           :on-save-click handle-save
                           :on-destroy    handle-destroy}]
        (do (logger/warn "Form not defined")
            (logger/warn "type" type)
            (logger/warn "names" names)
            (logger/warn "data" data)
            nil)))))
