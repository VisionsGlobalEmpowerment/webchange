(ns webchange.editor-v2.course-table.fields.concepts.views-edit
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.fields.concepts.state :as concepts-state]))

(defn- lesson-set-item
  [{:keys [name current-value options on-change]}]
  [:div {:style {:padding       "0"
                 :margin-bottom "10px"}}
   [ui/typography {:variant "body1"}
    (clojure.core/name name)]
   [ui/select
    {:value      (or current-value "")
     :on-change  #(on-change name (->> % .-target .-value))
     :on-wheel   #(.stopPropagation %)
     :style      {:max-width    "150px"
                  :padding-top  "2px"
                  :padding-left "8px"
                  :font-size    "10px"}
     :class-name "concepts-select-control"
     :MenuProps  {:class-name "concepts-option-menu"}}
    (for [{:keys [name items]} options]

      (let [items-names (->> items
                             (map :name)
                             (clojure.string/join ", "))]
        ^{:key name}
        [ui/menu-item {:value      name
                       :class-name "concepts-option-menu-item"}
         [ui/typography items-names]]))]])

(defn edit-form
  [{:keys [data]}]
  (r/with-let [_ (re-frame/dispatch [::concepts-state/init data])]
    (let [lesson-sets @(re-frame/subscribe [::concepts-state/available-sets])
          current-sets @(re-frame/subscribe [::concepts-state/current-lesson-sets])
          handle-change (fn [name lesson-set]
                          (re-frame/dispatch [::concepts-state/set-current-lesson-set name lesson-set]))]
      [:div
       (for [[name lesson-set-name] current-sets]
         ^{:key name}
         [lesson-set-item {:name          name
                           :current-value lesson-set-name
                           :options       lesson-sets
                           :on-change     handle-change}])])
    (finally
      (re-frame/dispatch [::concepts-state/save-concepts]))))
