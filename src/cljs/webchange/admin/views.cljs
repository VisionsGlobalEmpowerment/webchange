(ns webchange.admin.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.header.views :refer [header]]
    [webchange.admin.pages.index :refer [pages]]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as state]
    ;[webchange.admin-dashboard.index :refer [layout]]
    ))

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
         #_(routes/init! (:path route))))

     :reagent-render
     (fn []
       (let [{:keys [handler props]} @(re-frame/subscribe [::state/current-page])
             page-component (get pages handler (:404 pages))]
         #_[layout {:title  "Admin App"
                    :header [header]}
            [page-component props]]
         [page-component props]
         ))}))
