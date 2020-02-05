(ns webchange.student-dashboard.sync.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.service-worker.subs :as subs]
    [webchange.student-dashboard.sync.events :as events]
    [webchange.student-dashboard.sync.views-sync-list :refer [sync-list-modal]]))

(defn sync-status
  []
  (let [offline-mode @(re-frame/subscribe [::subs/offline-mode])]
    (case offline-mode
      :not-started [ic/cloud-off {:color "disabled"}]
      :in-progress [ic/cloud-download {:color "disabled"}]
      [ic/cloud-done])))

(defn sync-menu
  []
  (r/with-let [menu-anchor (r/atom nil)]
              (let [handle-select-resources-click #(do (reset! menu-anchor nil)
                                                       (re-frame/dispatch [::events/open-sync-list]))]
                [:div
                 [sync-list-modal]
                 [ui/icon-button
                  {:on-click #(reset! menu-anchor (.-currentTarget %))}
                  [sync-status]]
                 [ui/menu
                  {:anchor-el @menu-anchor
                   :open      (boolean @menu-anchor)
                   :on-close  #(reset! menu-anchor nil)}
                  [ui/menu-item
                   {:on-click handle-select-resources-click}
                   "Select Resources"]]])))

(defn sync-control
  []
  [sync-menu])
