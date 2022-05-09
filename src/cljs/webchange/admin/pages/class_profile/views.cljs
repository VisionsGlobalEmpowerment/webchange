(ns webchange.admin.pages.class-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.counter.views :refer [counter]]
    [webchange.admin.pages.class-profile.state :as state]
    [webchange.admin.widgets.class-form.views :refer [class-form]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :refer [button input]]))

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

(defn- class-info
  []
  (let [class-data @(re-frame/subscribe [::state/class-data])]
    [page/block {:title "Class Info"}
     [class-form {:data      class-data
                  :on-change [::state/set-class-data]}]]))

(defn- footer
  []
  (let [handle-save-click #(re-frame/dispatch [::state/save-class])
        save-button-enabled? @(re-frame/subscribe [::state/save-button-enabled?])]
    [:<>
     [button {:on-click  handle-save-click
              :disabled? (not save-button-enabled?)}
      "Save"]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page
     [page/main-content {:title  "Class Profile"
                         :footer [footer]}
      [class-counter]
      [statistics]
      [class-info]]
     [page/side-bar
      [no-data]]]))
