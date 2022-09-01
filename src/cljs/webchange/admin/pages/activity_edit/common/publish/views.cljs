(ns webchange.admin.pages.activity-edit.common.publish.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.activity-edit.common.publish.state :as state]
    [webchange.admin.pages.activity-edit.common.state :as common-state]
    [webchange.ui.index :as ui]))

(defn- control-state
  [{:keys [text icon]}]
  [:div {:class-name "control-state"}
   (when (some? text)
     [:label text])
   (when (some? icon)
     [ui/icon {:icon icon}])])

(defn- publish-control
  []
  (let [{:keys [disabled? read-only? visible?]} @(re-frame/subscribe [::state/publish-control-state])
        activity-id @(re-frame/subscribe [::common-state/activity-id])
        activity-published? @(re-frame/subscribe [::common-state/activity-published?])
        loading? @(re-frame/subscribe [::state/publish-loading?])
        label (cond
                loading? "Saving.."
                activity-published? "Global"
                :default "Not published")
        handle-change #(re-frame/dispatch [::state/set-published activity-id %])]
    (when visible?
      (if read-only?
        [control-state {:text (if activity-published? "Global" "Not published")}]
        [ui/switch {:checked?       activity-published?
                    :disabled?      disabled?
                    :indeterminate? loading?
                    :label          label
                    :on-change      handle-change
                    :color          "yellow-1"
                    :class-name     "switch-control"}]))))

(defn- lock-control
  []
  (let [{:keys [disabled? read-only? visible?]} @(re-frame/subscribe [::state/lock-control-state props])
        activity-id @(re-frame/subscribe [::common-state/activity-id])
        activity-locked? @(re-frame/subscribe [::common-state/activity-locked?])
        loading? @(re-frame/subscribe [::state/lock-loading?])
        label (cond
                loading? "Saving..."
                activity-locked? "Unlock Activity"
                :default "Lock Activity")
        handle-change #(re-frame/dispatch [::state/set-locked activity-id %])]
    (when visible?
      (if read-only?
        [control-state {:text (if activity-locked? "Locked" "Unlocked")
                        :icon "lock"}]
        [ui/switch {:checked?       activity-locked?
                    :disabled?      disabled?
                    :indeterminate? loading?
                    :label          label
                    :on-change      handle-change
                    :color          "yellow-1"
                    :class-name     "switch-control"}]))))

(defn publish-controls
  []
  [:<>
   [publish-control]
   [lock-control]])
