(ns webchange.ui.components.chip.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.icon.views :refer [general-icon]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn chip
  [{:keys [class-name counter disabled? icon on-click state]}]
  (let [handle-click #(when (and (fn? on-click)
                                 (not disabled?))
                        (on-click %))]
    [:div {:class-name (get-class-name {"bbs--chip"                     true
                                        "bbs--chip--clickable"          (fn? on-click)
                                        "bbs--chip--with-icon"          (some? icon)
                                        "bbs--chip--with-counter"       (some? counter)
                                        "bbs--chip--state-disabled"     disabled?
                                        (str "bbs--chip--state-" state) (some? state)
                                        class-name                      (some? class-name)})
           :on-click   handle-click}
     (when (some? icon)
       [general-icon {:icon       icon
                      :class-name "bbs--chip--icon"}])
     (->> (r/current-component)
          (r/children)
          (into [:div.bbs--chip--text]))
     (when (some? counter)
       [:div.bbs--chip--counter counter])]))
