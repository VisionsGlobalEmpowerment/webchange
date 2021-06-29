(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.menu.views :refer [add-concept-action add-scene-action unit-menu]]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.utils :refer [get-effect-name]]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state]
    [webchange.editor-v2.dialog.dialog-text-form.state-actions :as state-actions]
    [webchange.logger.index :as logger]
    [webchange.ui-framework.components.index :refer [icon menu]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- target-control
  [{:keys [value options on-change]}]
  [menu {:class-name "targets-menu"
         :el         (r/as-element [:span.target-value
                                    (if-not (empty? value) value "select target")])
         :items      (->> options
                          (map (fn [target]
                                 {:text     target
                                  :on-click #(on-change target)})))}])

(defn- phrase-target-control
  [{:keys [path source character]}]
  (r/with-let [_ (re-frame/dispatch [::state/set-current-target path character])]
    (let [current-target @(re-frame/subscribe [::state/current-target path])
          available-targets @(re-frame/subscribe [::state/available-targets])
          handle-target-change (fn [target]
                                 (re-frame/dispatch [::state/set-phrase-action-target path source target]))]
      [target-control {:value     current-target
                       :options   available-targets
                       :on-change handle-target-change}])))

(defn- text-animation-target-control
  [{:keys [path source text-object]}]
  (r/with-let [_ (re-frame/dispatch [::state/set-current-text-animation-target path text-object])]
    (let [current-target @(re-frame/subscribe [::state/current-text-animation-target path])
          available-targets @(re-frame/subscribe [::state/available-text-animation-targets])
          handle-target-change (fn [target]
                                 (re-frame/dispatch [::state/set-text-animation-action-target path source target]))]
      [target-control {:value     current-target
                       :options   available-targets
                       :on-change handle-target-change}])))

(defn- text-control
  [{:keys [on-change on-enter-press on-ctrl-enter-press]}]
  (let [ref (atom nil)
        handle-key-down (fn [e]
                          (when (= (.-key e) "Enter")
                            (.preventDefault e)
                            (if (.-ctrlKey e)
                              (on-ctrl-enter-press)
                              (on-enter-press))))]
    (r/create-class
      {:display-name "text-control"
       :should-component-update
                     (constantly false)
       :component-did-mount
                     (fn [this]
                       (let [{:keys [editable?]} (r/props this)]
                         (when editable?
                           (.addEventListener @ref "keydown" handle-key-down false))))
       :component-will-unmount
                     (fn []
                       (.removeEventListener @ref "keydown" handle-key-down))
       :reagent-render
                     (fn [{:keys [value editable?]}]
                       (let [handle-change (fn [event]
                                             (let [new-value (.. event -target -innerText)]
                                               (on-change new-value)))]
                         [:span (cond-> {:class-name (get-class-name {"text"          true
                                                                      "text-disabled" (not editable?)})
                                         :ref        #(when (some? %) (reset! ref %))}
                                        editable? (merge {:on-input                          handle-change
                                                          :content-editable                  true
                                                          :suppress-content-editable-warning true}))
                          value]))})))

(defn- phrase-text-control
  [{:keys [action-data path source text]}]
  (let [handle-enter-press (fn [] (add-scene-action {:action-data action-data}))
        handle-ctrl-enter-press (fn [] (add-concept-action {:action-data action-data}))
        handle-text-change (fn [new-value] (re-frame/dispatch [::state-actions/set-phrase-text {:action-path path
                                                                                                :action-type source
                                                                                                :value       new-value}]))]
    [text-control {:value               text
                   :editable?           true
                   :on-change           handle-text-change
                   :on-enter-press      handle-enter-press
                   :on-ctrl-enter-press handle-ctrl-enter-press}]))

(defn- phrase-unit
  [{:keys [source concept-name] :as props}]
  (let [concept? (= source :concept)]
    [:div (cond-> {:class-name (get-class-name {"phrase-unit"  true
                                                "concept-unit" concept?})}
                  concept? (assoc :title (str "Concept «" concept-name "»")))
     [phrase-target-control props]
     [phrase-text-control props]]))

(defn- effect-unit
  [{:keys [effect class-name]}]
  (let [effect-name (get-effect-name effect)]
    [:div {:class-name (get-class-name (merge class-name
                                              {"effect-unit" true}))}
     [icon {:icon       "effect"
            :class-name "effect-icon"}]
     effect-name]))

(defn- text-animation-unit
  [{:keys [text] :as props}]
  [:div {:class-name "text-animation-unit"}
   [text-animation-target-control props]
   [text-control {:value     text
                  :editable? false}]])

(defn- unknown-element
  [{:keys [type] :as props}]
  (logger/warn "Unknown element type: " type)
  (logger/trace-folded "Props" props)
  [:div.unknown-unit "Not editable action"])

(defn action-unit
  [{:keys [idx parallel-mark type] :as props}]
  (r/with-let [container-ref (r/atom nil)]
    [:div {:ref        #(when (some? %) (reset! container-ref %))
           :class-name (get-class-name {"action-unit"     true
                                        "parallel"        (not= parallel-mark :none)
                                        "parallel-start"  (= parallel-mark :start)
                                        "parallel-middle" (= parallel-mark :middle)
                                        "parallel-end"    (= parallel-mark :end)})}
     (case type
       :effect [effect-unit props]
       :phrase [phrase-unit props]
       :text-animation [text-animation-unit props]
       [unknown-element props])
     [unit-menu {:idx         idx
                 :parent-ref  @container-ref
                 :action-data props}]]))
