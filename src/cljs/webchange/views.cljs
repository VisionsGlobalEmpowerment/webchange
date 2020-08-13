(ns webchange.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.subs :as subs]
    [webchange.interpreter.components :refer [course]]
    [webchange.editor.events :as ee]
    [webchange.editor-v2.events :as ee2]
    [webchange.editor-v2.scene.state.skin :as editor-skin]
    [webchange.editor-v2.views :refer [course-view scene-view concept-view add-concept-view lesson-view add-lesson-view]]
    [webchange.auth.views :refer [teacher-login student-access-form]]
    [webchange.dashboard.events :as dashboard-events]
    [webchange.dashboard.views :refer [dashboard]]
    [webchange.student-dashboard.views :refer [student-dashboard-page student-dashboard-finished-page]]
    [webchange.sw-utils.state.status :as sw]
    [webchange.error-pages.page-404 :refer [page-404]]
    [webchange.views-login-switch :refer [login-switch]]
    [webchange.ui.theme :refer [get-in-theme]]
    [webchange.editor-v2.wizard.views :as wizard]))

(defn- get-styles
  []
  {:error-message {:background-color (get-in-theme [:palette :error :dark])}})

(defn- str->int-param
  [map key]
  (let [init-value (get map key)]
    (if init-value
      (.parseInt js/Number (re-find #"\d+" init-value))
      nil)))

(defn- editor-panel-v2 [course-id]
  (re-frame/dispatch [::ee2/init-editor course-id])
  [course-view])

(defn- editor-panel-v2-scene [course-id scene-id]
  (re-frame/dispatch [::ee2/init-editor course-id scene-id])
  (re-frame/dispatch [::ee/select-current-scene scene-id])
  (re-frame/dispatch [::editor-skin/load-characters])
  [scene-view course-id scene-id])

(defn- editor-panel-v2-concept [course-id concept-id]
  (re-frame/dispatch [::ee2/init-editor course-id])
  [concept-view course-id concept-id])

(defn- editor-panel-v2-add-concept [course-id]
  (re-frame/dispatch [::ee2/init-editor course-id])
  [add-concept-view course-id])

(defn- editor-panel-v2-lesson [course-id level lesson]
  (re-frame/dispatch [::ee2/init-editor course-id])
  [lesson-view course-id level lesson])

(defn- editor-panel-v2-add-lesson [course-id level]
  (re-frame/dispatch [::ee2/init-editor course-id])
  [add-lesson-view course-id level])

(defn- dashboard-panel
  [content route-params]
  (re-frame/dispatch
    [::dashboard-events/set-main-content content
     {:class-id   (str->int-param route-params :class-id)
      :student-id (str->int-param route-params :student-id)}])
  [dashboard])

(defn- error-message
  []
  (let [error @(re-frame/subscribe [::sw/current-error])
        handle-close #(re-frame/dispatch [::sw/reset-current-error])
        styles (get-styles)]
    [ui/snackbar {:anchor-origin      {:vertical   "bottom"
                                       :horizontal "right"}
                  :open               (boolean error)
                  :auto-hide-duration 6000
                  :on-close           handle-close}
     [ui/snackbar-content {:style   (:error-message styles)
                           :message (r/as-element [:span
                                                   [:div (str (:message error))]
                                                   [:div (str (:error error))]])}]]))

(defn- panels
  [panel-name route-params]
  (case panel-name
    :home [login-switch]
    :login [teacher-login :sign-in]
    :register-user [teacher-login :sign-up]
    :course [course (:id route-params)]
    ;; editor
    :course-editor-v2 [editor-panel-v2 (:id route-params)]
    :course-editor-v2-scene [editor-panel-v2-scene (:id route-params) (:scene-id route-params)]
    :course-editor-v2-concept [editor-panel-v2-concept (:course-id route-params) (-> route-params :concept-id js/parseInt)]
    :course-editor-v2-add-concept [editor-panel-v2-add-concept (:course-id route-params)]
    :course-editor-v2-lesson [editor-panel-v2-lesson (:course-id route-params) (-> route-params :level-id js/parseInt) (-> route-params :lesson-id js/parseInt)]
    :course-editor-v2-add-lesson [editor-panel-v2-add-lesson (:course-id route-params) (-> route-params :level-id js/parseInt)]

    ;; teacher dashboard
    :dashboard [dashboard-panel :dashboard route-params]
    :dashboard-classes [dashboard-panel :classes-list route-params]
    :dashboard-class-profile [dashboard-panel :class-profile route-params]
    :dashboard-students [dashboard-panel :students-list route-params]
    :dashboard-student-profile [dashboard-panel :student-profile route-params]
    :dashboard-schools [dashboard-panel :schools-list route-params]

    ;; student dashboard
    :student-login [student-access-form]
    :student-dashboard [student-dashboard-page]
    :student-course-dashboard [student-dashboard-page]
    :finished-activities [student-dashboard-finished-page]

    ;;wizard
    :create-course [wizard/create-course-panel]
    :create-activity [wizard/create-activity-panel (:course-id route-params)]
    [page-404]))

(defn main-panel []
  (let [{:keys [handler route-params]} @(re-frame/subscribe [::subs/active-route])]
    [:div
     [ui/css-baseline]
     [panels handler route-params]
     [error-message]]))
