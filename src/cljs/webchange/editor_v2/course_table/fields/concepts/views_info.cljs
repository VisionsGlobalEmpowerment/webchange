(ns webchange.editor-v2.course-table.fields.concepts.views-info
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :as s]
    [reagent.core :as r]))

(defn info-from
  [{:keys [data]}]
  [ui/list
   (for [[lesson-set-name lesson-set-data] (:lesson-sets data)]
     ^{:key lesson-set-name}
     [ui/list-item {:class-name "lesson-set-item"}
      (if-not (empty? lesson-set-data)
        (let [{:keys [items]} lesson-set-data
              concepts-names (->> (map :name items)
                                  (map s/capitalize)
                                  (s/join ", "))]
          [ui/tooltip {:title concepts-names}
           [ui/list-item-text {:primary                  lesson-set-name
                               :secondary                concepts-names
                               :primaryTypographyProps   {:class-name "lesson-set-primary"}
                               :secondaryTypographyProps {:class-name "lesson-set-secondary"}}]])
        [ui/list-item-text {:secondary                "Not defined"
                            :secondaryTypographyProps {:class-name "lesson-set-secondary"}}])])])
