(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views-menu
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.utils :refer [get-effect-name]]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state]
    [webchange.editor-v2.dialog.dialog-form.state.actions-utils :refer [get-available-effects]]
    [webchange.editor-v2.dialog.phrase-voice-over.audios-menu.views :refer [audios-menu]]
    [webchange.ui-framework.components.index :refer [button icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- pre-effect [value] (string? value))
(defn- pre-relative-position [value] (or (nil? value) (some #{value} [:after :before :parallel])))
(defn- pre-action-data [{:keys [node-data type]}] (and (some #{type} [:effect :concept-phrase :scene-phrase])
                                                       (some? node-data)))

(defn add-effect-action
  [{:keys [action-data effect relative-position] :or {relative-position :after}}]
  {:pre [(pre-action-data action-data)
         (pre-effect effect)
         (pre-relative-position relative-position)]}
  (re-frame/dispatch [::state/add-effect-action (merge action-data
                                                       {:effect            effect
                                                        :relative-position relative-position})]))

(defn add-scene-action
  [{:keys [action-data relative-position] :or {relative-position :after}}]
  {:pre [(pre-action-data action-data)
         (pre-relative-position relative-position)]}
  (re-frame/dispatch [::state/add-scene-action (merge action-data
                                                      {:relative-position relative-position})]))

(defn add-scene-parallel-action
  [{:keys [action-data]}]
  {:pre [(pre-action-data action-data)]}
  (re-frame/dispatch [::state/add-scene-parallel-action action-data]))

(defn add-concept-action
  [{:keys [action-data relative-position] :or {relative-position :after}}]
  {:pre [(pre-action-data action-data)
         (pre-relative-position relative-position)]}
  (re-frame/dispatch [::state/add-concept-action (merge action-data
                                                        {:relative-position relative-position})]))

(defn remove-action
  [{:keys [action-data]}]
  {:pre [(pre-action-data action-data)]}
  (re-frame/dispatch [::state/remove-action action-data]))

(defn- get-controls
  [{:keys [type node-data] :as action-data}]
  (let [show-concepts? @(re-frame/subscribe [::state/show-concepts?])
        available-effects (get-available-effects node-data)]
    (cond-> [;; Remove
             {:control [icon-button {:icon       "remove"
                                     :size       "small"
                                     :title      "Remove action"
                                     :class-name "remove-button"
                                     :on-click   #(remove-action {:action-data action-data})}]}
             ;; Add scene action
             {:control   [icon-button {:icon       "add"
                                       :size       "small"
                                       :title      "Add activity action"
                                       :class-name "add-scene-button"
                                       :on-click   #(add-scene-action {:action-data action-data})}]
              :sub-items [{:control [icon-button {:icon     "insert-before"
                                                  :size     "small"
                                                  :title    "Before"
                                                  :on-click #(add-scene-action {:action-data       action-data
                                                                                :relative-position :before})}]}
                          {:control [icon-button {:icon     "insert-after"
                                                  :size     "small"
                                                  :title    "After"
                                                  :on-click #(add-scene-action {:action-data       action-data
                                                                                :relative-position :after})}]}
                          {:control [icon-button {:icon     "insert-parallel"
                                                  :size     "small"
                                                  :title    "Parallel"
                                                  :on-click #(add-scene-parallel-action {:action-data action-data})}]}]}]
            ;; Add concept action
            show-concepts?
            (concat [{:control   [icon-button {:icon       "add-box"
                                               :size       "small"
                                               :title      "Add concept action"
                                               :class-name "add-concept-button"
                                               :on-click   #(add-concept-action {:action-data action-data})}]
                      :sub-items [{:control [icon-button {:icon     "insert-before"
                                                          :size     "small"
                                                          :title    "Before"
                                                          :on-click #(add-concept-action {:action-data       action-data
                                                                                          :relative-position :before})}]}
                                  {:control [icon-button {:icon     "insert-after"
                                                          :size     "small"
                                                          :title    "After"
                                                          :on-click #(add-concept-action {:action-data       action-data
                                                                                          :relative-position :after})}]}]}])

            ;; Add effects
            (-> available-effects empty? not)
            (concat [{:control             [icon-button {:icon  "effect"
                                                         :size  "small"
                                                         :title "Add effect"}]
                      :sub-items-direction "column"
                      :sub-items           (map (fn [effect]
                                                  {:control   [button {:size       "small"
                                                                       :class-name "effect-button"
                                                                       :color      "default"
                                                                       :variant    "outlined"
                                                                       :on-click   #(add-effect-action {:action-data action-data
                                                                                                        :effect      effect})}
                                                               (get-effect-name effect)]
                                                   :sub-items [{:control [icon-button {:icon     "insert-before"
                                                                                       :size     "small"
                                                                                       :title    "Before"
                                                                                       :on-click #(add-effect-action {:action-data       action-data
                                                                                                                      :relative-position :before
                                                                                                                      :effect            effect})}]}
                                                               {:control [icon-button {:icon     "insert-after"
                                                                                       :size     "small"
                                                                                       :title    "After"
                                                                                       :on-click #(add-effect-action {:action-data       action-data
                                                                                                                      :relative-position :after
                                                                                                                      :effect            effect})}]}
                                                               {:control [icon-button {:icon     "insert-parallel"
                                                                                       :size     "small"
                                                                                       :title    "Parallel"
                                                                                       :on-click #(add-effect-action {:action-data       action-data
                                                                                                                      :relative-position :parallel
                                                                                                                      :effect            effect})}]}]})
                                                available-effects)}])
            (some #{type} [:concept-phrase :scene-phrase])
            (concat [{:control   [icon-button {:icon  "mic"
                                               :size  "small"
                                               :title "Voice-over"}]
                      :sub-items [{:control [audios-menu {:action-data action-data}]}]}]))))

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
                   :style      {:z-index (- 1000 idx)}
                   :ref        #(when (some? %) (handle-menu-ref %))}]
            (if @show-controls? (render-controls controls) [])))
    (finally
      (unsubscribe-handlers menu-ref parent-ref events-handlers))))
