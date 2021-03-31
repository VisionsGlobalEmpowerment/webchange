(ns webchange.ui-framework.components.menu.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.confirm.index :as confirm-component]
    [webchange.ui-framework.components.icon.index :as icon-component]
    [webchange.ui-framework.components.icon-button.index :as icon-button-component]))

(defn- menu-item
  [{:keys [close-menu icon text on-click]}]
  [:div.wc-menu-list-item {:on-click #(when (some? on-click)
                                        (on-click)
                                        (close-menu))}
   (when (some? icon)
     [icon-component/component {:icon       icon
                                :class-name "wc-menu-list-item-icon"}])
   [:span text]])

(defn- confirmed-menu-item
  [{:keys [close-menu confirm on-click]
    :or   {on-click #()}
    :as   props}]
  [confirm-component/component {:message    confirm
                                :on-confirm #(do (on-click)
                                                 (close-menu))
                                :on-cancel  close-menu}
   [menu-item (dissoc props :on-click)]])

(defn component
  [{:keys [items]}]
  (r/with-let [show-menu? (r/atom false)
               menu-ref (r/atom nil)

               close-menu-ref (atom nil)

               handle-document-click #(when-not (.contains @menu-ref (.-target %)) (@close-menu-ref))
               set-document-click-handler #(js/document.addEventListener "click" handle-document-click)
               reset-document-click-handler #(js/document.removeEventListener "click" handle-document-click)

               close-menu #(do (reset! show-menu? false)
                               (reset-document-click-handler))
               show-menu #(do (reset! show-menu? true)
                              (set-document-click-handler))
               _ (reset! close-menu-ref close-menu)

               handle-menu-button-click #(if @show-menu?
                                           (close-menu)
                                           (show-menu))]
    [:div.wc-menu {:ref #(when (some? %) (reset! menu-ref %))}
     [icon-button-component/component {:icon     "menu"
                                       :on-click handle-menu-button-click}]
     (when @show-menu?
       [:div.wc-menu-list
        (for [[idx {:keys [confirm] :as item}] (map-indexed vector items)]
          (let [component (if (some? confirm) confirmed-menu-item menu-item)
                props (merge item {:close-menu close-menu})]
            ^{:key idx}
            [component props]))])]))
