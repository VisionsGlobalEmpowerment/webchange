(ns webchange.ui.components.tab.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.icon.views :refer [navigation-icon system-icon]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

;[{}
;               {:action "plus"}
;               {:align "center"}
;               {:icon    "create"
;                :counter 10}]

(defn tab
  [{:keys [action align class-name counter disabled? href icon on-click state target title]
    :or   {disabled? false
           on-click  #()
           target    "_blank"}}]
  (let [handle-click (fn []
                       (if (some? href)
                         (js/window.open href target)
                         (on-click)))]
    [:button (cond-> {:class-name (get-class-name {"bbs--tab"                     true
                                                   (str "bbs--tab--state-" state) (some? state)
                                                   (str "bbs--tab--align-" align) (some? align)
                                                   class-name                     (some? class-name)})
                      :disabled   disabled?
                      :on-click   handle-click}
                     (some? title) (assoc :title title))
     (when (some? icon)
       [navigation-icon {:icon       icon
                         :class-name "bbs--tab--icon"}])
     (->> (r/current-component)
          (r/children)
          (into [:div.bbs--tab--content]))
     (when (some? counter)
       [:div.bbs--tab--counter counter])
     (when (some? action)
       [system-icon {:icon action}])]))
