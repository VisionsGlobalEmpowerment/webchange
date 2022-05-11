(ns webchange.admin.pages.class-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.counter.views :refer [counter]]
    [webchange.admin.pages.class-profile.state :as state]
    [webchange.admin.widgets.class-form.views :refer [class-form]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as c :refer [button input]]))

(defn- class-counter
  []
  [counter {:items [{:id     :teachers
                     :value  0
                     :title  "Teachers"
                     :action {:title "Teachers"
                              :icon  "add"}}
                    {:id     :students
                     :value  0
                     :title  "Students"
                     :action {:title "Students"
                              :icon  "add"}}
                    {:id     :events
                     :value  0
                     :title  "Events"
                     :action {:title "Events"
                              :icon  "add"}}]}])

(defn- statistics
  []
  [page/block {:title "Statistics"}
   [no-data]])

(defn- side-bar
  [{:keys [class-id school-id]}]
  (let [form-editable? @(re-frame/subscribe [::state/form-editable?])
        handle-edit-click #(re-frame/dispatch [::state/set-form-editable (not form-editable?)])
        handle-data-save #(re-frame/dispatch [::state/set-class-data %])]
    [page/side-bar {:title   "Class Info"
                    :actions [:<>
                              [c/icon-button {:icon     "edit"
                                              :variant  "light"
                                              :on-click handle-edit-click}]]}
     [class-form {:class-id  class-id
                  :school-id school-id
                  :editable? form-editable?
                  :on-save   handle-data-save}]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page
     [page/main-content {:title  "Class Profile"}
      [class-counter]
      [statistics]]
     [side-bar props]]))
