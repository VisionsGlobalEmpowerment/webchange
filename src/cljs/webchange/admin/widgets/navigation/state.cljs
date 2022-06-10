(ns webchange.admin.widgets.navigation.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as parent-state]
    [webchange.admin.widgets.navigation.utils :refer [set-navigation-items-active]]
    [webchange.utils.map :refer [map->list]]))

(re-frame/reg-sub
  ::navigation-items
  (fn []
    [(re-frame/subscribe [::parent-state/current-page])])
  (fn [[current-page]]
    (-> [{:id    :dashboard
          :text  "Dashboard"
          :icon  "dashboard"
          :route {:page :dashboard}}
         {:id    :school-management
          :text  "School Management"
          :icon  "preview"
          :route {:page :schools}}
         {:id   :lesson-builder
          :text "Lesson builder"
          :icon "create-game"}
         {:id       :library
          :text     "Library"
          :icon     "book-library"
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
          :icon     "users"
          :children [{:id    :admin
                      :text  "Admin"
                      :route {:page        :accounts
                              :page-params {:account-type "admin"}}}
                     {:id    :live
                      :text  "Live Users"
                      :route {:page        :accounts
                              :page-params {:account-type "live"}}}]}]
        (set-navigation-items-active {:page (:handler current-page)}))))

(re-frame/reg-event-fx
  ::open-page
  (fn [{:keys []} [_ {:keys [page page-params] :as route}]]
    (when (some? route)
      (let [redirect-params (cond-> []
                                    (some? page) (conj page)
                                    (some? page-params) (concat (map->list page-params)))]
        (when-not (empty? redirect-params)
          {:dispatch (-> [::routes/redirect] (concat redirect-params) (vec))})))))
