(ns webchange.student-dashboard.toolbar.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.student-dashboard.toolbar.state :as state]))

(defn- toolbar-item
  [{:keys [img on-click title]}]
  [:div {:class-name "toolbar-item"
         :title      title
         :on-click   on-click}
   [:img {:src img}]])

(defn- toolbar-button
  [{:keys [on-click]}]
  [:div.toolbar-button-background
   (into [:button {:class-name "toolbar-button"
                   :on-click   on-click}]
         (-> (r/current-component)
             (r/children)))])

(defn- exit-button
  []
  (let [handle-click #(re-frame/dispatch [::state/exit])]
    [toolbar-button {:on-click handle-click}
     "Exit"]))

(defn toolbar
  []
  (let [items (->> [{:id    :awards
                     :title "Awards"
                     :img   "awards.png"}
                    {:id    :book-library
                     :title "Book Library"
                     :img   "book_library.png"}
                    {:id    :video-library
                     :title "Video Library"
                     :img   "video_library.png"}]
                   (map (fn [{:keys [id img] :as item}]
                          (-> item
                              (assoc :img (str "/images/student_dashboard/" img))
                              (assoc :on-click #(re-frame/dispatch [::state/open-page id]))))))]
    [:div.toolbar
     #_(for [{:keys [id] :as item} items]
         ^{:key id}
         [toolbar-item item])
     [exit-button]]))
