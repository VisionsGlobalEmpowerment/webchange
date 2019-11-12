(ns webchange.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.interpreter.components :refer [course]]
    [webchange.editor.index :refer [editor]]
    [webchange.editor.events :as ee]
    [webchange.editor-v2.events :as ee2]
    [webchange.editor-v2.views :refer [main-view]]
    [webchange.auth.views :refer [teacher-login student-access-form]]
    [webchange.dashboard.events :as dashboard-events]
    [webchange.dashboard.views :refer [dashboard]]
    [webchange.student-dashboard.views :refer [student-dashboard-page student-dashboard-finished-page]]
    [webchange.error-pages.page-404 :refer [page-404]]
    [webchange.views-login-switch :refer [login-switch]]))

(defn- str->int-param
  [map key]
  (let [init-value (get map key)]
    (if init-value
      (.parseInt js/Number (re-find #"\d+" init-value))
      nil)))

(defn- editor-panel [course-id]
  (re-frame/dispatch [::ee/init-editor course-id])
  [editor])

(defn- editor-panel-v2 [course-id scene-id]
  (re-frame/dispatch [::ee/init-editor course-id scene-id])
  (re-frame/dispatch [::ee2/init-editor course-id scene-id])
  [main-view scene-id])

(defn- dashboard-panel
  [content route-params]
  (re-frame/dispatch
    [::dashboard-events/set-main-content content
     {:class-id   (str->int-param route-params :class-id)
      :student-id (str->int-param route-params :student-id)}])
  [dashboard])

(defn- panels
  [panel-name route-params]
  (case panel-name
    :home [login-switch]
    :login [teacher-login :sign-in]
    :register-user [teacher-login :sign-up]
    :course [course (:id route-params)]
    ;; editor
    :course-editor [editor-panel (:id route-params)]
    :course-editor-v2 [editor-panel-v2 (:id route-params) nil]
    :course-editor-v2-scene [editor-panel-v2 (:id route-params) (:scene-id route-params)]
    ;; teacher dashboard
    :dashboard [dashboard-panel :dashboard route-params]
    :dashboard-classes [dashboard-panel :classes-list route-params]
    :dashboard-class-profile [dashboard-panel :class-profile route-params]
    :dashboard-students [dashboard-panel :students-list route-params]
    :dashboard-student-profile [dashboard-panel :student-profile route-params]
    ;; student dashboard
    :student-login [student-access-form]
    :student-dashboard [student-dashboard-page]
    :finished-activities [student-dashboard-finished-page]
    [page-404]))

(defn main-panel []
  (let [{:keys [handler route-params]} @(re-frame/subscribe [::subs/active-route])]
    [panels handler route-params]))
