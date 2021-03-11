(ns webchange.ui-framework.layout.avatar.views
  (:require
    [clojure.string :as string]
    [re-frame.core :as re-frame]
    [webchange.auth.subs :as as]
    [webchange.routes :refer [location]]))

(defn avatar
  []
  (let [user @(re-frame/subscribe [::as/user])
        initials (-> user :first-name first)]
    [:div.avatar {:on-click #(location :profile)}
     (string/upper-case initials)]))
