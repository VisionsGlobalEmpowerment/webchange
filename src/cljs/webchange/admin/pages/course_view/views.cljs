(ns webchange.admin.pages.course-view.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.admin.pages.course-view.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.validation.specs.course-spec :as course-spec]
    [webchange.ui-framework.components.index :as c]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn- edit-course-form
  []
  (let [loading? @(re-frame/subscribe [::state/data-loading?])
        saving? @(re-frame/subscribe [::state/data-saving?])
        data @(re-frame/subscribe [::state/course-data])
        handle-save #(re-frame/dispatch [::state/save-course %])
        handle-remove (fn [] (c/with-confirmation {:message    "Are you sure you want to archive this course?"
                                                   :on-confirm #(re-frame/dispatch [::state/archive-course])}))]
    [:div {:class-name (c/get-class-name {"widget--course-form" true})}
     [ui/form {:form-id :course-edit
               :data    data
               :model   {:name {:label "Course Name"
                                :type  :text}
                         :lang {:label   "Language"
                                :type    :select
                                :options language-options}}
               :spec    ::course-spec/edit-course
               :on-save handle-save
               :saving? saving?}]
     [c/icon-button {:icon       "remove"
                     :variant    "light"
                     :class-name "remove-button"
                     :loading?   saving?
                     :on-click   handle-remove}
      "Archive Course"]]))

(defn page
  [props]
  (r/with-let [_ (re-frame/dispatch [::state/init props])]
    (let [handle-close-click #(re-frame/dispatch [::state/open-course-list])]
      [page/single-page {:class-name        "page--course-add"
                         :header            {:title    "Edit Course"
                                             :icon     "courses"
                                             :on-close handle-close-click}
                         :background-image? true
                         :form-container?   true}
       [edit-course-form]])
    (finally
      (re-frame/dispatch [::state/reset-form]))))
