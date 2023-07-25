(ns webchange.views
  (:require
    [re-frame.core :as re-frame]
    ["react" :as react]
    [reagent.core :as r]
    [webchange.book-library.views :as book-library]
    [webchange.subs :as subs]
    [webchange.interpreter.components :refer [course]]
    [webchange.student-dashboard.views :as student-dashboard]
    [webchange.error-pages.page-404 :refer [page-404]]
    [webchange.error-message.views :refer [error-message]]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]
    [webchange.ui.index :as ui]
    [webchange.utils.lazy-component :refer [lazy-component]]
    [webchange.sandbox.views :as sandbox]
    [webchange.student.pages.sign-in.views :as student-sign-in]
    [webchange.student.pages.select-school.views :as select-school]))

(def overall-views
  (merge book-library/views
         student-dashboard/views))

(def modules {:admin   (-> webchange.admin.views/index (shadow.lazy/loadable) (lazy-component))
              :login   (-> webchange.login.views/index (shadow.lazy/loadable) (lazy-component))
              :parent  (-> webchange.parent.views/index (shadow.lazy/loadable) (lazy-component))
              :teacher (-> webchange.teacher.views/index (shadow.lazy/loadable) (lazy-component))
              :ui      (-> webchange.ui.views/index (shadow.lazy/loadable) (lazy-component))})

(defn module-route?
  [route-name]
  (contains? modules route-name))

(defn- module
  [{:keys [component url]}]
  [:> react/Suspense {:fallback (r/as-element [ui/loading-overlay])}
   [:> component {:route {:path url}}]])

(defn- panels
  [route-name route-params url]
  (cond
    (module-route? route-name) [module {:url       url
                                        :component (get modules route-name)}]
    (contains? overall-views route-name) [(get overall-views route-name) route-params]
    :else (case route-name
            :course [course {:mode ::modes/game}]
            ;; sandbox
            :activity-sandbox [sandbox/activity {:scene-id (:scene-id route-params)}]
            ;; student dashboard
            :student-login [select-school/page]
            :school-student-login [student-sign-in/page {:school-id (:school-id route-params)}]

            nil [:div]
            [page-404])))

(defn main-panel []
  (let [{:keys [handler url route-params]} @(re-frame/subscribe [::subs/active-route])
        school-id @(re-frame/subscribe [::subs/school-id])
        route-params (update route-params :school-id #(or % school-id))]
    [:div {:class-name (when-not (module-route? handler) "main-app")}
     [panels handler route-params url]
     [error-message]]))
