(ns webchange.editor-v2.course-table.views-row-skills
  (:require
    [webchange.editor-v2.course-table.views-row-common :refer [field-cell]]))

(defn skills-abbr
  [{:keys [data] :as props}]
  [field-cell (merge props
                     {:cell-props {:class-name "skills"}})
   [:ul
    (for [{:keys [abbr]} (:skills data)]
      ^{:key abbr}
      [:li abbr])]])

(defn skills-description
  [{:keys [data] :as props}]
  [field-cell (merge props
                     {:cell-props {:class-name "skills"}})
   [:ul
    (for [{:keys [abbr name]} (:skills data)]
      ^{:key abbr}
      [:li name])]])
