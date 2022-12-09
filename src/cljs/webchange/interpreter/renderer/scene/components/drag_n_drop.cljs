(ns webchange.interpreter.renderer.scene.components.drag-n-drop
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.scene :as state]
    [webchange.interpreter.renderer.scene.components.dragging :as dragging]
    [webchange.interpreter.renderer.scene.components.collisions :as collisions]))

(defn- init-state
  [state object]
  (let [init-position {:x (.. object -position -x)
                       :y (.. object -position -y)}]
    (swap! state assoc
           :init-position init-position
           :collisions-enabled? true)))

(defn- add-collision
  [state target]
  (swap! state update :collisions concat [target])
  (when (:collisions-enabled? @state)
    (when-let [first-collision (-> @state :collisions first)]
      (re-frame/dispatch [::state/set-scene-object-state first-collision {:highlight true}]))))

(defn- remove-collision
  [state target]
  (swap! state update :collisions #(remove (fn [existing-collision] (= existing-collision target)) %))
  (when-let [first-collision (-> @state :collisions first)]
    (re-frame/dispatch [::state/set-scene-object-state first-collision {:highlight true}]))
  (re-frame/dispatch [::state/set-scene-object-state target {:highlight false}])) 

(defn- on-drag-end
  [state object {:keys [drag-n-drop on-drop object-name]}]
  (swap! state assoc :collisions-enabled? false)
  (if-let [first-collision (-> @state :collisions first)]
    (let [placeholders (:placeholders drag-n-drop)
          params (->> placeholders (filter #(= (:id %) (name first-collision))) first)]
      (when (fn? on-drop)
        (on-drop (merge {:placeholder params
                         :object object-name})))
      (re-frame/dispatch [::state/set-scene-object-state first-collision {:highlight false}]))
    (do (set! (.. object -position -x) (-> @state :init-position :x))
        (set! (.. object -position -y) (-> @state :init-position :y)))))

(defn enable-drag-n-drop!
  [object {:keys [drag-n-drop] :as props}]
  (let [state (atom {:collisions []})]
    (dragging/enable-drag! object {:on-drag-start #(init-state state object)
                                   :on-drag-end #(on-drag-end state object props)})
    (collisions/enable-collisions! object (merge props
                                                 {:on-collide-enter {:handler #(add-collision state (-> % :target keyword))
                                                                     :type "bounds"
                                                                     :test (map :id (:placeholders drag-n-drop))}
                                                  :on-collide-leave {:handler #(remove-collision state (-> % :target keyword))
                                                                     :test (map :id (:placeholders drag-n-drop))}}))))
