(ns webchange.admin.pages.add-class.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [_]} [_ {:keys [school-id]}]]
    {}))
