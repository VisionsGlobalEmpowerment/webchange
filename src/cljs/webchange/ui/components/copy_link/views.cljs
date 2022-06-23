(ns webchange.ui.components.copy-link.views
  (:require
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.components.input-label.views :refer [input-label]]))

(defn copy-link
  [{:keys [label text value]
    :or   {text "Copy"}}]
  (let [handle-button-click #(-> (.-clipboard js/navigator)
                                 (.writeText value))]
    [:div {:class-name "bbs--copy-link"}
     (when (some? label)
       [input-label label])
     [button {:shape      "rounded"
              :class-name "bbs--copy-link--button"
              :on-click   handle-button-click}
      text]]))
