(ns webchange.student-dashboard.toolbar.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-library.routes :as book-library]
    [webchange.events :as events]
    [webchange.i18n.translate :as i18n]
    [webchange.state.warehouse :as warehouse]
    [webchange.student-dashboard.state :as parent-state]))

(re-frame/reg-sub
  ::toolbar-items
  (fn []
    (re-frame/subscribe [::i18n/t {:exit-title    [:exit]
                                   :library-title [:book-library]}]))
  (fn [{:keys [exit-title library-title]}]
    (->> [#_{:id    :awards
             :title "Awards"
             :img   "awards.png"}
          #_{:id    :video-library
             :title "Video Library"
             :img   "video_library.png"}
          {:id    :book-library
           :title library-title
           :img   "book_library.png"}
          {:id    :exit
           :title exit-title
           :img   "exit.png"}]
         (map (fn [{:keys [img] :as item}]
                (assoc item :img (str "/images/student_dashboard/" img)))))))

(re-frame/reg-event-fx
  ::handle-toolbar-item-click
  (fn [{:keys [_]} [_ item-id]]
    (case item-id
      :exit {:dispatch [::exit]}
      :book-library {:dispatch [::open-book-library]}
      {})))

(re-frame/reg-event-fx
  ::login-as-parent
  (fn [{:keys [_]} [_]]
    {:dispatch [::warehouse/login-as-student-parent {}
                {:on-success [::login-as-parent-success]}]}))

(re-frame/reg-event-fx
  ::login-as-parent-success
  (fn [{:keys [db]} [_ user]]
    {:db       (update-in db [:user] merge user)
     :dispatch [::events/redirect :parent-dashboard]}))

(re-frame/reg-event-fx
  ::exit
  (fn [{:keys [db]} [_]]
    (let [current-school (-> db :user :school-id)]
      (if current-school
        {:dispatch [::events/redirect :student-login]}
        {:dispatch [::login-as-parent]}))))

(re-frame/reg-event-fx
  ::open-book-library
  (fn [{:keys [db]} [_]]
    (let [course-id (parent-state/get-current-course db)]
      {:dispatch [::book-library/open-book-library {:course-id course-id}]})))
