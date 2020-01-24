(ns webchange.student-dashboard.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [webchange.auth.subs :as as]
    [webchange.routes :refer [redirect-to]]
    [webchange.student-dashboard.scene-items.views-scene-list :refer [scene-list]]
    [webchange.student-dashboard.life-skills.views-life-skill-list :refer [life-skill-list]]
    [webchange.student-dashboard.related-content.views-related-content-list :refer [related-content-list]]
    [webchange.student-dashboard.stubs :refer [related-content life-skills]]
    [webchange.student-dashboard.events :as sde]
    [webchange.student-dashboard.subs :as sds]
    [webchange.ui.theme :refer [with-mui-theme]]
    [webchange.subs :as subs]
    [webchange.interpreter.events :as ie]))

(def courses
  [{:key :test :value "test" :text "Español"}
   {:key :english :value "english" :text "English"}])

(defn translate
  [path]
  (get-in {:header          {:pre-user    "Hello,"
                             :pre-sign-in "Please,"
                             :sign-in     "Sign In"
                             :course      "Course"}
           :story           {:title "Continue the story"}
           :assessment      {:title "Assessments"}
           :related-content {:title "Related / Additional content"}
           :life-skills     {:title "Life skills / Extra credit"}
           :history         {:title "History"}
           :lesson          {:title "Lesson "}}
          path))

(def dashboard-container-styles
  {:display        "flex"
   :flex-direction "column"})

(def dashboard-content-styles
  {:display    "flex"
   :overflow-y "auto"})

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

(def header-styles
  {:flex-grow  1
   :text-align "right"})

(defn- menu-item
  [{:keys [value text]}]
  [ui/menu-item
   {:key value :value value}
   text])

(defn- sync-status
  []
  (let [offline-mode @(re-frame/subscribe [::subs/offline-mode])]
    (case offline-mode
      :not-started [ic/cloud-off {:color "disabled"}]
      :in-progress [ic/cloud-download {:color "disabled"}]
      [ic/cloud-done {:color "disabled"}])))

(defn app-bar
  [{:keys [user course-id]}]
    [ui/app-bar
     {:color    "default"
      :position "static"
      :style    {}}
       [ui/toolbar {:style header-styles}

        [ui/typography
         {:variant "button"
          :style   header-styles}
         (translate [:header :pre-user])]
        [ui/button (str (:first-name user) " " (:last-name user))]
        [ui/form-control {}
         [ui/select {:value course-id :on-change #(re-frame/dispatch [::ie/open-student-course-dashboard (-> % .-target .-value)])}
          (for [course courses]
            (menu-item course))]]
        [sync-status]
        ]])

(defn- continue-the-story []
  (let [loading? @(re-frame/subscribe [::sds/progress-loading])
        finished @(re-frame/subscribe [::sds/finished-activities])
        next-activity @(re-frame/subscribe [::sds/next-activity])
        list (into [] (concat (take-last 3 finished) [next-activity]))]
    (if loading?
      [ui/linear-progress]
      [scene-list list {:title    (translate [:story :title])
                        :show-more #(re-frame/dispatch [::sde/show-more])
                        :on-click (fn [{id :id activity-id :activity-id}] (re-frame/dispatch [::sde/open-activity id activity-id]))}])))

(defn- assessments []
  (let [loading? @(re-frame/subscribe [::sds/progress-loading])
        assessments @(re-frame/subscribe [::sds/assessments])]
    (if loading?
      [ui/linear-progress]
      [scene-list assessments {:title    (translate [:assessment :title])
                               :on-click (fn [{id :id activity-id :activity-id}] (re-frame/dispatch [::sde/open-activity id activity-id]))}])))

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
  (let [user @(re-frame/subscribe [::as/user])
        current-course @(re-frame/subscribe [::subs/current-course])
        handle-related-content-click #(println (str "Related content clicked: " %))
        handle-life-skill-click #(println (str "Life skill clicked: " %))]
    [with-mui-theme
     [:div {:style dashboard-container-styles}
      [app-bar {:user user :course-id current-course}]
      [:div {:style dashboard-content-styles}
       [main-content]
       [additional-content {:related-content          related-content
                            :life-skills              life-skills
                            :on-related-content-click handle-related-content-click
                            :on-life-skill-click      handle-life-skill-click}]]]]))

(defn student-dashboard-finished-page
  []
  (let [finished @(re-frame/subscribe [::sds/finished-activities])
        lessons (group-by :lesson finished)]
    [with-mui-theme
     [:div {:style dashboard-container-styles}
      [:div {:style main-content-styles}
       (for [[lesson-id lesson-items] lessons]
         [scene-list lesson-items {:title    (str (translate [:lesson :title]) lesson-id)
                               :on-click (fn [{id :id activity-id :activity-id}] (re-frame/dispatch [::sde/open-activity id activity-id]))}])
       ]]]))
