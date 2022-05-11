(ns webchange.admin.widgets.layout.navigation.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.routes :as routes]
    [webchange.admin.state :as parent-state]
    [webchange.admin.widgets.layout.navigation.utils :refer [set-navigation-items-active]]
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
         {:id       :school-management
          :text     "School Management"
          :icon     "preview"
          :children [{:id       :schools
                      :text     "Schools"
                      :route    {:page :schools}
                      :children [{:id       :school-1
                                  :text     "School 1"
                                  :route    {:page        :school-profile
                                             :page-params {:school-id 1}}
                                  :children [{:id       :classes
                                              :text     "Classes"
                                              :route    {:page        :classes
                                                         :page-params {:school-id 1}}
                                              :children [{:id    :class-3
                                                          :text  "Class 3"
                                                          :route {:page        :class-profile
                                                                  :page-params {:school-id 1
                                                                                :class-id  3}}}]}
                                             {:id       :students
                                              :text     "Students"
                                              :route    {:page        :students
                                                         :page-params {:school-id 1}}
                                              :children [{:id    :add
                                                          :text  "Add Student"
                                                          :route {:page        :add-student
                                                                  :page-params {:school-id 1}}}]}
                                             {:id       :courses
                                              :text     "Courses"
                                              :route    {:page        :school-courses
                                                         :page-params {:school-id 1}}}]}]}
                     {:id    :courses
                      :text  "Courses"
                      :route {:page :courses}}]}
         {:id   :lesson-builder
          :text "Lesson builder"
          :icon "create-game"}
         {:id   :libraries
          :text "Libraries"
          :icon "book-library"}
         {:id   :accounts
          :text "Accounts"
          :icon "user"}]
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
