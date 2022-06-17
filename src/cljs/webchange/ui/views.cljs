(ns webchange.ui.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.ui.pages.index :refer [pages]]
    [webchange.ui.pages.dashboard.views :as dashboard]
    [webchange.ui.routes :as routes]
    [webchange.ui.state :as state]))

(defn index
  []
  (r/create-class
    {:display-name "UI Index"

     :component-did-mount
     (fn [this]
       (let [{:keys [route]} (r/props this)]
         (routes/init! (:path route))))

     :reagent-render
     (fn []
       (let [{:keys [handler props] :as page-params} @(re-frame/subscribe [::state/current-page])
             page-component (if (= handler :dashboard)
                              dashboard/page
                              (get pages handler (:404 pages)))]
         (routes/set-title! page-params)
         [:div#tabschool-ui
          [page-component props]]))}))
