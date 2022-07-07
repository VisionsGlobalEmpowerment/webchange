(ns webchange.lesson-builder.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.widgets.layout.views :refer [layout]]
    [webchange.lesson-builder.pages.index :refer [pages]]
    [webchange.lesson-builder.routes :as routes]
    [webchange.lesson-builder.state :as state]))

(defn index
  []
  (r/create-class
   {:display-name "Lesson Builder Index"
    :component-did-mount
    (fn [this]
      (let [{:keys [route]} (r/props this)]
        (js/console.log "index in lesson builder")
        (routes/init! (:path route))))

    :reagent-render
    (fn []
      (let [{:keys [handler props]} @(re-frame/subscribe [::state/current-page])
            page-component (get pages handler (:404 pages))]
        [:div#lesson-builder
         [layout 
          [page-component props]]]))}))
