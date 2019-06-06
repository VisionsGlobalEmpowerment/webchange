(ns webchange.dashboard.classes.views-list-menu
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.dashboard.classes.events :as classes-events]
    [webchange.dashboard.classes.subs :as classes-subs]))

(defn- classes-list-menu-item
  [{:keys [name]}]
  [ui/list-item {:primary-text   name
                 :inset-children true}])

(defn classes-list-menu
  []
  (r/with-let [_ (re-frame/dispatch [::classes-events/load-classes])]
              (let [classes @(re-frame/subscribe [::classes-subs/classes-list])]
                [ui/list
                 [ui/list-item {:primary-text "Classes"
                                :on-click     #(re-frame/dispatch [::classes-events/show-manage-classes])}]
                 (for [class classes]
                   ^{:key (:id class)}
                   [classes-list-menu-item class])
                 [ui/list-item {:primary-text "Add"
                                :left-icon    (ic/content-add)}]])))
