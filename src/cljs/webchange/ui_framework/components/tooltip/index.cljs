(ns webchange.ui-framework.components.tooltip.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [title]}]
  (r/with-let [open? (r/atom false)
               ref (atom nil)
               handle-ref (fn [target]
                            (when (some? target)
                              (reset! ref target)
                              (.addEventListener target "mouseenter" #(reset! open? true))
                              (.addEventListener target "mouseleave" #(reset! open? false))))]
    (->> (r/current-component)
         (r/children)
         (into [:div {:class-name "wc-tooltip"
                      :ref        handle-ref}
                (when @open?
                  [:div {:class-name (get-class-name {"wc-tooltip-title" true
                                                      "open"             @open?})}
                   title])]))))
