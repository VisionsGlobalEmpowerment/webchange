(ns webchange.parent.widgets.layout.header.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.parent.widgets.layout.header.state :as state]
    [webchange.ui.index :as ui]))

(defn- navigation-item
  [{:keys [event text]}]
  (let [handle-click #(re-frame/dispatch event)]
    [ui/button {:class-name "header--navigation-item"
                :color      "transparent"
                :on-click   handle-click}
     text]))

(defn- navigation
  []
  (let [items [{:text  "Home"
                :event [::state/open-home-page]}
               {:text  "Help"
                :event [::state/open-faq-page]}
               {:text  "Log Out"
                :event [::state/logout]}]]
    [:div {:class-name "header--navigation"}
     (for [[idx item-data] (map-indexed vector items)]
       ^{:key idx}
       [navigation-item item-data])]))

(defn header
  []
  [:div.parent--layout--header
   [ui/logo-with-name {:class-name "header--logo"}]
   [navigation]])
