(ns webchange.ui.components.panel.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.available-values :as available-values]
    [webchange.ui.components.icon.views :refer [system-icon]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn panel
  [{:keys [class-name class-name-content color title icon]}]
  {:pre [(or (nil? class-name) (string? class-name))
         (or (nil? class-name-content) (string? class-name-content))
         (or (nil? color) (some #{color} available-values/color))
         (or (nil? title) (string? title))
         (or (nil? icon)
             (some #{icon} available-values/icon-system))]}
  (let [show-title? (or (some? title)
                        (some? icon))]
    [:div {:class-name (get-class-name {"bbs--panel"                     true
                                        (str "bbs--panel--color-" color) (some? color)
                                        class-name                       (some? class-name)})}
     (when show-title?
       [:h3.bbs--panel--title
        (when (some? icon)
          [system-icon {:icon       icon
                        :class-name "bbs--panel--title-icon"}])
        title])
     (->> (r/current-component)
          (r/children)
          (into [:div {:class-name (get-class-name {"bbs--panel--content" true
                                                    class-name-content    (some? class-name-content)})}]))]))
