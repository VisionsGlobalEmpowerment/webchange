(ns webchange.admin.widgets.navigation.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as parent-state]
    [webchange.admin.widgets.navigation.utils :refer [set-navigation-items-active
                                                      hide-navigation-items-by-user-type]]
    [webchange.utils.map :refer [map->list]]))

(re-frame/reg-sub
  ::navigation-items
  (fn []
    [(re-frame/subscribe [::parent-state/current-page])
     (re-frame/subscribe [:current-user])])
  (fn [[current-page current-user]]
    (-> [{:id    :dashboard
          :text  "Dashboard"
          :icon  "dashboard"
          :route {:page :dashboard}}
         {:id    :school-management
          :text  "School Management"
          :icon  "school"
          :route {:page :schools}}
         {:id   :lesson-builder
          :text "Create"
          :icon "create"
          :route {:page :create}}
         {:id       :library
          :text     "Library"
          :icon     "library"
          :children [{:id    :courses
                      :text  "Courses"
                      :route {:page :courses}}
                     {:id    :activities
                      :text  "Activities"
                      :route {:page :activities}}
                     {:id    :books
                      :text  "Books"
                      :route {:page :books}}]}
         {:id       :accounts
          :text     "Accounts"
          :icon     "accounts"
          :children [{:id    :admin
                      :text  "Admin"
                      :route {:page        :accounts
                              :page-params {:account-type "admin"}}
                      :visible-for ["admin"]}
                     {:id    :live
                      :text  "Live Users"
                      :route {:page        :accounts
                              :page-params {:account-type "live"}}}]}]
        (set-navigation-items-active {:page (:handler current-page)})
        (hide-navigation-items-by-user-type current-user))))

(re-frame/reg-event-fx
  ::open-page
  (fn [{:keys []} [_ {:keys [page page-params] :as route}]]
    (when (some? route)
      (let [redirect-params (cond-> []
                                    (some? page) (conj page)
                                    (some? page-params) (concat (map->list page-params)))]
        (when-not (empty? redirect-params)
          {:dispatch (-> [::routes/redirect] (concat redirect-params) (vec))})))))
