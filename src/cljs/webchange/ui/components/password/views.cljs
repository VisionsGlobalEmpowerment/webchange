(ns webchange.ui.components.password.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.button.views :refer [button]]
    [webchange.ui.components.input.views :refer [input]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn password
  [{:keys [class-name disabled?] :as props}]
  (r/with-let [text-visible? (r/atom false)
               toggle-visibility #(swap! text-visible? not)]
    [:div {:class-name (get-class-name (-> {"bbs--password-wrapper" true}
                                           (assoc class-name (some? class-name))))}
     [input (cond-> props
                    :always (assoc :type (if @text-visible? "text" "password"))
                    :always (dissoc :class-name)
                    (not disabled?) (assoc :action {:icon       (if @text-visible? "visibility-on" "visibility-off")
                                                    :class-name "bbs--password--visibility-button"
                                                    :on-click   toggle-visibility}))]]))
