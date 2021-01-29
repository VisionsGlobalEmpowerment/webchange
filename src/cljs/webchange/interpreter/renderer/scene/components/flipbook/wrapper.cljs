(ns webchange.interpreter.renderer.scene.components.flipbook.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.flipbook.utils :as utils]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn- get-left-page-position
  [_]
  {:x 0 :y 0})

(defn- get-right-page-position
  [state]
  {:x (/ (:width state) 2) :y 0})

(defn- get-page-size
  [state]
  {:width  (/ (:width state) 2)
   :height (:height state)})

(defn- get-page-object-name
  [state index]
  (let [pages-names (->> (:pages state)
                         (map first)
                         (map :object-name)
                         (map clojure.core/name))]
    (when (and (some? index)
               (> index -1)
               (< index (count pages-names)))
      (nth pages-names index))))

(defn- get-page-action-name
  [state index]
  (let [actions-names (->> (:pages state)
                           (map second))]
    (when (and (> index -1)
               (< index (count actions-names)))
      (nth actions-names index))))

(defn- spread-numbers->object-names
  [state {:keys [left right]}]
  {:left  (get-page-object-name state left)
   :right (get-page-object-name state right)})

(defn- spread-numbers->action-names
  [state {:keys [left right]}]
  {:left  (get-page-action-name state left)
   :right (get-page-action-name state right)})

(defn- first-spread?
  [_ {:keys [right]}]
  (= right 0))

(defn- last-spread?
  [state {:keys [right]}]
  (or (= right (-> (:pages state) (count) (dec)))
      (not (some? right))))

(defn- flip
  [state direction on-end]
  (let [prev-control (-> @state :prev-control keyword)
        next-control (-> @state :next-control keyword)
        current-spread (get @state :current-spread)
        next-spread (case direction
                      "forward" (when-not (last-spread? @state current-spread)
                                  {:left  (+ (get current-spread :left) 2)
                                   :right (+ (get current-spread :right) 2)})
                      "backward" (when-not (first-spread? @state current-spread)
                                   {:left  (- (get current-spread :left) 2)
                                    :right (- (get current-spread :right) 2)}))]
    (if (some? next-spread)
      (do (utils/set-visibility prev-control false)
          (utils/set-visibility next-control false)
          (utils/flip-page {:direction       direction
                            :current-spread  (spread-numbers->object-names @state current-spread)
                            :next-spread     (spread-numbers->object-names @state next-spread)
                            :page-dimensions {:left-page-position  (get-left-page-position @state)
                                              :right-page-position (get-right-page-position @state)
                                              :page-size           (get-page-size @state)}
                            :on-end          (fn []
                                               (swap! state assoc :current-spread next-spread)
                                               (utils/set-visibility prev-control (not (first-spread? @state next-spread)))
                                               (utils/set-visibility next-control (not (last-spread? @state next-spread)))
                                               (let [{:keys [left right]} (spread-numbers->action-names @state next-spread)
                                                     action (cond-> {:type "sequence"
                                                                     :data []}
                                                                    (some? left) (update :data conj left)
                                                                    (some? right) (update :data conj right))]
                                                 (utils/execute-action action))
                                               (on-end))}))
      (on-end))))

(defn- show-spread
  [state spread]
  (let [{:keys [left right]} (spread-numbers->object-names state spread)]
    (when (some? left)
      (utils/set-position (keyword left) (get-left-page-position state))
      (utils/set-visibility (keyword left) true))
    (when (some? right)
      (utils/set-position (keyword right) (get-right-page-position state))
      (utils/set-visibility (keyword right) true))))

(defn- hide-spread
  [state spread]
  (let [{:keys [left right]} (spread-numbers->object-names state spread)]
    (when (some? left) (utils/set-visibility (keyword left) false))
    (when (some? right) (utils/set-visibility (keyword right) false))))

(defn wrap
  [type name container state]
  (create-wrapper {:name          name
                   :type          type
                   :object        container
                   :container     container
                   :init          (fn []
                                    (let [first-page-index 0
                                          first-page (keyword (get-page-object-name @state first-page-index))
                                          first-action (get-page-action-name @state first-page-index)
                                          prev-control (keyword (:prev-control @state))]
                                      (swap! state assoc :current-spread {:left  (dec first-page-index)
                                                                          :right first-page-index})
                                      (utils/set-position first-page (get-right-page-position @state))
                                      (utils/set-visibility first-page true)
                                      (utils/set-visibility prev-control false)
                                      (when (some? first-action)
                                        (utils/execute-action {:type "action"
                                                               :id   first-action}))))
                   :flip-forward  (fn [{:keys [on-end]}]
                                    (flip state "forward" on-end))
                   :flip-backward (fn [{:keys [on-end]}]
                                    (flip state "backward" on-end))
                   :show-spread   (fn [spread-idx]
                                    (let [current-spread (:current-spread @state)
                                          new-spread {:left  (->> (* spread-idx 2) (dec))
                                                      :right (->> (* spread-idx 2))}]
                                      (show-spread @state new-spread)
                                      (hide-spread @state current-spread)
                                      (swap! state assoc :current-spread new-spread)))}))
