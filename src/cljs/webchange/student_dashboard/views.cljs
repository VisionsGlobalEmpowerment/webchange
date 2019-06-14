(ns webchange.student-dashboard.views
  (:require
    [webchange.student-dashboard.scene-items.views-scene-list :refer [scene-list]]
    [webchange.student-dashboard.life-skills.views-life-skill-list :refer [life-skill-list]]
    [webchange.student-dashboard.related-content.views-related-content-list :refer [related-content-list]]
    [webchange.student-dashboard.stubs :refer [stories assessments related-content life-skills]]
    [webchange.ui.theme :refer [with-mui-theme]]))

(defn translate
  [path]
  (get-in {:story           {:title "Continue the story"}
           :assessment      {:title "Assessment"}
           :related-content {:title "Related / Additional content"}
           :life-skills     {:title "Life skills / Extra credit"}}
          path))

(def dashboard-container-styles
  {:display "flex"})

(def main-content-styles
  {:background-color "#ededed"
   :display          "flex"
   :flex             "1 1 auto"
   :flex-direction   "column"
   :padding          "50px"})

(def additional-content-styles
  {:background-color "#f7f7f7"
   :display          "flex"
   :flex-direction   "column"
   :flex             "0 0 auto"
   :padding          "50px"
   :width            500})

(defn- main-content
  [{:keys [stories assessments on-story-click on-assessment-click]}]
  [:div {:style main-content-styles}
   [scene-list (into [{:id :show-more}] stories) {:title    (translate [:story :title])
                                                  :on-click on-story-click}]
   [scene-list assessments {:title    (translate [:assessment :title])
                            :on-click on-assessment-click}]])

(defn- additional-content
  [{:keys [related-content on-related-content-click
           life-skills on-life-skill-click]}]
  [:div {:style additional-content-styles}
   [related-content-list related-content {:title    (translate [:related-content :title])
                                          :on-click on-related-content-click}]
   [life-skill-list life-skills {:title    (translate [:life-skills :title])
                                 :on-click on-life-skill-click}]])

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
