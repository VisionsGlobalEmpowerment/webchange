(ns webchange.ui-framework.layout.avatar.views
  (:require
    [clojure.string :as string]
    [re-frame.core :as re-frame]
    [webchange.auth.subs :as as]
    [webchange.routes :refer [location]]))

(defn avatar
  []
  (let [user @(re-frame/subscribe [::as/user])
        initials (or (-> user :first-name first)
                     (-> user :last-name first))]
    [:div.avatar {:on-click #(location :profile)}
     (if (string? initials)
       (string/upper-case initials)
       "T")]))
