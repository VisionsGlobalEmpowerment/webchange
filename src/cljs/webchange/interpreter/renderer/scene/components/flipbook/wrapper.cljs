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

(defn- get-page-name
  [state index]
  (let [pages-names (->> (:pages state)
                         (map :object-name)
                         (map clojure.core/name))]
    (when (and (> index -1)
               (< index (count pages-names)))
      (nth pages-names index))))

(defn- spread-numbers->names
  [state {:keys [left right]}]
  {:left  (get-page-name state left)
   :right (get-page-name state right)})

(defn- flip
  [state direction on-end]
  (let [current-spread (get @state :current-spread)
        next-spread (case direction
                      "forward" {:left  (+ (get current-spread :left) 2)
                                 :right (+ (get current-spread :right) 2)}
                      "backward" {:left  (- (get current-spread :left) 2)
                                  :right (- (get current-spread :right) 2)})]
    (utils/flip-page {:direction       direction
                      :current-spread  (spread-numbers->names @state current-spread)
                      :next-spread     (spread-numbers->names @state next-spread)
                      :page-dimensions {:left-page-position  (get-left-page-position @state)
                                        :right-page-position (get-right-page-position @state)
                                        :page-size           (get-page-size @state)}
                      :on-end          (fn []
                                         (swap! state assoc :current-spread next-spread)
                                         (on-end))})))

(defn wrap
  [type name container state]
  (create-wrapper {:name          name
                   :type          type
                   :object        container
                   :container     container
                   :init          (fn []
                                    (let [first-page-index 0
                                          first-page (keyword (get-page-name @state first-page-index))]
                                      (swap! state assoc :current-spread {:left  (dec first-page-index)
                                                                          :right first-page-index})
                                      (utils/set-position first-page (get-right-page-position @state))
                                      (utils/set-visibility first-page true)))
                   :flip-forward  (fn [{:keys [on-end]}]
                                    (flip state "forward" on-end))
                   :flip-backward (fn [{:keys [on-end]}]
                                    (flip state "backward" on-end))}))
