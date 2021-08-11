(ns webchange.ui-framework.layout.user-widget.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.auth.subs :as as]
    [webchange.ui-framework.layout.avatar.views :refer [avatar]]))

(defn user-widget
  []
  (let [current-user @(re-frame/subscribe [::as/user])]
    [:div.icon-bottom
     [:span.avatar-icn
      [avatar]]
     [:span.user-name
      (str (:first-name current-user) " " (:last-name current-user))]]))
