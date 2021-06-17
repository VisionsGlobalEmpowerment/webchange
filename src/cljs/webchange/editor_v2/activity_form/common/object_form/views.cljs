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
    [webchange.ui-framework.components.utils :refer [get-class-name]]

    [webchange.subs :as subs]
    [webchange.utils.scene-data :as utils]))

(def form-components
  {"animation" animation-form/form
   "text"      text-form/form
   "image"     image-form/form})

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

(defn- group-form
  [{:keys [component-key objects-data] :as props}]
  (r/with-let [current-asset-idx (r/atom 0)]
    (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])
          assets (->> (get objects-data :children [])
                      (map keyword)
                      (map (fn [child-name]
                             (let [object-data (utils/get-scene-object scene-data child-name)]
                               {:object-type   (get object-data :type)
                                :objects-data  object-data
                                :objects-names [child-name]}))))
          current-asset (nth assets @current-asset-idx)
          component-key (->> (get current-asset :objects-names)
                             (map clojure.core/name)
                             (clojure.string/join "--")
                             (str component-key "--"))]
      [:div.group-form
       [:ul.menu
        (for [[idx {:keys [objects-names]}] (map-indexed vector assets)]
          ^{:key idx}
          [:li {:class-name (get-class-name {"menu-item" true
                                             "selected"  (= idx @current-asset-idx)})
                :on-click   #(reset! current-asset-idx idx)}
           (first objects-names)])]
       ^{:key component-key}
       [object-form-view (merge props current-asset)]])))

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
                             (clojure.string/join "--"))
          component-props {:object-type   type
                           :objects-data  data
                           :objects-names names
                           :on-save-click handle-save
                           :on-destroy    handle-destroy}]
      (cond
        (and (some? data) (= type "group")) ^{:key component-key} [group-form (merge component-props
                                                                                     {:component-key component-key})]
        (and (some? data) (available-object-type? type)) ^{:key component-key} [object-form-view component-props]
        :else (do (logger/warn "Form not defined")
                  (logger/warn "type" type)
                  (logger/warn "names" names)
                  (logger/warn "data" data)
                  nil)))))
