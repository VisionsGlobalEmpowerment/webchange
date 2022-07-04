(ns webchange.admin.pages.courses.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.courses.state :as state]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.widgets.page.views-dep :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn- courses-list-item
  [{:keys [name slug lang]}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-course slug])
        handle-view-click #(re-frame/dispatch [::state/view-course slug])]
    [l/list-item {:name    name
                  :actions [:<>
                            [ui/icon-button {:icon     "copy"
                                             :title    "View"
                                             :variant  "light"
                                             :on-click handle-edit-click}]
                            [ui/icon-button {:icon     "edit"
                                             :title    "Edit"
                                             :variant  "light"
                                             :on-click handle-view-click}]]}
     [l/content-right {:class-name "item-content-right"}
      [:p [:strong "Language: "] lang]]]))

(defn- courses-list
  []
  (let [loading? @(re-frame/subscribe [::state/courses-loading?])
        courses @(re-frame/subscribe [::state/courses])]
    (if loading?
      [ui/loading-overlay]
      [l/list {:class-name "courses-list"}
       (for [{:keys [id] :as course} courses]
         ^{:key id}
         [courses-list-item course])])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [handle-add-click #(re-frame/dispatch [::state/add-course])]
      [page/page {:class-name "page--courses"}
       [page/_header {:title "Courses"
                      :icon  "presentation"
                      :actions [ui/icon-button {:icon       "add"
                                                :title      "Add Course"
                                                :on-click   handle-add-click}
                                "Add Course"]}]
       [page/main-content
        [courses-list]]])))
