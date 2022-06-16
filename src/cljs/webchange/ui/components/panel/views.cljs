(ns webchange.ui.components.panel.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.icon.views :refer [system-icon]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn panel
  [{:keys [class-name title icon]}]
  (let [show-title? (or (some? title)
                        (some? icon))]
    [:div {:class-name (get-class-name {"bbs--panel" true
                                        class-name   (some? class-name)})}
     (when show-title?
       [:h3.bbs--panel--title
        (when (some? icon)
          [system-icon {:icon       icon
                        :class-name "bbs--panel--title-icon"}])
        title])
     (->> (r/current-component)
          (r/children)
          (into [:div {:class-name "bbs--panel--content"}]))]))
