(ns webchange.editor-v2.course-table.views-row-concepts
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [clojure.string :as s]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.views-row-common :refer [field-cell]]))

(defn concepts
  [{:keys [data] :as props}]
  [field-cell (merge props
                     {:cell-props {:class-name "concepts"}})
   [ui/list
    (for [[lesson-set-name lesson-set-data] (:lesson-sets data)]
      ^{:key lesson-set-name}
      [ui/list-item {:class-name "lesson-set-item"}
       (if-not (empty? lesson-set-data)
         (let [{:keys [items name]} lesson-set-data
               concepts-names (->> (map :name items)
                                   (map s/capitalize)
                                   (s/join ", "))]
           [ui/tooltip {:title concepts-names}
            [ui/list-item-text {:primary                  (r/as-element [:div.lesson-set-primary
                                                                         [:span (clojure.core/name lesson-set-name) ":"]
                                                                         [:span "\"" name "\""]])
                                :secondary                (r/as-element concepts-names)
                                :secondaryTypographyProps {:class-name "lesson-set-secondary"}}]])
         [ui/list-item-text {:secondary                "Not defined"
                             :secondaryTypographyProps {:class-name "lesson-set-secondary"}}])])]])
