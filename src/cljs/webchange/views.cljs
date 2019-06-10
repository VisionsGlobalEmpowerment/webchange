(ns webchange.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [webchange.interpreter.components :refer [course]]
    [webchange.editor.index :refer [editor]]
    [webchange.editor.events :as ee]
    [webchange.auth.views :refer [teacher-login register-form student-access-form]]
    [webchange.dashboard.views :refer [dashboard-page]]
    [soda-ash.core :as sa]))

;; course

(defn course-switch
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
         [sa/Header {:as "h1" :content "Pick course"}]]]
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
                        :on-click #(re-frame/dispatch [::events/redirect :course :id "demo"])}
             [sa/Flag {:name "us"}]
             "English"]]
           [sa/GridColumn {}
            [sa/Button {:basic true
                        :on-click #(re-frame/dispatch [::events/redirect :course :id "test"])}
             [sa/Flag {:name "es"}]
             "Español"]]]]]]]]]]])

;; editor

(defn editor-panel [course-id]
  (re-frame/dispatch [::ee/init-editor course-id])
  [editor])

;; main

(defn- panels [panel-name route-params]
  (case panel-name
    :home [course-switch]
    :register-user [register-form]
    :login [teacher-login]
    :student-login [student-access-form]
    :course [course (:id route-params)]
    :course-editor [editor-panel (:id route-params)]
    :dashboard [dashboard-page]
    [:div "test"]))

(defn main-panel []
  (let [{:keys [handler route-params]} @(re-frame/subscribe [::subs/active-route])]
    [panels handler route-params]))
