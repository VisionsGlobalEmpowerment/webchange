(ns webchange.student-dashboard-v2.toolbar.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.student-dashboard-v2.toolbar.state :as state]))

(defn toolbar-item
  [{:keys [img on-click title]}]
  [:div {:class-name "toolbar-item"
         :title      title
         :on-click   on-click}
   [:img {:src img}]])

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
     #_[:button {:on-click #(re-frame/dispatch [::state/login-as-parent])
               :style    {:z-index 1}}
      "Back to parent"]
     (for [{:keys [id] :as item} items]
       ^{:key id}
       [toolbar-item item])]))
