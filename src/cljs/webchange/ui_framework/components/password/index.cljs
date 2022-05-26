(ns webchange.ui-framework.components.password.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.icon-button.index :as icon-button]
    [webchange.ui-framework.components.input.index :as input]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [class-name] :as props}]
  (r/with-let [text-visible? (r/atom false)
               toggle-visibility #(swap! text-visible? not)]
    [:div {:class-name (get-class-name (-> {"wc-password-wrapper" true}
                                           (assoc class-name (some? class-name))))}
     [input/component (-> props
                          (assoc :type (if @text-visible? "text" "password"))
                          (dissoc :class-name))]
     [icon-button/component {:icon       (if @text-visible? "visibility-on" "visibility-off")
                             :variant    "light"
                             :class-name "visibility-button"
                             :on-click   toggle-visibility}]]))
