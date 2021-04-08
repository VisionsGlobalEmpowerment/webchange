(ns webchange.dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.common.views :refer [content-page]]
    [webchange.dashboard.dashboard-layout.views :refer [app-bar drawer progress-bar side-menu]]
    [webchange.dashboard.dashboard-layout.utils :refer [get-shift-styles]]
    [webchange.dashboard.classes.views :refer [classes-list class-modal class-delete-modal class-profile]]
    [webchange.dashboard.schools.views :refer [schools-list school-modal school-delete-modal school-sync-modal]]
    [webchange.dashboard.students.views :refer [students-list student-modal student-delete-modal student-remove-from-class-modal student-profile]]
    [webchange.dashboard.courses.views :refer [courses-list-page]]
    [webchange.dashboard.subs :as dashboard-subs]
    [webchange.routes :refer [redirect-to]]))

(def app-bar-height 64)
(def drawer-width 300)

(defn translate
  [path]
  (get-in {:content {:redirect    "Redirect..."
                     :not-defined "Dashboard content is not defined"}}
          path))

(defn main-content
  [main-content]
  (case main-content
    :dashboard (do (redirect-to :dashboard-classes) [content-page {:title (translate [:content :redirect])}])
    :classes-list [classes-list]
    :schools-list [schools-list]
    :courses-list [courses-list-page]
    :class-profile [class-profile]
    :students-list [students-list]
    :student-profile [student-profile]
    [:div (translate [:content :not-defined])]))

(defn- modal-windows
  []
  [:div
   [class-modal]
   [school-modal]
   [school-delete-modal]
   [school-sync-modal]
   [class-delete-modal]
   [student-modal]
   [student-remove-from-class-modal]
   [student-delete-modal]
   ])

(defn dashboard
  []
  (let [drawer-open (r/atom true)]
    (fn []
      (let [is-loading? false
            current-main-content @(re-frame/subscribe [::dashboard-subs/current-main-content])]
        [:div
         [app-bar
          {:on-open-menu #(reset! drawer-open true)
           :drawer-open? @drawer-open
           :drawer-width drawer-width}]
         [drawer {:open     @drawer-open
                  :on-close #(reset! drawer-open false)}
          [side-menu]]
         [:div {:style (merge {:height     (str "calc(100vh - " app-bar-height "px)")
                               :overflow-x "hidden"
                               :overflow-y "auto"}
                              (get-shift-styles @drawer-open drawer-width))}
          (if is-loading?
            [progress-bar]
            [main-content current-main-content])]
         [modal-windows]]))))
