(ns webchange.admin.pages.class-profile.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-profile.state :as state]
    [webchange.admin.widgets.counter.views :refer [counter]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.profile.views :as profile]
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
  [profile/block {:title "Statistics"}
   [no-data]])

(defn- class-info
  []
  (let [name @(re-frame/subscribe [::state/class-name])
        validation-error @(re-frame/subscribe [::state/name-validation-error])
        handle-change #(re-frame/dispatch [::state/set-class-name %])]
    [profile/block {:title "Class Info"}
     [input {:label     "Class Name"
             :value     name
             :error     validation-error
             :on-change handle-change}]]))

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
    [profile/page
     [profile/main-content {:title  "Class Profile"
                            :footer [footer]}
      [class-counter]
      [statistics]
      [class-info]]
     [profile/side-bar
      [no-data]]]))
