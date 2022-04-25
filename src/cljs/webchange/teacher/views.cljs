(ns webchange.teacher.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.teacher.pages.index :refer [pages]]
    [webchange.teacher.routes :as routes]
    [webchange.teacher.state :as state]))

(defn index
  []
  (r/create-class
    {:display-name "Teacher App Index"
     :component-did-mount
     (fn [this]
       (let [{:keys [route]} (r/props this)]
         (routes/init! (:path route))))

     :reagent-render
     (fn []
       (let [{:keys [handler props]} @(re-frame/subscribe [::state/current-page])
             page-component (get pages handler (:404 pages))]
         [:div#tabschool-teacher
          [page-component props]]))}))
