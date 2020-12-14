(ns webchange.editor-v2.course-table.fields.skills.views-info)

(defn info-from
  [{:keys [data]}]
  [:ul
   (for [{:keys [abbr]} (:skills data)]
     ^{:key abbr}
     [:li abbr])])

(defn skills-description
  [{:keys [data]}]
  [:ul
   (for [{:keys [abbr name]} (:skills data)]
     ^{:key abbr}
     [:li name])])
