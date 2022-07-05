(ns webchange.admin.pages.course-view.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.admin.pages.course-view.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.validation.specs.course-spec :as course-spec]
    [webchange.ui.index :as ui]
    [webchange.utils.languages :refer [language-options]]))

(defn- remove-window
  []
  (let [{:keys [done? open? in-progress?]} @(re-frame/subscribe [::state/remove-window-state])
        remove #(re-frame/dispatch [::state/archive-course])
        close-window #(re-frame/dispatch [::state/close-remove-window])
        confirm-removed #(re-frame/dispatch [::state/handle-removed])]
    [ui/confirm {:open?      open?
                 :loading?   in-progress?
                 :confirm-text (if done? "Ok" "Yes")
                 :on-confirm (if done? confirm-removed remove)
                 :on-cancel  (when-not done? close-window)}
     (if done?
       "Course successfully deleted"
       "Are you sure you want to delete course?")]))

(defn- edit-course-form
  []
  (let [loading? @(re-frame/subscribe [::state/data-loading?])
        saving? @(re-frame/subscribe [::state/data-saving?])
        data @(re-frame/subscribe [::state/course-data])
        handle-save #(re-frame/dispatch [::state/save-course %])
        handle-remove #(re-frame/dispatch [::state/open-remove-window])]
    [:div {:class-name "widget--course-form"}
     [ui/form {:form-id :course-edit
               :data    data
               :model   {:name {:label "Course Name"
                                :type  :text}
                         :lang {:label   "Language"
                                :type    :select
                                :options language-options}
                         :remove {:label "Remove Course"
                                  :icon "trash"
                                  :type :action
                                  :on-click handle-remove}}
               :spec    ::course-spec/edit-course
               :on-save handle-save
               :saving? saving?}]
     [remove-window]]))

(defn page
  [props]
  (r/with-let [_ (re-frame/dispatch [::state/init props])]
    (let [{:keys [name]} @(re-frame/subscribe [::state/course-data])
          handle-close-click #(re-frame/dispatch [::state/open-course-list])]
      [page/single-page {:class-name        "page--course-add"
                         :header            {:title    name
                                             :icon     "courses"
                                             :icon-color "blue-2"
                                             :on-close handle-close-click}
                         :form-container?   true}
       [edit-course-form]])
    (finally
      (re-frame/dispatch [::state/reset-form]))))
