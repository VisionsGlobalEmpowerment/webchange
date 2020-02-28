(ns webchange.student-dashboard.toolbar.auth.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.auth.subs :as as]))

(defn- get-styles
  []
  {:main      {:padding "0 20px"}
   :user-name {:font-weight "bold"}})

(defn auth
  []
  (let [current-user @(re-frame/subscribe [::as/user])
        styles (get-styles)]
    [ui/typography {:variant "h6"
                    :style   (:main styles)}
     "Hello, "
     [:span {:style (:user-name styles)}
      (str (:first-name current-user) " " (:last-name current-user))]]))
