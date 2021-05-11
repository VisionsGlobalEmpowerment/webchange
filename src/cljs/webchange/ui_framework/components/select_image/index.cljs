(ns webchange.ui-framework.components.select-image.index
  (:require
    [reagent.core :as r]
    [webchange.ui-framework.components.utils :refer [get-bounding-rect]]))

(defn- current-value
  [{:keys [on-click ref value]}]
  [:div {:class-name "current-value"
         :on-click   on-click
         :ref        #(when (some? %) (reset! ref %))}
   (if value
     [:img {:src value}]
     [:div.no-value])
   [:div.arrow]])

(defn- options-list-item
  [{:keys [on-click thumbnail value]}]
  (let [thumb (or thumbnail value)
        handle-click #(on-click value)]
    [:img {:src        thumb
           :class-name "options-list-item"
           :on-click   handle-click}]))

(defn- options-list
  [{:keys [on-click options dimensions ref]}]
  [:div {:class-name "options-list"
         :style      {:top   (:top dimensions)
                      :left  (:left dimensions)
                      :width (:width dimensions)}
         :ref        #(when (some? %) (reset! ref %))}
   [:div.items-wrapper
    (for [{:keys [value] :as option} options]
      ^{:key value}
      [options-list-item (merge option
                                {:on-click on-click})])]])

(defn- get-menu-position
  [menu-button]
  (let [{:keys [x y width height]} (get-bounding-rect menu-button)]
    {:top   (+ y height)
     :left  x
     :width width}))

(defn component
  [{:keys [on-change options value]
    :or   {on-change #()
           options   []}}]
  (r/with-let [show-options-list? (r/atom false)
               options-list-ref (r/atom nil)
               options-list-dimensions (r/atom {:top  0
                                                :left 0})

               current-value-ref (atom nil)
               close-menu-ref (atom nil)

               handle-document-click #(when-not (.contains @options-list-ref (.-target %)) (@close-menu-ref))
               set-document-click-handler #(js/document.addEventListener "click" handle-document-click)
               reset-document-click-handler #(js/document.removeEventListener "click" handle-document-click)

               close-menu #(do (reset! show-options-list? false)
                               (reset-document-click-handler))
               show-menu #(do (reset! show-options-list? true)
                              (reset! options-list-dimensions (get-menu-position @current-value-ref))
                              (set-document-click-handler))

               handle-list-item-click (fn [value]
                                        (close-menu)
                                        (on-change value))

               _ (reset! close-menu-ref close-menu)]
    [:div.wc-select-image
     [current-value {:value    value
                     :on-click show-menu
                     :ref      current-value-ref}]
     (when @show-options-list?
       [options-list {:options    options
                      :on-click   handle-list-item-click
                      :dimensions @options-list-dimensions
                      :ref        options-list-ref}])]))
