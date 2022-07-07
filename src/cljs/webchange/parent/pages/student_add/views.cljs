(ns webchange.parent.pages.student-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.parent.components.header-button.views :refer [header-button]]
    [webchange.parent.pages.student-add.state :as state]
    [webchange.ui.index :as ui]
    [webchange.validation.specs.parent-student :as parent-student-specs]))

(def student-model {:name          {:label "Name"
                                    :type  :text}
                    :date-of-birth {:label "Birthday"
                                    :type  :date}
                    :course-slug   {:label   "Course"
                                    :type    :select
                                    :options []}
                    :device        {:label   "Device"
                                    :type    :select
                                    :options []}})

(defn- student-form
  []
  (let [course-options @(re-frame/subscribe [::state/course-options])
        device-options @(re-frame/subscribe [::state/device-options])
        loading? @(re-frame/subscribe [::state/courses-loading?])
        saving? @(re-frame/subscribe [::state/saving?])
        handle-save #(re-frame/dispatch [::state/save %])]
    [ui/form {:form-id  :add-parent-student
              :model    (-> student-model
                            (assoc-in [:device :options] device-options)
                            (assoc-in [:course-slug :options] course-options))
              :spec     ::parent-student-specs/parent-student
              :on-save  handle-save
              :loading? loading?
              :saving?  saving?}]))

(defn- info
  []
  [:div.info
   [:p "Please enter a username or the student’s first name to be shown on their home screen. The name cannot be edited once created."]
   [:p "Also, we recommend playing Blue Brick School activities on Android Tablets. Could you select what device the student will use?"]])

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [handle-back-click #(re-frame/dispatch [::state/open-students-list-page])]
      [:div#page--student-add
       [:h1 "Add A Student" [header-button {:on-click handle-back-click}
                             "Back"]]
       [:div.content
        [student-form]
        [info]]])))
