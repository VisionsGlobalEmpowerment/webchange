(ns webchange.dashboard.classes.class-profile.views-profile-table
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]))

(defn profile-table
  [{:keys [columns]} data]
  [ui/table
   [ui/table-head
    [ui/table-row
     (for [column (partition 2 columns)]
       ^{:key (first column)}
       [ui/table-cell
        (second column)])]]
   [ui/table-body
    (for [student data]
      ^{:key (:id student)}
      [ui/table-row
       (for [column (partition 2 columns)]
         ^{:key (str (:id student) "_" (first column))}
         [ui/table-cell
          (get student (first column))])])]])
