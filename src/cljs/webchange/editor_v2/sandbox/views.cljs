(ns webchange.editor-v2.sandbox.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.sandbox.state :as state]
    [webchange.editor-v2.sandbox.views-modal :refer [share-modal]]
    [webchange.ui-framework.index :refer [icon-button]]))

(defn share-button
  []
  (let [handle-click (fn [] (re-frame/dispatch [::state/open]))]
    [:div
     [icon-button {:icon       "play"
                   :class-name "share-button"
                   :on-click   handle-click}]
     [share-modal]]))
