(ns webchange.student-dashboard.toolbar.sync.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.sw-utils.message :as sw]
    [webchange.sw-utils.state.status :as status]
    [webchange.student-dashboard.toolbar.sync.state.sync-list :as sync-list]
    [webchange.student-dashboard.toolbar.sync.views-sync-list :refer [sync-list-modal]]
    [webchange.student-dashboard.toolbar.sync.icons.icon-ready :as icon-ready]
    [webchange.student-dashboard.toolbar.sync.icons.icon-syncing :as icon-syncing]
    [webchange.student-dashboard.toolbar.sync.icons.icon-unavailable :as icon-unavailable]))

(defn- sync-status
  []
  (let [sync-status @(re-frame/subscribe [::status/sync-status])]
    (case sync-status
      :installing [icon-syncing/get-shape]
      :syncing [icon-syncing/get-shape]
      :synced [icon-ready/get-shape]
      :disabled [icon-unavailable/get-shape {:color "#cccccc"}]
      :offline [icon-ready/get-shape {:color "#278600"}]
      [icon-unavailable/get-shape])))

(defn current-version-data
  [{:keys [update-date-str version]}]
  (let [update (js/Date. update-date-str)
        update-date (.toLocaleDateString update)
        update-time (.toLocaleTimeString update)]
    [:div
     [:div {:style {:display   "flex"
                    :font-size 10
                    :height    15}}
      [:div {:style {:width 50}} "Version:"]
      [:div
       [:span {:style {:height 14}} version]]]
     [:div {:style {:display   "flex"
                    :font-size 10}}
      [:div {:style {:width 50}} "Updated:"]
      [:div {:style {:display        "flex"
                     :flex-direction "column"}}
       [:span {:style {:height 14}} update-time]
       [:span {:style {:height 14}} update-date]]]]))

(defn current-version
  []
  (sw/get-last-update)
  (let [last-update @(re-frame/subscribe [::status/last-update])
        version @(re-frame/subscribe [::status/version])]
    [ui/menu-item
     {:disabled true
      :style    {:height          50
                 :justify-content "center"
                 :padding-top     0
                 :padding-bottom  0}}
     (if (nil? last-update)
       [ui/circular-progress
        {:size 24}]
       [current-version-data {:update-date-str last-update
                              :version         version}])]))

(defn sync
  []
  (r/with-let [menu-anchor (r/atom nil)]
              (let [disabled? @(re-frame/subscribe [::status/sync-disabled?])
                    handle-select-resources-click #(do (reset! menu-anchor nil)
                                                       (re-frame/dispatch [::sync-list/open]))]
                [:div
                 [ui/icon-button
                  {:disabled disabled?
                   :on-click #(reset! menu-anchor (.-currentTarget %))}
                  [sync-status]]
                 [ui/menu
                  {:anchor-el               @menu-anchor
                   :open                    (boolean @menu-anchor)
                   :disable-auto-focus-item true
                   :on-close                #(reset! menu-anchor nil)}
                  [ui/menu-item
                   {:on-click handle-select-resources-click}
                   "Select Resources"]
                  [ui/divider]
                  [current-version]]
                 [sync-list-modal]])))
