(ns webchange.editor-v2.course-table.views-edit-form-concepts
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.edit-concepts :as concepts-state]
    [webchange.editor-v2.course-table.views-edit-menu :refer [edit-menu]]))

(defn- lesson-set-item
  [{:keys [name current-value options on-change]}]
  [ui/list-item
   [ui/list-item-text {:primary name
                       :style   {:width "50%"}}]
   [ui/select
    {:value     (or current-value "")
     :on-change #(on-change name (->> % .-target .-value))
     :style     {:width "50%"}
     :MenuProps {:class-name "concepts-option-menu"}}
    (for [{:keys [name items]} options]

      (let [items-names (->> items
                             (map :name)
                             (clojure.string/join ", "))]
        ^{:key name}
        [ui/menu-item {:value      name
                       :class-name "concepts-option-item"}
         [ui/typography name]
         [ui/typography items-names]]))]])

(defn concepts-form
  []
  (let [lesson-sets @(re-frame/subscribe [::concepts-state/available-sets])
        current-sets @(re-frame/subscribe [::concepts-state/current-lesson-sets])
        handle-change (fn [name lesson-set]
                        (re-frame/dispatch [::concepts-state/set-current-lesson-set name lesson-set]))]
    [edit-menu {:on-save #(re-frame/dispatch [::concepts-state/save-concepts])}
     [ui/list
      (for [[name lesson-set-name] current-sets]
        ^{:key name}
        [lesson-set-item {:name          name
                          :current-value lesson-set-name
                          :options       lesson-sets
                          :on-change     handle-change}])]]))
