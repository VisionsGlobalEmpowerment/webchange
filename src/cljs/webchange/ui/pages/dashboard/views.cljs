(ns webchange.ui.pages.dashboard.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.ui.pages.index :refer [pages]]
    [webchange.ui.pages.layout :refer [layout]]))

(defn- page-button
  [{:keys [route title]}]
  (let [handle-click #(re-frame/dispatch [:ui-redirect route])]
    [:button {:on-click handle-click}
     title]))

(defn page
  []
  (let [pages (->> (dissoc pages :404)
                   (map first)
                   (map (fn [page-key]
                          {:title page-key
                           :route page-key})))]
    [:div#page--dashboard
     [layout {:title      "UI"
              :show-back? false}
      [:div.pages-list
       (for [{:keys [route] :as page-data} pages]
         ^{:key route}
         [page-button page-data])]]]))
