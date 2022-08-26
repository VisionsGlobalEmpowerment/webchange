(ns webchange.utils.links
  (:require
    [re-frame.core :as re-frame]))

(defn activity-preview
  [{:keys [activity-id]}]
  (str js/location.protocol "//" js/location.host "/s/" activity-id))

(re-frame/reg-fx
  ::open-new-tab
  (fn [link]
    (js/window.open link "_blank")))