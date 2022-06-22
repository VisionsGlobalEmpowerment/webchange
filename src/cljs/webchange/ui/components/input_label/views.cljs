(ns webchange.ui.components.input-label.views
  (:require
    [reagent.core :as r]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn input-label
  [{:keys [class-name for required?]}]
  (->> (r/current-component)
       (r/children)
       (into [:label {:for        for
                      :class-name (get-class-name {"bbs--input-label"           true
                                                   "bbs--input-label--required" required?
                                                   class-name                   (some? class-name)})}])))
