(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state]
    [webchange.ui-framework.components.index :refer [icon icon-button menu select]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- target-control
  [{:keys [path type value]}]
  (r/with-let [_ (re-frame/dispatch [::state/set-current-target path value])]
    (let [current-target @(re-frame/subscribe [::state/current-target path])
          available-targets @(re-frame/subscribe [::state/available-targets])
          handle-target-change (fn [target]
                                 (re-frame/dispatch [::state/set-phrase-action-target path type target]))]
      [menu {:class-name "targets-menu"
             :el         (r/as-element [:span.target-value (if-not (empty? current-target) current-target "___")])
             :items      (->> available-targets
                              (map (fn [target]
                                     {:text     target
                                      :on-click #(handle-target-change target)})))}])))

(defn- text-control
  []
  (let []
    (r/create-class
      {:display-name "text-control"
       :should-component-update
                     (constantly false)
       :reagent-render
                     (fn [{:keys [path type value]}]
                       (let [handle-change (fn [event]
                                             (let [new-value (.. event -target -innerText)]
                                               (re-frame/dispatch [::state/set-phrase-action-text path type new-value])))]
                         [:span {:class-name                        "text"
                                 :on-input                          handle-change
                                 :content-editable                  true
                                 :suppress-content-editable-warning true}
                          value]))})))

(defn- side-controls
  [{:keys [ref action-data]}]
  (let [handle-remove-click #(re-frame/dispatch [::state/remove-action action-data])
        handle-add-scene-click #(re-frame/dispatch [::state/add-scene-action action-data])
        handle-add-concept-click #(re-frame/dispatch [::state/add-concept-action action-data])]
    [:div.side-controls {:ref ref}
     [icon-button {:icon       "remove"
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
                   :on-click   handle-add-concept-click}]]))

(defn- phrase-unit
  [{:keys [text character path type concept-name node-data parallel-mark class-name]}]
  (r/with-let [show-controls? (r/atom false)
               mouse-over-text (atom false)
               mouse-over-controls (atom false)
               check-mouse-position (fn []
                                      (reset! show-controls? (or @mouse-over-text @mouse-over-controls)))

               text-ref (atom nil)
               controls-ref (atom nil)
               handle-text-ref (fn [ref]
                                 (reset! text-ref ref)
                                 (.addEventListener @text-ref "mouseenter" (fn [] (reset! mouse-over-text true) (check-mouse-position)))
                                 (.addEventListener @text-ref "mouseleave" (fn [] (reset! mouse-over-text false) (check-mouse-position))))
               handle-controls-ref (fn [ref]
                                     (reset! controls-ref ref)
                                     (.addEventListener @controls-ref "mouseenter" (fn [] (reset! mouse-over-controls true) (check-mouse-position)))
                                     (.addEventListener @controls-ref "mouseleave" (fn [] (reset! mouse-over-controls false) (check-mouse-position))))]
    (let [concept? (= type :concept-phrase)]
      [:div (cond-> {:ref        #(when (some? %) (handle-text-ref %))
                     :class-name (get-class-name (merge class-name
                                                        {"action-unit"  true
                                                         "concept-unit" concept?}))}
                    concept? (assoc :title (str "Concept «" concept-name "»")))
       [target-control {:value character
                        :path  path
                        :type  type}]
       [text-control {:value text
                      :path  path
                      :type  type}]
       (when @show-controls?
         [side-controls {:action-data {:type      type
                                       :node-data node-data}
                         :ref         #(when (some? %) (handle-controls-ref %))}])])))

(defn- effect-unit
  [{:keys [effect class-name]}]
  (let [effect-name (clojure.string/replace effect "-" " ")]
    [:div {:class-name (get-class-name (merge class-name
                                              {"action-unit" true
                                               "effect-unit" true}))}
     [icon {:icon       "effect"
            :class-name "effect-icon"}]
     effect-name]))

(defn action-unit
  [{:keys [type parallel-mark] :as props}]
  (let [class-name {"parallel"        (not= parallel-mark :none)
                    "parallel-start"  (= parallel-mark :start)
                    "parallel-middle" (= parallel-mark :middle)
                    "parallel-end"    (= parallel-mark :end)}
        unit-props (merge props
                          {:class-name class-name})]
    (cond
      (= type :effect) [effect-unit unit-props]
      :else [phrase-unit unit-props])))
