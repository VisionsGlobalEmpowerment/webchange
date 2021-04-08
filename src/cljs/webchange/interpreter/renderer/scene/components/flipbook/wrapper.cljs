(ns webchange.interpreter.renderer.scene.components.flipbook.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.flipbook.utils :as utils]
    [webchange.interpreter.renderer.scene.components.flipbook.utils-page-number :as page-number-utils]
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
  (<= right 0))

(defn- last-spread?
  [state {:keys [right]}]
  (or (>= right (-> (:pages state) (count) (dec)))
      (not (some? right))))

(defn- sequence-for
  ([action-name on-end]
   (cond-> {:type "sequence-data" :data []}
           (some? action-name)
           (update :data conj {:type "action" :id action-name})

           :always
           (update :data conj {:type "callback" :callback on-end})))
  ([left right on-end]
   (cond-> {:type "sequence-data" :data []}
           (some? left)
           (update :data conj {:type "action" :id left})

           (and (some? left) (some? right))
           (update :data conj {:type "empty" :duration 1000})

           (some? right)
           (update :data conj {:type "action" :id right})

           :always
           (update :data conj {:type "callback" :callback on-end}))))

(defn- flip
  [state direction on-end read]
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
    (page-number-utils/hide-pages-numbers)
    (if (some? next-spread)
      (let [next-spread-objects (spread-numbers->object-names @state next-spread)]
        (utils/set-visibility prev-control false)
        (utils/set-visibility next-control false)
        (utils/flip-page {:direction       direction
                          :current-spread  (spread-numbers->object-names @state current-spread)
                          :next-spread     next-spread-objects
                          :page-dimensions {:left-page-position  (get-left-page-position @state)
                                            :right-page-position (get-right-page-position @state)
                                            :page-size           (get-page-size @state)}
                          :on-end          (fn []
                                             (swap! state assoc :current-spread next-spread)
                                             (page-number-utils/set-pages-numbers (->> (keys next-spread)
                                                                                       (filter #(get next-spread-objects %))
                                                                                       (select-keys next-spread)))
                                             (page-number-utils/show-pages-numbers)
                                             (utils/set-visibility prev-control (not (first-spread? @state next-spread)))
                                             (utils/set-visibility next-control (not (last-spread? @state next-spread)))
                                             (if read
                                               (let [{:keys [left right]} (spread-numbers->action-names @state next-spread)
                                                     action (sequence-for left right on-end)]
                                                 (utils/execute-action action))
                                               (on-end)))}))
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
                   :init          (fn [{:keys [on-end read]}]
                                    (let [first-page-index 0
                                          first-page (keyword (get-page-object-name @state first-page-index))
                                          first-action (get-page-action-name @state first-page-index)
                                          prev-control (keyword (:prev-control @state))]
                                      (swap! state assoc :current-spread {:left  (dec first-page-index)
                                                                          :right first-page-index})
                                      (utils/set-position first-page (get-right-page-position @state))
                                      (utils/set-visibility first-page true)
                                      (utils/set-visibility prev-control false)
                                      (if read
                                        (let [action (sequence-for first-action on-end)]
                                          (utils/execute-action action))
                                        (on-end))))
                   :flip-forward  (fn [{:keys [on-end read]}]
                                    (flip state "forward" on-end read))
                   :flip-backward (fn [{:keys [on-end read]}]
                                    (flip state "backward" on-end read))
                   :show-spread   (fn [spread-idx]
                                    (let [current-spread (:current-spread @state)
                                          new-spread {:left  (->> (* spread-idx 2) (dec))
                                                      :right (->> (* spread-idx 2))}]
                                      (hide-spread @state current-spread)
                                      (show-spread @state new-spread)
                                      (swap! state assoc :current-spread new-spread)))
                   :read-left     (fn [on-end]
                                    (let [current-spread (get @state :current-spread)
                                          {:keys [left]} (spread-numbers->action-names @state current-spread)
                                          action (sequence-for left on-end)]
                                      (utils/execute-action action)))
                   :read-right    (fn [on-end]
                                    (let [current-spread (get @state :current-spread)
                                          {:keys [right]} (spread-numbers->action-names @state current-spread)
                                          action (sequence-for right on-end)]
                                      (utils/execute-action action)))}))
