(ns webchange.admin.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.index :refer [pages]]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as state]
    [webchange.admin.widgets.confirm.views :refer [block-confirm]]
    [webchange.admin.widgets.layout.views :refer [layout]]
    [webchange.login.check-current-user :as current-user]
    [webchange.ui.index :as ui]))

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
         (cond
           (= handler :login) [page-component props]
           (= handler :update-status) [page-component props]
           authenticating? [ui/loading-overlay]
           :else [layout {:no-padding? (= handler :lesson-builder)}
                  [page-component props]
                  [block-confirm]])]))}))
