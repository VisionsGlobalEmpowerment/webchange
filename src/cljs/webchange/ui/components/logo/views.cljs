(ns webchange.ui.components.logo.views
  (:require
    [webchange.ui.components.logo.logo-with-name :as logo-with-name-svg]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn logo-with-name
  [{:keys [class-name]}]
  [:div {:class-name (get-class-name {"bbs--logo-with-name" true
                                      class-name            (some? class-name)})}
   logo-with-name-svg/data])
