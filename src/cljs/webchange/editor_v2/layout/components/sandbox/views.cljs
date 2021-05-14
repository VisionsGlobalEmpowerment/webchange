(ns webchange.editor-v2.layout.components.sandbox.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.sandbox.state :as state]
    [webchange.editor-v2.layout.components.sandbox.state-modal :as state-modal]
    [webchange.editor-v2.layout.components.sandbox.views-modal :refer [share-modal]]
    [webchange.ui-framework.components.index :refer [button]]))

(defn share-button
  []
  (let [link @(re-frame/subscribe [::state/link])
        has-params? @(re-frame/subscribe [::state/form-has-params?])
        handle-click (fn [] (re-frame/dispatch [::state-modal/open]))]
    [:div
     (if has-params?
       [button {:on-click handle-click}
        "Preview"]
       [button {:href       link
                :class-name "open-link"}
        "Preview"])
     [share-modal]]))
