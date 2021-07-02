(ns webchange.editor-v2.dialog.dialog-text-form.menu.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.state-actions :as state-actions]
    [webchange.ui-framework.components.index :refer [icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- pre-relative-position [value] (or (nil? value) (some #{value} [:after :before :parallel])))
(defn- pre-action-data [{:keys [node-data source]}] (and (some #{source} [:concept :scene])
                                                         (some? node-data)))

(defn add-scene-action
  [{:keys [action-data relative-position] :or {relative-position :after}}]
  {:pre [(pre-action-data action-data)
         (pre-relative-position relative-position)]}
  (re-frame/dispatch [::state-actions/add-scene-action (merge action-data
                                                              {:relative-position relative-position})]))

(defn add-concept-action
  [{:keys [action-data relative-position] :or {relative-position :after}}]
  {:pre [(pre-action-data action-data)
         (pre-relative-position relative-position)]}
  (re-frame/dispatch [::state-actions/add-concept-action (merge action-data
                                                                {:relative-position relative-position})]))

(defn remove-action
  [{:keys [action-data]}]
  {:pre [(pre-action-data action-data)]}
  (re-frame/dispatch [::state-actions/remove-action action-data]))

(defn- get-controls
  [{:keys [node-data] :as action-data}]
  (cond-> [;; Remove
           {:control [icon-button {:icon       "remove"
                                   :size       "small"
                                   :title      "Remove action"
                                   :class-name "remove-button"
                                   :on-click   #(remove-action {:action-data action-data})}]}]))

;; Render

(defn- menu-parent-control
  [{:keys [control sub-items sub-items-direction]}]
  [:div.menu-item-wrapper
   control
   (when (some? sub-items)
     (into [:div {:class-name (get-class-name {"sub-items"        true
                                               "sub-items-column" (= sub-items-direction "column")})}]
           (map (fn [sub-item]
                  [menu-parent-control sub-item])
                sub-items)))])

(defn- render-controls
  [controls]
  (map (fn [{:keys [control sub-items] :as item}]
         (if (some? sub-items)
           [menu-parent-control item]
           control))
       controls))

(defn- subscribe-handlers
  [menu-ref parent-ref events-handlers]
  (.addEventListener @menu-ref "mouseenter" (:menu-enter events-handlers))
  (.addEventListener @menu-ref "mouseleave" (:menu-leave events-handlers))
  (.addEventListener @parent-ref "mouseenter" (:parent-enter events-handlers))
  (.addEventListener @parent-ref "mouseleave" (:parent-leave events-handlers)))

(defn- unsubscribe-handlers
  [menu-ref parent-ref events-handlers]
  (.removeEventListener @menu-ref "mouseenter" (:menu-enter events-handlers))
  (.removeEventListener @menu-ref "mouseleave" (:menu-leave events-handlers))
  (.removeEventListener @parent-ref "mouseenter" (:parent-enter events-handlers))
  (.removeEventListener @parent-ref "mouseleave" (:parent-leave events-handlers)))

(defn unit-menu
  [{:keys [action-data idx]}]
  (r/with-let [show-controls? (r/atom false)

               mouse-over-parent (atom false)
               mouse-over-menu (atom false)
               check-mouse-position (fn [] (reset! show-controls? (or @mouse-over-parent @mouse-over-menu)))

               menu-ref (atom nil)
               parent-ref (atom nil)

               events-handlers {:menu-enter   (fn [e] (.stopPropagation e) (reset! mouse-over-menu true) (check-mouse-position))
                                :menu-leave   (fn [e] (.stopPropagation e) (reset! mouse-over-menu false) (check-mouse-position))
                                :parent-enter (fn [e] (.stopPropagation e) (reset! mouse-over-parent true) (check-mouse-position))
                                :parent-leave (fn [e] (.stopPropagation e) (reset! mouse-over-parent false) (check-mouse-position))}

               handle-menu-ref (fn [ref]
                                 (reset! menu-ref ref)
                                 (reset! parent-ref (.-parentNode ref))
                                 (subscribe-handlers menu-ref parent-ref events-handlers))]
    (let [controls (get-controls action-data)]
      (into [:div {:class-name (get-class-name {"side-controls"    true
                                                "menu-placeholder" (not @show-controls?)
                                                "menu-active"      (and @show-controls? (-> controls empty? not))})
                   :style      {:z-index (- 300 idx)}
                   :ref        #(when (some? %) (handle-menu-ref %))}]
            (if @show-controls? (render-controls controls) [])))
    (finally
      (unsubscribe-handlers menu-ref parent-ref events-handlers))))
