(ns webchange.lesson-builder.widgets.activity-title.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.state :as state]
    [webchange.utils.date :refer [date-str->locale-date date-str->time]]))

(re-frame/reg-sub
  ::activity-name
  :<- [::state/activity-info]
  (fn [activity-data]
    (get activity-data :name "No Name Activity")))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [_]} [_]]
    {:dispatch [::state/save-activity]}))

(re-frame/reg-sub
  ::saving?
  :<- [::state/activity-saving?]
  identity)

(re-frame/reg-sub
  ::last-save
  :<- [::state/activity-versions]
  (fn [versions]
    (let [{:keys [created-at]} (->> versions (sort-by :created-at) (last))]
      {:date (date-str->locale-date created-at)
       :time (date-str->time created-at)})))

(re-frame/reg-sub
  ::versions-loading?
  :<- [::state/activity-versions-loading?]
  identity)

(defn- create-link
  [{:keys [course-slug activity-slug]}]
  (str js/location.protocol "//" js/location.host "/s/" course-slug "/" activity-slug))

(re-frame/reg-event-fx
  ::preview
  (fn [{:keys [db]} [_]]
    (let [{:keys [slug course-slug]} (state/get-activity-info db state/path-to-db)
          link (create-link {:course-slug   course-slug
                             :activity-slug slug})]
      {:open-link link})))

(re-frame/reg-fx
  :open-link
  (fn [link]
    (js/window.open link "_blank")))
