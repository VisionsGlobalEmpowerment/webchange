(ns webchange.student-dashboard.toolbar.sync.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.sw-utils.state.status :as status]
    [webchange.student-dashboard.toolbar.sync.state.sync-list :as sync-list]
    [webchange.student-dashboard.toolbar.sync.views-sync-list :refer [sync-list-modal]]
    [webchange.student-dashboard.toolbar.sync.icons.icon-ready :refer [icon-ready]]
    [webchange.student-dashboard.toolbar.sync.icons.icon-syncing :refer [icon-syncing]]
    [webchange.student-dashboard.toolbar.sync.icons.icon-unavailable :refer [icon-unavailable]]))

(defn current-version-data
  [{:keys [update-date-str version app-version]}]
  (let [update (js/Date. update-date-str)
        update-date (.toLocaleDateString update)
        update-time (.toLocaleTimeString update)]
    [:div
     [:div {:style {:display   "flex"
                    :font-size 10
                    :height    15}}
      [:div {:style {:width 60}} "App Version:"]
      [:div
       [:span {:style {:height 14}} app-version]]]
     [:div {:style {:display   "flex"
                    :font-size 10
                    :height    15}}
      [:div {:style {:width 60}} "SW Version:"]
      [:div
       [:span {:style {:height 14}} version]]]
     [:div {:style {:display   "flex"
                    :font-size 10}}
      [:div {:style {:width 60}} "Updated:"]
      [:div {:style {:display        "flex"
                     :flex-direction "column"}}
       [:span {:style {:height 14}} update-time]
       [:span {:style {:height 14}} update-date]]]]))

(defn current-version
  []
  (let [last-update @(re-frame/subscribe [::status/last-update])
        version @(re-frame/subscribe [::status/version])
        app-version @(re-frame/subscribe [:app-version])]
    [ui/menu-item
     {:disabled true
      :style    {:height          70
                 :justify-content "center"
                 :padding-top     0
                 :padding-bottom  0}}
     (if (nil? last-update)
       [ui/circular-progress
        {:size 24}]
       [current-version-data {:update-date-str last-update
                              :version         version
                              :app-version     app-version}])]))

(defn- sync-status-icon
  [sync-status]
  (case sync-status
    ;; preparing
    :installing [icon-syncing]
    :installed [icon-syncing]
    :activating [icon-syncing]
    :syncing [icon-syncing]
    ;; ready to work
    :activated [icon-ready]
    :synced [icon-ready]
    :offline [icon-ready {:color "#278600"}]
    ;; not available
    :disabled [icon-unavailable {:color "#cccccc"}]
    :broken [icon-unavailable {:color "#ff2121"}]
    [icon-unavailable {:color "#ff9f21"}]))

(defn sync
  []
  (r/with-let [menu-anchor (r/atom nil)]
              (let [disabled? @(re-frame/subscribe [::status/sync-disabled?])
                    sync-status @(re-frame/subscribe [::status/sync-status])
                    handle-select-resources-click #(do (reset! menu-anchor nil)
                                                       (re-frame/dispatch [::sync-list/open]))]
                [:div
                 [ui/tooltip {:title (name sync-status)}
                  [ui/icon-button
                   {:disabled disabled?
                    :on-click #(reset! menu-anchor (.-currentTarget %))}
                   [sync-status-icon sync-status]]]
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
