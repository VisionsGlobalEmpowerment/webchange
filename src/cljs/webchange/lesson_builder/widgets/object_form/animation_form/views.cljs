(ns webchange.lesson-builder.widgets.object-form.animation-form.views
  (:require
    [webchange.lesson-builder.widgets.choose-character.views :refer [choose-character]]
    [webchange.lesson-builder.widgets.object-form.common.views :refer [flip-component scale-component]]
    [webchange.ui.index :as ui]))

(defn- character->object-state
  [{:keys [change-skeleton? name skin-params]}]
  (cond-> {}
          change-skeleton? (assoc :name name)
          (string? skin-params) (assoc :skin skin-params)
          (map? skin-params) (assoc :skin-names skin-params)))

(defn animation-form
  [{:keys [class-name data on-change] :as props}]
  (let [{:keys [editable?]} data
        options (get editable? :edit-form {:scale true
                                           :flip  true})
        handle-change #(when (fn? on-change) (on-change %))
        handle-character-change (fn [character-data]
                                  (-> (character->object-state character-data)
                                      (handle-change)))
        component-props (merge props
                               {:on-change handle-change})]
    [:div {:class-name (ui/get-class-name {"animation-form-fields" true
                                           class-name              (some? class-name)})}
     [choose-character {:on-change     handle-character-change
                        :default-value data}]
     [:div.additional-fields
      (when (:flip options) [flip-component component-props])
      (when (:scale options) [scale-component component-props])]]))
