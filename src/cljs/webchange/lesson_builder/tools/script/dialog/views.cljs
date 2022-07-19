(ns webchange.lesson-builder.tools.script.dialog.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.block.views :refer [block]]
    [webchange.lesson-builder.tools.script.dialog-item.views :refer [dialog-item]]
    [webchange.lesson-builder.tools.script.dialog.state :as state]
    [webchange.ui.index :as ui]))

(defn- dialog-footer
  [{:keys [action-path]}]
  (let [blocked? @(re-frame/subscribe [::state/user-interactions-blocked? action-path])
        handle-change #(re-frame/dispatch [::state/toggle-user-interactions-block action-path])]
    [:div.component--dialog--footer
     [ui/switch {:label      "Block Interaction"
                 :checked?   blocked?
                 :on-change  handle-change
                 :class-name "component--dialog--block"}]]))

(defn dialog
  [{:keys [action-path] :as props}]
  (let [dialog-name @(re-frame/subscribe [::state/dialog-name action-path])
        dialog-items @(re-frame/subscribe [::state/dialog-items action-path])]
    [block {:title               dialog-name
            :class-name--content "component--dialog"
            :footer              [dialog-footer props]}
     (for [{:keys [id] :as dialog-item-data} dialog-items]
       ^{:key id}
       [dialog-item dialog-item-data])]))
