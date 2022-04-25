(ns webchange.admin.widgets.layout.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as parent-state]))

(re-frame/reg-sub
  ::top-bar-items
  (fn []
    [(re-frame/subscribe [::parent-state/current-page])])
  (fn [[current-page]]
    (->> [{:id    :dashboard
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
          {:id   :libraries
           :text "Libraries"
           :icon "book-library"}
          {:id   :accounts
           :text "Accounts"
           :icon "user"}]
         (map (fn [{:keys [route] :as data}]
                (cond-> data
                        (= (:handler current-page) (:page route)) (assoc :active? true)))))))

(re-frame/reg-event-fx
  ::open-page
  (fn [{:keys []} [_ {:keys [page]}]]
    (let [redirect-params (cond-> []
                                  (some? page) (conj page))]
      (when-not (empty? redirect-params)
        {:dispatch (-> [::routes/redirect] (concat redirect-params) (vec))}))))
