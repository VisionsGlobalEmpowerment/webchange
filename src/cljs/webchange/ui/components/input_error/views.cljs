(ns webchange.ui.components.input-error.views
  (:require
    [reagent.core :as r]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn input-error
  [{:keys [class-name for]}]
  (->> (r/current-component)
       (r/children)
       (into [:label (cond-> {:class-name (get-class-name {"bbs--input-error" true
                                                           class-name         (some? class-name)})}
                             (some? for) (assoc :for for))])))
