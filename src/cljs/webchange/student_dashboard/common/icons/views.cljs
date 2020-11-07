(ns webchange.student-dashboard.common.icons.views
  (:require
    [webchange.student-dashboard.common.icons.icon-history :as history]
    [webchange.student-dashboard.common.icons.icon-mark-done :as mark-done]
    [webchange.student-dashboard.common.icons.icon-play :as play]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn get-default-params
  []
  {:color (get-in-theme [:palette :text :primary])
   :style {:display "flex"}})

(defn- icon-wrapper
  [icon {:keys [color style]}]
  (let [default-params (get-default-params)]
    [:div {:style (merge (or (:style default-params) {}) style)}
     (icon {:color (or color (:color default-params))})]))

(defn history
  [params]
  [icon-wrapper history/icon params])

(defn mark-done
  [params]
  [icon-wrapper mark-done/icon params])

(defn play
  [params]
  [icon-wrapper play/icon params])
