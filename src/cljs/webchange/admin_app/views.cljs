(ns webchange.admin-app.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin-app.dashboard.views :as dashboard]
    [webchange.admin-app.header.views :refer [header]]
    [webchange.admin-app.routes :as routes]
    [webchange.admin-app.schools.views :as schools]
    [webchange.admin-app.school-profile.views :as school-profile]
    [webchange.admin-app.state :as state]
    [webchange.admin-dashboard.index :refer [layout]]))

(def pages {:home           dashboard/page
            :schools        schools/page
            :school-profile school-profile/page})

(defn page-not-fount
  []
  [:div "Page view is not defined"])

(defn index
  []
  (r/create-class
    {:display-name "Admin App Index"
     :component-did-mount
     (fn [this]
       (let [{:keys [route]} (r/props this)]
         (routes/init! (:path route))))

     :reagent-render
     (fn []
       (let [{:keys [handler props]} @(re-frame/subscribe [::state/current-page])
             page-component (get pages handler page-not-fount)]
         [layout {:title  "Admin App"
                  :header [header]}
          [page-component props]]))}))
