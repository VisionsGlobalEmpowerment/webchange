(ns webchange.game-changer.templates-list.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.game-changer.templates-list.state :as state]))

(defn- template-list-item
  [{:keys [description name preview]}]
  [:div.templates-list-item
   [:div.preview {:style {:background-image (str "url(" preview ")")}}]
   [:span.title name]
   [:div.description description]])

(defn templates-list
  []
  (r/with-let [_ (re-frame/dispatch [::state/init])]
    (let [templates @(re-frame/subscribe [::state/templates-list])]
      (print "templates" templates)
      [:div.templates-list
       (for [{:keys [id] :as template} templates]
         ^{:key id}
         [template-list-item template])])))


