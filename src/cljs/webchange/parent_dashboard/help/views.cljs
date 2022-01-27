(ns webchange.parent-dashboard.help.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.parent-dashboard.help.state :as state]
    [webchange.parent-dashboard.help.views-about :refer [about]]
    [webchange.parent-dashboard.help.views-general :refer [general]]
    [webchange.parent-dashboard.help.views-how-do :refer [how-do]]
    [webchange.parent-dashboard.layout.views :refer [layout]]
    [webchange.parent-dashboard.ui.index :refer [button]]))

(defn- help-form
  []
  [:div.help-form
   [about]
   [how-do]
   [general]])

(defn help-page
  []
  (let [handle-back-click #(re-frame/dispatch [::state/open-dashboard])]
    [layout {:title   "Help"
             :actions [[button {:on-click handle-back-click
                                :variant  "text"}
                        "< Back"]]}
     [help-form]]))
