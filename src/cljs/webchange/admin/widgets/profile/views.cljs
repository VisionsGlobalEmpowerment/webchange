(ns webchange.admin.widgets.profile.views
  (:require
    [reagent.core :as r]))

(defn page
  []
  (->> (r/current-component)
       (r/children)
       (into [:div.widget-profile])))

(defn main-content
  [{:keys [footer title]}]
  [:div.widget-profile--main-content
   [:h1 title]
   (->> (r/current-component)
        (r/children)
        (into [:div.widget-profile--main-content--content]))
   [:div.widget-profile--main-content--footer
    footer]])

(defn side-bar
  []
  (->> (r/current-component)
       (r/children)
       (into [:div.widget-profile--side-bar])))

(defn block
  [{:keys [title]}]
  (->> (r/current-component)
       (r/children)
       (into [:div.widget-profile--content-block
              [:h2 title]])))
