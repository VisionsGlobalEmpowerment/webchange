(ns webchange.student-dashboard.toolbar.sync.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.sw-utils.state.status :as status]
    [webchange.student-dashboard.toolbar.sync.sync-list.state.window :as window]
    [webchange.student-dashboard.toolbar.sync.sync-list.views-sync-window :refer [sync-list-modal]]
    [webchange.student-dashboard.toolbar.sync.icons.icon-ready :refer [icon-ready]]
    [webchange.student-dashboard.toolbar.sync.icons.icon-syncing :refer [icon-syncing]]
    [webchange.student-dashboard.toolbar.sync.icons.icon-unavailable :refer [icon-unavailable]]
    [webchange.sw-utils.state.resources :as sw]))

(defn- get-styles
  []
  {:progress-container {:width           117
                        :height          62
                        :justify-content "center"
                        :padding-top     0
                        :padding-bottom  0}
   :progress-value     {:position    "absolute"
                        :top         "50%"
                        :left        "50%"
                        :margin-top  "-14px"
                        :margin-left "-11px"
                        :font-size   "12px"
                        :display     "block"
                        :width       "22px"
                        :text-align  "center"}})

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

(defn- progress-bar
  []
  (let [progress (->> @(re-frame/subscribe [::status/caching-progress])
                      (* 100)
                      (Math/round))
        params {:size      36
                :thickness 5}
        styles (get-styles)]
    [ui/menu-item
     {:disabled true
      :style    (:progress-container styles)}
     (if (some? progress)
       [:div
        [ui/circular-progress
         (merge params
                {:variant "determinate"
                 :value   progress})]
        [:span {:style (:progress-value styles)} progress]]
       [ui/circular-progress
        params])]))

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
     [current-version-data {:update-date-str last-update
                            :version         version
                            :app-version     app-version}]]))

(defn- sync-status-button
  [{:keys [menu-anchor status cache-empty? disabled?]}]
  (r/with-let [tooltip-touched? (r/atom false)]
              [ui/tooltip
               {:title     (r/as-element [:span {:style {:color "#ff9f21"}} "No lesson selected"])
                :open      (and cache-empty?
                                (= status :synced)
                                (not @tooltip-touched?))
                :placement "bottom-start"
                :onOpen    #(reset! tooltip-touched? true)}
               [ui/icon-button
                {:disabled disabled?
                 :on-click #(reset! menu-anchor (.-currentTarget %))}
                (case status
                  ;; preparing
                  :installing [icon-syncing]
                  :installed [icon-syncing]
                  :activating [icon-syncing]
                  :syncing [icon-syncing]
                  ;; ready to work
                  :activated [icon-ready]
                  :synced [icon-ready {:color (if cache-empty? "#ff9f21" "#1373e6")}]
                  :offline [icon-ready {:color "#278600"}]
                  ;; not available
                  :disabled [icon-unavailable {:color "#cccccc"}]
                  :broken [icon-unavailable {:color "#ff2121"}]
                  [icon-unavailable {:color "#cccccc"}])]]))

(defn sync
  []
  (r/with-let [menu-anchor (r/atom nil)]
              (let [disabled? @(re-frame/subscribe [::status/sync-disabled?])
                    sync-status @(re-frame/subscribe [::status/sync-status])
                    cached-resources @(re-frame/subscribe [::sw/cached-resources])
                    cache-empty? (or (empty? (:resources cached-resources))
                                     (empty? (:endpoints cached-resources)))
                    handle-select-resources-click #(do (reset! menu-anchor nil)
                                                       (re-frame/dispatch [::window/open]))]
                [:div
                 [sync-status-button {:menu-anchor  menu-anchor
                                      :status       sync-status
                                      :cache-empty? cache-empty?
                                      :disabled?    disabled?}]
                 [ui/menu
                  {:anchor-el               @menu-anchor
                   :open                    (boolean @menu-anchor)
                   :disable-auto-focus-item true
                   :on-close                #(reset! menu-anchor nil)}
                  (if (= sync-status :syncing)
                    [progress-bar]
                    [:div
                     [ui/menu-item
                      {:on-click handle-select-resources-click}
                      "Select Resources"
                      (when cache-empty?
                        [ic/report {:style {:color       "#ff9f21"
                                            :margin-left "10px"
                                            :margin-top  "2px"
                                            :font-size   "20px"}}])]
                     [ui/divider]
                     [current-version]])]
                 [sync-list-modal]])))
