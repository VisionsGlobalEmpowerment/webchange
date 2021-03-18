(ns webchange.book-creator.text-form.views-voice-over-button
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]
    [webchange.ui-framework.index :refer [icon-button]]))

(defn voice-over-button
  [{:keys [id]}]
  (let [window-options {:components     {:description  {:hide? true}
                                         :node-options {:hide? true}
                                         :target       {:hide? true}
                                         :phrase       {:hide? true}
                                         :diagram      {:hide? true}}
                        :single-phrase? true}
        handle-click (fn [] (re-frame/dispatch [::state/open-dialog-window id window-options]))]
    [icon-button {:icon       "mic"
                  :color      "primary"
                  :class-name "voice-over"
                  :on-click handle-click}]))
