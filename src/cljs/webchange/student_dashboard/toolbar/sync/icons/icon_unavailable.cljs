(ns webchange.student-dashboard.toolbar.sync.icons.icon-unavailable)

(def default-props {:color "#ff4a4a"})

(defn get-shape
  [props]
  (let [{:keys [color]} (merge default-props props)]
    [:svg {:xmlns   "http://www.w3.org/2000/svg"
           :width   "36"
           :height  "22"
           :viewBox "0 0 36 22"}
     [:g
      [:g
       [:g]
       [:g [:path {:fill color :d "M30.266 22a6.14 6.14 0 0 0-.41-12.261c-.15-.01-.3-.01-.45 0a7.938 7.938 0 0 0 0-.85 9 9 0 0 0-17.32-3.42 4.06 4.06 0 0 0-7.08 3.6 6.55 6.55 0 0 0 1.5 12.92z"}]]
       [:g [:path {:fill "#fff" :d "M18 17.5a5 5 0 1 0 0-10 5 5 0 0 0 0 10z"}]]
       [:g [:path {:fill color :d "M18.712 12.505l1.64-1.65a.495.495 0 0 0-.7-.7l-1.65 1.64-1.65-1.64a.495.495 0 1 0-.7.7l1.64 1.65-1.64 1.65a.48.48 0 0 0 0 .7.48.48 0 0 0 .7 0l1.65-1.64 1.65 1.64a.48.48 0 0 0 .7 0 .48.48 0 0 0 0-.7z"}]]]]]))
