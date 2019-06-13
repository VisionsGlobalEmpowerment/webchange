(ns webchange.student-dashboard.views
  (:require
    [webchange.student-dashboard.stubs :refer [stories assessments related-content life-skills]]
    [webchange.student-dashboard.views-main-content :refer [main-content]]
    [webchange.student-dashboard.views-additional-content :refer [additional-content]]
    [webchange.ui.theme :refer [w-colors with-mui-theme]]))

(def dashboard-container-styles
  {:display "flex"})

(defn student-dashboard-page
  []
  (let [handle-story-click #(println (str "Story clicked: " %))
        handle-assessment-click #(println (str "Assessment clicked: " %))
        handle-related-content-click #(println (str "Related content clicked: " %))
        handle-life-skill-click #(println (str "Life skill clicked: " %))]
    [with-mui-theme
     [:div {:style dashboard-container-styles}
      [main-content {:stories             stories
                     :assessments         assessments
                     :on-story-click      handle-story-click
                     :on-assessment-click handle-assessment-click}]
      [additional-content {:related-content          related-content
                           :life-skills              life-skills
                           :on-related-content-click handle-related-content-click
                           :on-life-skill-click      handle-life-skill-click}]]]))
