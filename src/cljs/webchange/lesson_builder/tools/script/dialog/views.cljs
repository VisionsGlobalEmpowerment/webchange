(ns webchange.lesson-builder.tools.script.dialog.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.script.block.views :refer [block]]
    [webchange.lesson-builder.tools.script.dialog-item.views :refer [dialog-item]]
    [webchange.lesson-builder.tools.script.dialog.state :as state]))

(defn dialog
  [{:keys [action-path]}]
  (let [dialog-name @(re-frame/subscribe [::state/dialog-name action-path])
        dialog-items @(re-frame/subscribe [::state/dialog-items action-path])]
    [block {:title dialog-name}
     (for [{:keys [id] :as dialog-item-data} dialog-items]
       ^{:key id}
       [dialog-item dialog-item-data])]))
