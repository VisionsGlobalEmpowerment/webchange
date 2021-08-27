(ns webchange.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.interpreter.components :refer [course]]
    [webchange.editor.events :as ee]
    [webchange.editor-v2.course-table.views :refer [course-table]]
    [webchange.editor-v2.scenes-crossing.views :refer [scenes-crossing]]
    [webchange.editor-v2.events :as ee2]
    [webchange.editor-v2.views :refer [course-view scene-view concept-view add-concept-view lesson-view add-lesson-view]]
    [webchange.auth.views :refer [teacher-login student-access-form]]
    [webchange.dashboard.events :as dashboard-events]
    [webchange.dashboard.views :refer [dashboard]]
    [webchange.game-changer.views :as game-changer]
    [webchange.student-dashboard.views :refer [student-dashboard-page student-dashboard-finished-page]]
    [webchange.error-pages.page-404 :refer [page-404]]
    [webchange.views-login-switch :refer [login-switch]]
    [webchange.editor-v2.wizard.views :as wizard]
    [webchange.error-message.views :refer [error-message]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.ui-framework.test-page.index :refer [test-ui]]))

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

(defn- panels
  [panel-name route-params]
  (case panel-name
    :home [login-switch]
    :login [teacher-login :sign-in]
    :register-user [teacher-login :sign-up]
    :course [course {:mode ::modes/game}]
    ;; sandbox
    :sandbox [course {:mode ::modes/sandbox}]
    ;; editor
    :course-editor-v2 [editor-panel-v2 (:id route-params)]
    :course-editor-v2-scene [editor-panel-v2-scene (:id route-params) (:scene-id route-params)]
    :course-editor-v2-concept [editor-panel-v2-concept (:course-id route-params) (-> route-params :concept-id js/parseInt)]
    :course-editor-v2-add-concept [editor-panel-v2-add-concept (:course-id route-params)]
    :course-editor-v2-lesson [editor-panel-v2-lesson (:course-id route-params) (-> route-params :level-id js/parseInt) (-> route-params :lesson-id js/parseInt)]
    :course-editor-v2-add-lesson [editor-panel-v2-add-lesson (:course-id route-params) (-> route-params :level-id js/parseInt)]
    :course-table [course-table {:course-id (:course-id route-params)}]
    :scenes-crossing [scenes-crossing {:course-id (:course-id route-params)}]

    ;; teacher dashboard
    :dashboard [dashboard-panel :dashboard route-params]
    :dashboard-classes [dashboard-panel :classes-list route-params]
    :dashboard-class-profile [dashboard-panel :class-profile route-params]
    :dashboard-students [dashboard-panel :students-list route-params]
    :dashboard-student-profile [dashboard-panel :student-profile route-params]
    :dashboard-schools [dashboard-panel :schools-list route-params]

    ;; admin dashboard
    :dashboard-courses [dashboard-panel :courses-list route-params]

    ;; student dashboard
    :student-login [student-access-form]
    :student-dashboard [student-dashboard-page]
    :student-course-dashboard [student-dashboard-page]
    :finished-activities [student-dashboard-finished-page]

    ;;wizard
    :book-creator [wizard/book-creator-panel]
    :wizard [wizard/wizard]
    :wizard-configured [wizard/wizard-configured (:course-slug route-params) (:scene-slug route-params)]
    :game-changer [game-changer/index]

    ;; technical
    :test-ui [test-ui]

    nil [:div]
    [page-404]))

(defn main-panel []
  (let [{:keys [handler route-params]} @(re-frame/subscribe [::subs/active-route])]
    [:div
     [ui/css-baseline]
     [panels handler route-params]
     [error-message]]))
