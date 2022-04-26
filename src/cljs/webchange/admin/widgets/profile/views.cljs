(ns webchange.admin.widgets.profile.views
  (:require
    [reagent.core :as r]))

(defn page
  []
  (->> (r/current-component)
       (r/children)
       (into [:div.widget-profile])))

(defn main-content
  [{:keys [title]}]
  (->> (r/current-component)
       (r/children)
       (into [:div.widget-profile--main-content
              [:h1 title]])))

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
