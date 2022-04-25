(ns webchange.admin.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.index :refer [pages]]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as state]
    [webchange.admin.widgets.layout.views :refer [layout]]))

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
             page-component (get pages handler (:404 pages))]
         [:div#tabschool-admin
          [layout
           [page-component props]]]))}))
