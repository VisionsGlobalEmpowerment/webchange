(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views-menu
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- get-phrase-controls
  [{:keys [type] :as action-data}]
  (cond
    (or (= type :scene-phrase)
        (= type :concept-phrase)) (let [handle-remove-click #(re-frame/dispatch [::state/remove-action action-data])
                                        handle-add-scene-click #(re-frame/dispatch [::state/add-scene-action action-data])
                                        handle-add-concept-click #(re-frame/dispatch [::state/add-concept-action action-data])]
                                    [[icon-button {:icon       "remove"
                                                   :size       "small"
                                                   :title      "Remove action"
                                                   :class-name "remove-button"
                                                   :on-click   handle-remove-click}]
                                     [icon-button {:icon       "add"
                                                   :size       "small"
                                                   :title      "Add activity action"
                                                   :class-name "add-scene-button"
                                                   :on-click   handle-add-scene-click}]
                                     [icon-button {:icon       "add-box"
                                                   :size       "small"
                                                   :title      "Add concept action"
                                                   :class-name "add-concept-button"
                                                   :on-click   handle-add-concept-click}]])
    :else []))

(defn- get-controls
  [props]
  (get-phrase-controls props))

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

               events-handlers {:menu-enter   (fn [e] (print ":menu-enter") (.stopPropagation e) (reset! mouse-over-menu true) (check-mouse-position))
                                :menu-leave   (fn [e] (print ":menu-leave") (.stopPropagation e) (reset! mouse-over-menu false) (check-mouse-position))
                                :parent-enter (fn [e] (print ":parent-enter") (.stopPropagation e) (reset! mouse-over-parent true) (check-mouse-position))
                                :parent-leave (fn [e] (print ":parent-leave") (.stopPropagation e) (reset! mouse-over-parent false) (check-mouse-position))}

               handle-menu-ref (fn [ref]
                                 (reset! menu-ref ref)
                                 (reset! parent-ref (.-parentNode ref))
                                 (subscribe-handlers menu-ref parent-ref events-handlers))]
    (let [controls (get-controls action-data)]
      (into [:div {:class-name (get-class-name {"side-controls"    true
                                                "menu-placeholder" (not @show-controls?)
                                                "menu-active"      (and @show-controls? (-> controls empty? not))})
                   :style      {:z-index (- 1000 idx)}
                   :ref        #(when (some? %) (handle-menu-ref %))}]
            (if @show-controls? controls [])))
    (finally
      (unsubscribe-handlers menu-ref parent-ref events-handlers))))
