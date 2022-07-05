(ns webchange.admin.pages.class-profile.course-assign.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.components.select-list.views :refer [select-list]]
    [webchange.admin.pages.class-profile.course-assign.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn- courses-list
  []
  (let [courses @(re-frame/subscribe [::state/courses-list])
        handle-change #(re-frame/dispatch [::state/set-selected-course-id (first %)])]
    [select-list {:data       courses
                  :on-change  handle-change
                  :class-name "students-list"}]))

(defn- actions
  []
  [:div])

(defn assign-class-course
  []
  (r/create-class
    {:display-name "Assign Class Course"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn []
       (let [loading? @(re-frame/subscribe [::state/loading?])
             apply-enable? @(re-frame/subscribe [::state/apply-enable?])
             saving? @(re-frame/subscribe [::state/saving?])
             handle-save #(re-frame/dispatch [::state/change-class-course])
             handle-cancel #(re-frame/dispatch [::state/close])]
         [page/side-bar {:title  "Assign a Course"
                         :icon   "courses"
                         :footer [{:text     "Cancel"
                                   :color    "blue-1"
                                   :on-click handle-cancel}
                                  {:text      "Apply"
                                   :loading?  saving?
                                   :disabled? (not apply-enable?)
                                   :on-click  handle-save}]}
          (if-not loading?
            [:<>
             [courses-list]
             [actions]]
            [ui/loading-overlay])]))}))
