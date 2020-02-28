(ns webchange.student-dashboard.toolbar.course-selector.flags.flag-es)

(defn get-shape
  []
  [:svg {:xmlns   "http://www.w3.org/2000/svg"
         :width   "18"
         :height  "14"
         :viewBox "0 0 18 14"}
   [:g
    [:g
     [:g [:path {:fill "#ff4a4a" :d "M3 0h12a3 3 0 0 1 3 3v1H0V3a3 3 0 0 1 3-3z"}]]
     [:g [:path {:fill "#ff4a4a" :d "M0 10h18v1a3 3 0 0 1-3 3H3a3 3 0 0 1-3-3z"}]]
     [:g [:path {:fill "#ffc900" :d "M0 4h18v6H0z"}]]
     [:g [:path {:fill "#ff4a4a" :d "M4 5h2a.5.5 0 0 1 .5.5v2a1.5 1.5 0 1 1-3 0v-2A.5.5 0 0 1 4 5z"}]]
     [:g [:path {:fill "#fff" :d "M7.5 5.5h1v3h-1z"}]]
     [:g [:path {:fill "#fff" :d "M1.5 5.5h1v3h-1z"}]]]]])
