(ns webchange.admin.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.index :refer [pages]]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as state]
    [webchange.admin.widgets.layout.views :refer [layout]]
    [webchange.login.check-current-user :as current-user]
    [webchange.ui-framework.components.index :as ui]))

(defn index
  []
  (r/create-class
    {:display-name "Admin App Index"
     :component-did-mount
     (fn [this]
       (let [{:keys [route]} (r/props this)]
         (re-frame/dispatch [::current-user/check-current-user])
         (routes/init! (:path route))))

     :reagent-render
     (fn []
       (let [authenticating? @(re-frame/subscribe [::current-user/in-progress?])
             {:keys [handler props] :as page-params} @(re-frame/subscribe [::state/current-page])
             page-component (get pages handler (:404 pages))]
         (routes/set-title! page-params)
         [:div#tabschool-admin
          (if (= handler :login)
            [page-component props]
            (if authenticating?
              [ui/loading-overlay]
              [layout
               [page-component props]]))]))}))
