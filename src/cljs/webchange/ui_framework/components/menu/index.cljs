(ns webchange.ui-framework.components.menu.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.confirm.index :as confirm-component]
    [webchange.ui-framework.components.icon.index :as icon-component]
    [webchange.ui-framework.components.icon-button.index :as icon-button-component]
    [webchange.ui-framework.components.utils :refer [get-bounding-rect get-class-name]]))

(defn- menu-item
  [{:keys [close-menu icon text on-click has-icon?]}]
  [:div.wc-menu-list-item {:on-click #(when (some? on-click)
                                        (on-click)
                                        (close-menu))}
   (when has-icon?
     [icon-component/component {:icon       (if (some? icon) icon "none")
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

(defn- get-menu-position
  [menu-button list-position]
  (let [{:keys [x y width height]} (get-bounding-rect menu-button)]
    (case list-position
      "bottom" {:top  (if (> 371 y)
                        (+ y height)
                        (- y 191))
                :left x}
      "right" {:top  y
               :left (+ x width)})))

(defn component
  [{:keys [class-name items icon el list-position title]
    :or   {icon          "horizontal"
           list-position "bottom"}}]
  "Props:
   :list-position - menu position relative to button. 'bottom' | 'right'
   :items - items list
       .text - item text
       .icon - icon name
       .on-click - click handler, called after confirm if '.confirm' defined
       .confirm - text message for confirm window
   :icon - 'vertical' or 'horizontal'."
  (r/with-let [show-menu? (r/atom false)
               menu-ref (r/atom nil)
               menu-position (r/atom {:top  0
                                      :left 0})

               close-menu-ref (atom nil)
               handle-document-click #(when-not (.contains @menu-ref (.-target %)) (@close-menu-ref))
               set-document-click-handler #(js/document.addEventListener "click" handle-document-click)
               reset-document-click-handler #(js/document.removeEventListener "click" handle-document-click)

               close-menu #(do (reset! show-menu? false)
                               (reset-document-click-handler))
               show-menu #(do (reset! show-menu? true)
                              (reset! menu-position (get-menu-position @menu-ref list-position))
                              (set-document-click-handler))
               _ (reset! close-menu-ref close-menu)

               handle-menu-button-click #(if @show-menu?
                                           (close-menu)
                                           (show-menu))]
    (let [items-has-icon? (some #(:icon %) items)]
      [:div (cond-> {:class-name (get-class-name (cond-> {"wc-menu" true}
                                                         (some? class-name) (assoc class-name true)))
                     :ref        #(when (some? %) (reset! menu-ref %))}
                    (some? title) (assoc :title title))
       (if (some? el)
         [:div {:on-click handle-menu-button-click} el]
         [icon-button-component/component {:icon     (case icon
                                                       "horizontal" "menu"
                                                       "vertical" "menu-vertical"
                                                       icon)
                                           :on-click handle-menu-button-click}])
       (when @show-menu?
         [:div.wc-menu-list {:style {:top  (:top @menu-position)
                                     :left (:left @menu-position)}}
          (for [[idx {:keys [confirm] :as item}] (map-indexed vector items)]
            (let [component (if (some? confirm) confirmed-menu-item menu-item)
                  props (merge item {:close-menu close-menu
                                     :has-icon?  items-has-icon?})]
              ^{:key idx}
              [component props]))])])))
