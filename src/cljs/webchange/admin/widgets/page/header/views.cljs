(ns webchange.admin.widgets.page.header.views
  (:require
    [webchange.ui.components.available-values :as available-values]
    [webchange.ui.index :refer [get-class-name] :as ui]))

(defn header
  [{:keys [icon on-close title]}]
  {:pre [(or (nil? icon) (some #{icon} available-values/icon-navigation))
         (or (nil? on-close) (fn? on-close))
         (or (nil? title) (string? title))]}
  [:div.widget--page--header
   (when (some? icon)
     [ui/navigation-icon {:icon       icon
                          :class-name "widget--page--header--navigation-icon"}])
   (when (some? title)
     [:div.widget--page--header--title
      title])
   [:div.widget--page--header--filler]
   (when (fn? on-close)
     [ui/button {:icon       "close"
                 :class-name "widget--page--header--close-button"
                 :on-click   on-close}])])
