(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.views-menu :refer [add-concept-action add-scene-action unit-menu]]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.utils :refer [get-effect-name]]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state]
    [webchange.ui-framework.components.index :refer [icon menu]]
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
  [{:keys [action-data]}]
  (let [ref (atom nil)
        handle-key-down (fn [e]
                          (when (= (.-key e) "Enter")
                            (.preventDefault e)
                            (if (.-ctrlKey e)
                              (add-concept-action {:action-data action-data})
                              (add-scene-action {:action-data action-data}))))]
    (r/create-class
      {:display-name "text-control"
       :should-component-update
                     (constantly false)
       :component-did-mount
                     (fn []
                       (.addEventListener @ref "keydown" handle-key-down false))
       :component-will-unmount
                     (fn []
                       (.removeEventListener @ref "keydown" handle-key-down))
       :reagent-render
                     (fn [{:keys [path type value]}]
                       (let [handle-change (fn [event]
                                             (let [new-value (.. event -target -innerText)]
                                               (re-frame/dispatch [::state/set-phrase-action-text path type new-value])))]
                         [:span {:class-name                        "text"
                                 :on-input                          handle-change
                                 :ref                               #(when (some? %) (reset! ref %))
                                 :content-editable                  true
                                 :suppress-content-editable-warning true}
                          value]))})))

(defn- phrase-unit
  [{:keys [text character path type concept-name action-data]}]
  (let [concept? (= type :concept-phrase)]
    [:div (cond-> {:class-name (get-class-name {"phrase-unit"  true
                                                "concept-unit" concept?})}
                  concept? (assoc :title (str "Concept «" concept-name "»")))
     [target-control {:value character
                      :path  path
                      :type  type}]
     [text-control {:value       text
                    :path        path
                    :type        type
                    :action-data action-data}]]))

(defn- effect-unit
  [{:keys [effect class-name]}]
  (let [effect-name (get-effect-name effect)]
    [:div {:class-name (get-class-name (merge class-name
                                              {"effect-unit" true}))}
     [icon {:icon       "effect"
            :class-name "effect-icon"}]
     effect-name]))

(defn action-unit
  [{:keys [idx node-data parallel-mark path type] :as props}]
  (r/with-let [container-ref (r/atom nil)]
    (let [action-data {:type      type
                       :path      path
                       :node-data node-data}]
      [:div {:ref        #(when (some? %) (reset! container-ref %))
             :class-name (get-class-name {"action-unit"     true
                                          "parallel"        (not= parallel-mark :none)
                                          "parallel-start"  (= parallel-mark :start)
                                          "parallel-middle" (= parallel-mark :middle)
                                          "parallel-end"    (= parallel-mark :end)})}
       (cond
         (= type :effect) [effect-unit props]
         :else [phrase-unit (merge props
                                   {:action-data action-data})])
       [unit-menu {:idx         idx
                   :parent-ref  @container-ref
                   :action-data action-data}]])))
