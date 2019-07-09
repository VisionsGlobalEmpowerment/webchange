(ns webchange.student-dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.scene-items.views-scene-list :refer [scene-list]]
    [webchange.student-dashboard.life-skills.views-life-skill-list :refer [life-skill-list]]
    [webchange.student-dashboard.related-content.views-related-content-list :refer [related-content-list]]
    [webchange.student-dashboard.stubs :refer [related-content life-skills]]
    [webchange.ui.theme :refer [with-mui-theme]]
    [webchange.student-dashboard.events :as sde]
    [webchange.student-dashboard.subs :as sds]))

(defn translate
  [path]
  (get-in {:story           {:title "Continue the story"}
           :assessment      {:title "Assessments"}
           :related-content {:title "Related / Additional content"}
           :life-skills     {:title "Life skills / Extra credit"}
           :history         {:title "History"}}
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

(defn- continue-the-story []
  (let [show-more {:id :show-more}
        finished @(re-frame/subscribe [::sds/finished-activities])
        next-activity @(re-frame/subscribe [::sds/next-activity])
        list (into [] (concat [show-more] (take-last 3 finished) [next-activity]))]
    [scene-list list {:title (translate [:story :title])
                      :on-click (fn [{id :id}]
                                  (if (= id :show-more)
                                    (re-frame/dispatch [::sde/show-more])
                                    (re-frame/dispatch [::sde/open-activity id])))}]))

(defn- assessments []
  (let [assessments @(re-frame/subscribe [::sds/assessments])]
    [scene-list assessments {:title (translate [:assessment :title])
                             :on-click (fn [{id :id}] (re-frame/dispatch [::sde/open-activity id]))}]))

(defn- main-content
  []
  [:div {:style main-content-styles}
   [continue-the-story]
   [assessments]])

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
  (let [handle-related-content-click #(println (str "Related content clicked: " %))
        handle-life-skill-click #(println (str "Life skill clicked: " %))]
    [with-mui-theme
     [:div {:style dashboard-container-styles}
      [main-content]
      [additional-content {:related-content          related-content
                           :life-skills              life-skills
                           :on-related-content-click handle-related-content-click
                           :on-life-skill-click      handle-life-skill-click}]]]))

(defn student-dashboard-finished-page
  []
  (let [finished @(re-frame/subscribe [::sds/finished-activities])]
    [with-mui-theme
     [:div {:style dashboard-container-styles}
      [:div {:style main-content-styles}
        [scene-list finished {:title (translate [:history :title])
                              :on-click (fn [{id :id}] (re-frame/dispatch [::sde/open-activity id]))}]]]]))
