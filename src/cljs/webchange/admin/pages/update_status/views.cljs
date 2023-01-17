(ns webchange.admin.pages.update-status.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.update-status.state :as state]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [status @(re-frame/subscribe [::state/status])]
      [:div#page--update-status
       [:div.status
        (str "Update status: " status) ]])))
