(ns webchange.admin.pages.courses.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.courses.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))


(defn- courses-list-item
  [{:keys [name slug lang]}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-course slug])
        handle-view-click #(re-frame/dispatch [::state/view-course slug])]
    [ui/list-item {:name    name
                   :info [{:key "Language"
                           :value lang}]
                   :actions [{:icon     "duplicate"
                              :title    "Duplicate"
                              :on-click handle-edit-click}
                             {:icon     "edit"
                              :title    "Edit"
                              :on-click handle-view-click}]}]))

(defn- courses-list
  []
  (let [loading? @(re-frame/subscribe [::state/courses-loading?])
        courses @(re-frame/subscribe [::state/courses])]
    (if loading?
      [ui/loading-overlay]
      [ui/list {:class-name "courses-list"}
       (for [{:keys [id] :as course} courses]
         ^{:key id}
         [courses-list-item course])])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [handle-add-click #(re-frame/dispatch [::state/add-course])]
      [page/single-page {:class-name "page--courses"
                         :header     {:title      "Courses"
                                      :icon       "courses"
                                      :icon-color "blue-2"
                                      :actions    [{:text     "Add Course"
                                                    :icon     "plus"
                                                    :on-click handle-add-click}]}}
       [courses-list]])))
