(ns webchange.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [webchange.interpreter.components :refer [course]]
    [webchange.editor.index :refer [editor]]
    [webchange.editor.events :as ee]
    [webchange.auth.views :refer [teacher-login student-access-form]]
    [webchange.dashboard.views :refer [dashboard-page]]
    [webchange.student-dashboard.views :refer [student-dashboard-page]]
    [webchange.error-pages.page-404 :refer [page-404]]
    [soda-ash.core :as sa]))

(defn login-switch
  []
  [sa/Grid {:centered true
            :columns 2
            :container true
            :vertical-align "middle"}
   [sa/GridRow {}
    [sa/GridColumn {}
     [sa/Segment {:placeholder true}
      [sa/Grid {:centered true
                :vertical-align "middle"}
       [sa/GridRow {}
        [sa/GridColumn {:text-align "center"}
         [sa/Header {:as "h1" :content "Login as"}]]]
       [sa/GridRow {}
        [sa/GridColumn {}
         [sa/Grid {:stackable true
                   :text-align "center"}
          [sa/Divider {:vertical true}
           "Or"]
          [sa/GridRow {:columns 2
                       :vertical-align "middle"}
           [sa/GridColumn {}
            [sa/Button {:basic true
                        :on-click #(re-frame/dispatch [::events/redirect :login])}
             "Teacher"]]
           [sa/GridColumn {}
            [sa/Button {:basic true
                        :on-click #(re-frame/dispatch [::events/redirect :student-login])}
             "Student"]]]]]]]]]]])

;; editor

(defn editor-panel [course-id]
  (re-frame/dispatch [::ee/init-editor course-id])
  [editor])

;; main

(defn- panels [panel-name route-params]
  (case panel-name
    :home [login-switch]
    :login [teacher-login :sign-in]
    :register-user [teacher-login :sign-up]
    :student-login [student-access-form]
    :course [course (:id route-params)]
    :course-editor [editor-panel (:id route-params)]
    :dashboard [dashboard-page]
    :student-dashboard [student-dashboard-page]
    [page-404]))

(defn main-panel []
  (let [{:keys [handler route-params]} @(re-frame/subscribe [::subs/active-route])]
    [panels handler route-params]))
