(ns webchange.ui-framework.components.message.index
  (:require
    [webchange.ui-framework.components.icon.index :as icon-component]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def icons {"warn" "warning"})

(defn component
  [{:keys [class-name message type]}]
  [:div {:class-name (get-class-name (cond-> {"wc-message" true}
                                             (some? class-name) (assoc class-name true)))}
   (when (contains? icons type)
     [icon-component/component {:icon       (get icons type)
                                :class-name "wc-message-icon"}])
   (when (some? message)
     [:div.wc-message-text
      message])])
