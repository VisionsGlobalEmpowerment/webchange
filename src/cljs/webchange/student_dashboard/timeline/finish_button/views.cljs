(ns webchange.student-dashboard.timeline.finish-button.views
  (:require
    [webchange.ui-framework.components.index :as ui]))

(def img-src {"english" "/images/student_dashboard/good_work_eng.png"
              "spanish" "/images/student_dashboard/good_work_spa.png"})

(defn finish-button
  [{:keys [lang]}]
  [:div {:class-name (ui/get-class-name {"finish-button" true})}
   [:img {:src (or (get img-src lang)
                   (get img-src "english"))}]])
