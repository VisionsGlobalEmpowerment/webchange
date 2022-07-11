(ns webchange.parent.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.parent.pages.index :refer [pages]]
    [webchange.parent.routes :as routes]
    [webchange.parent.state :as state]
    [webchange.parent.widgets.layout.views :refer [layout]]))

(defn index
  []
  (r/create-class
    {:display-name "Parent App Index"
     :component-did-mount
     (fn [this]
       (let [{:keys [route]} (r/props this)]
         (routes/init! (:path route))))

     :reagent-render
     (fn []
       (let [{:keys [handler props] :as page-params} @(re-frame/subscribe [::state/current-page])
             page-component (get pages handler (:404 pages))]
         (routes/set-title! page-params)
         [:div#tabschool-parent
          [layout
           [page-component props]]]))}))
