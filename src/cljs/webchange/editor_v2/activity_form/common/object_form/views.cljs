(ns webchange.editor-v2.activity-form.common.object-form.views
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-form.common.object-form.state :as state]
    [webchange.editor-v2.activity-form.common.object-form.animation-form.views :as animation-form]
    [webchange.editor-v2.activity-form.common.object-form.image-form.views :as image-form]
    [webchange.editor-v2.activity-form.common.object-form.text-form.views :as text-form]
    [webchange.editor-v2.activity-form.common.object-form.video-form.views :as video-form]
    [webchange.ui-framework.components.index :refer [button with-confirmation]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]

    [webchange.subs :as subs]
    [webchange.utils.scene-data :as utils]))

(def form-components
  {"animation" animation-form/form
   "text"      text-form/form
   "image"     image-form/form
   "video"     video-form/form})

(defn available-object-type?
  [object-type]
  (contains? form-components object-type))

(defn- object-form-view
  [{:keys [class-name objects-data objects-names object-type on-destroy on-save-click ref show-save-button? title]
    :or   {on-save-click     #()
           show-save-button? true}}]
  (when (available-object-type? object-type)
    (r/with-let [id (->> (random-uuid) (str) (take 8) (str/join ""))
                 _ (when (fn? ref) (ref id))]
      (let [component (get form-components object-type)
            component-props {:id            id
                             :objects-data  objects-data
                             :objects-names objects-names}
            disabled? @(re-frame/subscribe [::state/disabled? id])]
        [:div {:class-name (get-class-name (-> {"activity-object-form" true}
                                               (assoc class-name (some? class-name))))}
         (when (some? title)
           [:h3 title])
         [:div.object-form-content
          [component component-props]]
         (when show-save-button?
           [:div.buttons-block
            [button {:variant   "contained"
                     :color     "primary"
                     :size      "big"
                     :disabled? disabled?
                     :on-click  #(on-save-click id)}
             "Apply"]])])
      (finally
        (on-destroy id)))))

(defn- group-form
  [{:keys [component-key objects-data on-save-click] :as props
    :or   {on-save-click #()}}]
  (r/with-let [children-ids (atom [])]
    (let [scene-data @(re-frame/subscribe [::subs/current-scene-data])
          handle-child-ref #(swap! children-ids conj %)
          handle-group-save #(on-save-click @children-ids)
          assets (->> (get objects-data :children [])
                      (map keyword)
                      (map (fn [child-name]
                             (let [object-data (utils/get-scene-object scene-data child-name)]
                               {:alias         (get object-data :alias)
                                :object-type   (get object-data :type)
                                :objects-data  object-data
                                :objects-names [child-name]})))
                      (filter (fn [{:keys [object-type]}]
                                (available-object-type? object-type))))]
      [:div.group-form
       (for [{:keys [alias object-type] :as asset} assets]
         ^{:key (->> (get asset :objects-names)
                     (map clojure.core/name)
                     (clojure.string/join "--")
                     (str component-key "--"))}
         [object-form-view (merge props
                                  (select-keys asset [:object-type :objects-data :objects-names])
                                  {:title             (or alias object-type)
                                   :class-name        "group-form-item"
                                   :show-save-button? false
                                   :ref               handle-child-ref})])
       [button {:variant  "contained"
                :color    "primary"
                :size     "big"
                :on-click handle-group-save}
        "Apply"]])))

(defn object-form
  []
  (r/with-let [handle-save #(re-frame/dispatch [::state/save %])
               handle-reset #(re-frame/dispatch [::state/reset %])
               handle-save-and-reset #(re-frame/dispatch [::state/save % {:reset? true}])
               handle-destroy (fn [id]
                                (if @(re-frame/subscribe [::state/has-changes? id])
                                  (with-confirmation {:message      "Save changes?"
                                                      :confirm-text "Save"
                                                      :discard-text "Don't save"
                                                      :on-confirm   #(handle-save-and-reset id)
                                                      :on-discard   #(handle-reset id)})
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
      (when (some? data)
        (if (= type "group")
          ^{:key component-key}
          [group-form (merge component-props {:component-key component-key})]
          ^{:key component-key}
          [object-form-view component-props])))))
