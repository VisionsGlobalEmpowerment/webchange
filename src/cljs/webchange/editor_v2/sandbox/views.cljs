(ns webchange.editor-v2.sandbox.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.sandbox.state :as state]
    [webchange.editor-v2.sandbox.views-modal :refer [share-modal]]
    [webchange.ui-framework.components.index :refer [button]]))

(defn share-button
  []
  (let [handle-click (fn [] (re-frame/dispatch [::state/open]))]
    [:div
     [button {:on-click handle-click}
      "Preview"]
     [share-modal]]))
