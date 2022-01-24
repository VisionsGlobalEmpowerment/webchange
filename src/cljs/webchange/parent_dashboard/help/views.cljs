(ns webchange.parent-dashboard.help.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.parent-dashboard.help.state :as state]
    [webchange.parent-dashboard.layout.views :refer [layout]]))

(defn- p
  [{:keys [title]}]
  [:div.paragraph
   (when (some? title)
     [:h3 title])
   (into [:p]
         (-> (r/current-component)
             (r/children)))])

(defn link
  [{:keys [url]}]
  (into [:a.link {:href url}]
        (-> (r/current-component)
            (r/children))))

(defn- help-form
  []
  [:div.help-form
   [p {:title "How to play"}
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."]
   [p {:title "STUCK ON A GAME"}
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "]
   [link {:url "#"} "Link to provide feedback"]])

(defn help-page
  []
  (let [handle-back-click #(re-frame/dispatch [::state/open-dashboard])]
    [layout {:title   "Help"
             :actions [[:button {:on-click handle-back-click} "Back"]]}
     [help-form]]))
