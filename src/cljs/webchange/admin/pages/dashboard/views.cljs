(ns webchange.admin.pages.dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.dashboard.state :as state]
    [webchange.admin.pages.dashboard.views-world-map :refer [world-map]]
    [webchange.admin.pages.dashboard.learners-chart :as learners-svg]
    [webchange.admin.pages.dashboard.organisations-chart :as ord-chart-svg]
    [webchange.admin.pages.dashboard.schools-chart :as schools-chart-svg]
    [webchange.admin.pages.school-profile.views :as school-profile]
    [webchange.admin.pages.activities.views :as activities]
    [webchange.ui.index :as ui]))

(defn- overview
  []
  (let [{:keys [activities classes courses schools students teachers]} @(re-frame/subscribe [::state/statistics])
        handle-activities-click #(re-frame/dispatch [::state/open-activities-page])
        handle-courses-click #(re-frame/dispatch [::state/open-courses-page])
        handle-schools-click #(re-frame/dispatch [::state/open-schools-page])]
    [ui/panel {:title              "Blue Brick School Overview"
               :icon               "info"
               :class-name         "overview-panel"
               :class-name-content "overview-panel-content"}
     [ui/card {:text            "Schools"
               :icon            "school"
               :icon-background "yellow-2"
               :counter         schools
               :actions         [{:text     "View Schools"
                                  :on-click handle-schools-click}]}]
     [ui/card {:text    "Activities"
               :icon    "games"
               :counter activities
               :actions [{:text     "View Activities"
                          :on-click handle-activities-click}]}]
     [ui/card {:text    "Courses"
               :icon    "courses"
               :counter courses
               :actions [{:text     "View Courses"
                          :on-click handle-courses-click}]}]
     [ui/card {:text            "Classes"
               :icon            "classes"
               :icon-background "yellow-2"
               :counter         classes
               :background      "transparent"}]
     [ui/card {:text            "Teachers"
               :icon            "teachers"
               :icon-background "yellow-2"
               :counter         teachers
               :background      "transparent"}]
     [ui/card {:text            "Students"
               :icon            "students"
               :icon-background "yellow-2"
               :counter         students
               :background      "transparent"}]]))

(defn- chart
  [{:keys [data legend]}]
  [:div.chart
   data
   [:ul
    (for [[idx {:keys [color text]}] (map-indexed vector legend)]
      ^{:key idx}
      [:li {:class-name (str "color-" color)} text])]])

(defn- statistics
  []
  [ui/panel {:title              "Statistics"
             :icon               "statistics"
             :class-name         "statistics-panel"
             :class-name-content "statistics-panel-content"}
   [chart {:data   ord-chart-svg/data
           :legend [{:color "blue-2"
                     :text  "Active partnerships"}
                    {:color "blue-1"
                     :text  "Inactive partnerships"}]}]
   [chart {:data   schools-chart-svg/data
           :legend [{:color "yellow-2"
                     :text  "Active Offline Centers"}
                    {:color "yellow-1"
                     :text  "Active Schools Online"}]}]
   [chart {:data   learners-svg/data
           :legend [{:color "green-1"
                     :text  "Global Offline Learners (Active)"}
                    {:color "green-2"
                     :text  "Global Offline Learners (Inactive)"}]}]])

(defn- quick-links
  []
  (let [{:keys [accounts books]} @(re-frame/subscribe [::state/statistics])
        handle-accounts-click #(re-frame/dispatch [::state/open-accounts-page])
        handle-books-click #(re-frame/dispatch [::state/open-books-page])
        handle-create-book-click #(re-frame/dispatch [::state/open-create-book-page])]
    [ui/panel {:title              "Quick Links"
               :color              "blue-1"
               :class-name-content "quick-links-panel"}
     [ui/tab {:icon     "accounts"
              :counter  accounts
              :on-click handle-accounts-click}
      "Accounts"]
     [ui/tab {:icon     "book"
              :counter  books
              :on-click handle-books-click}
      "Books"]
     [ui/tab {:icon     "create"
              :on-click handle-create-book-click
              :action   "plus"}
      "Create a Book"]]))

(defn- countries
  []
  [ui/panel {:title              "Countries"
             :class-name         "countries-panel"
             :class-name-content "countries-panel-content"}
   [world-map {:title     "Schools"
               :countries ["us" "ni" "in" "gt"]}]
   [quick-links]])

(defn page-admin
  []
  (re-frame/dispatch [::state/init-admin])
  (fn []
    [:div.page--dashboard
     [overview]
     [statistics]
     [countries]]))

(defn page-live
  []
  [activities/page])

(defn page-teacher
  []
  (let [current-school @(re-frame/subscribe [::state/current-school])]
    [school-profile/page {:school-id current-school}]))

(defn page
  []
  (let [current-role @(re-frame/subscribe [::state/current-role])]
    (case current-role
      "admin" [page-admin]
      "teacher" [page-teacher]
      "live" [page-live]
      [ui/loading-overlay])))
