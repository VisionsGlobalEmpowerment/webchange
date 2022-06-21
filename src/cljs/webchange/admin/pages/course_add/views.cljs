(ns webchange.admin.pages.course-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.form.views :refer [form]]
    [webchange.admin.pages.course-add.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.validation.specs.course-spec :as course-spec]
    [webchange.ui-framework.components.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn- add-course-form
  []
  (let [saving? @(re-frame/subscribe [::state/data-saving?])
        handle-save #(re-frame/dispatch [::state/create-course %])]
    [:div {:class-name (ui/get-class-name {"widget--course-form" true})}
     [form {:form-id :course-add
            :model   {:name     {:label "Course Name"
                                 :type  :text}
                      :lang {:label   "Language"
                             :type    :select
                             :options language-options}}
            :spec    ::course-spec/create-course
            :on-save handle-save
            :saving? saving?}]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [handle-close-click #(re-frame/dispatch [::state/open-course-list])]
      [page/single-page {:class-name        "page--course-add"
                         :header            {:title    "Add Course"
                                             :icon     "courses"
                                             :on-close handle-close-click}
                         :background-image? true
                         :form-container?   true}
       [add-course-form]])))
