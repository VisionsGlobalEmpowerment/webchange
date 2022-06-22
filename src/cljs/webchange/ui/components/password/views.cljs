(ns webchange.ui.components.password.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.components.input.views :refer [input]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn password
  [{:keys [class-name] :as props}]
  (r/with-let [text-visible? (r/atom false)
               toggle-visibility #(swap! text-visible? not)]
    [:div {:class-name (get-class-name (-> {"bbs--password-wrapper" true}
                                           (assoc class-name (some? class-name))))}
     [input (-> props
                (assoc :type (if @text-visible? "text" "password"))
                (dissoc :class-name))]
     [button {:icon       (if @text-visible? "visibility-on" "visibility-off")
              :class-name "visibility-button"
              :color      (if @text-visible? "blue-2" "grey-4")
              :on-click   toggle-visibility}]]))
