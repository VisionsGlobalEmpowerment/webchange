(ns webchange.editor-v2.course-table.fields.concepts.views-edit
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.fields.concepts.state :as state]))

(defn- lesson-set
  [{:keys [name component-id]}]
  (r/with-let [edit? (r/atom false)
               handle-open-menu (fn [] (reset! edit? true))
               handle-close-menu (fn [] (reset! edit? false))
               handle-item-picked (fn [e]
                                    (let [item-id (.. e -target -value)]
                                      (re-frame/dispatch [::state/add-dataset-item item-id name component-id])))
               handle-remove-click (fn [item-id] (re-frame/dispatch [::state/remove-dataset-item item-id name component-id]))]
    (let [selected-items @(re-frame/subscribe [::state/selected-dataset-items name component-id])
          available-dataset-items @(re-frame/subscribe [::state/available-dataset-items name component-id])]
      [:div {:style {:padding       "0"
                     :margin-bottom "10px"}}
       [ui/typography {:variant "body1"}
        (clojure.core/name name)]

       (for [{:keys [id name]} selected-items]
         ^{:key id}
         [ui/chip {:label      name
                   :variant    "outlined"
                   :class-name "dataset-item-chip"
                   :on-delete  #(handle-remove-click id)}])

       (if @edit?
         [ui/select {:value     ""
                     :open      true
                     :on-close  handle-close-menu
                     :on-change handle-item-picked
                     :on-wheel  #(.stopPropagation %)}
          (for [{:keys [id name]} available-dataset-items]
            ^{:key id}
            [ui/menu-item {:value id} name])]
         [ui/chip {:label      "+"
                   :variant    "outlined"
                   :class-name "dataset-item-chip"
                   :on-click   #(handle-open-menu)}])])))

(defn edit-form
  [{:keys [data]}]
  (r/with-let [component-id (:idx data)
               _ (re-frame/dispatch [::state/init data component-id])]
    (let [current-sets @(re-frame/subscribe [::state/current-lesson-sets component-id])]
      [:div {:style {:min-width "200px"}}
       (for [[concepts-name _] current-sets]
         ^{:key concepts-name}
         [lesson-set {:name         concepts-name
                      :component-id component-id}])])
    (finally
      (re-frame/dispatch [::state/save component-id]))))
