(ns webchange.admin.pages.school-profile.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.counter.views :refer [counter]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.school-form.views :refer [school-form]]
    [webchange.admin.pages.school-profile.state :as state]
    [webchange.ui-framework.components.index :refer [button input circular-progress]]))

(defn- school-counter
  []
  (let [{:keys [stats]} @(re-frame/subscribe [::state/school-data])]
    [counter {:items [{:id     :teachers
                       :value  (:teachers stats)
                       :title  "Teachers"
                       :on-click #(re-frame/dispatch [::state/open-teachers])
                       :action {:title "Teachers"
                                :icon  "add"}}
                      {:id     :students
                       :value  (:students stats)
                       :title  "Students"
                       :on-click #(re-frame/dispatch [::state/open-students])
                       :action {:title "Students"
                                :icon  "add"}}
                      {:id     :courses
                       :value  (:courses stats)
                       :title  "Courses"
                       :on-click #(re-frame/dispatch [::state/open-courses])
                       :action {:title "Courses"
                                :icon  "add"}}
                      {:id     :classes
                       :value  (:classes stats)
                       :title  "Classes"
                       :on-click #(re-frame/dispatch [::state/open-classes])
                       :action {:title "Classes"
                                :icon  "add"}}]}]))

(defn- statistics
  []
  [page/block {:title "Statistics"}
   [no-data]])

(defn- side-bar
  [{:keys [school-id]}]
  (let [handle-data-save #(re-frame/dispatch [::state/set-school-data %])]
    [page/side-bar {:title "School Info"}
     [school-form {:id      school-id
                   :on-save handle-data-save}]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [school-id]}]
    (let [school-name @(re-frame/subscribe [::state/school-name])]
      [page/page
       [page/main-content {:title school-name}
        [school-counter]
        [statistics]]
       [side-bar {:school-id school-id}]])))
