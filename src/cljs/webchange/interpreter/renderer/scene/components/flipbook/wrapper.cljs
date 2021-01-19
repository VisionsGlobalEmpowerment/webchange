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
               (< (count pages-names)))
      (nth pages-names index))))

(defn- flip
  [state direction on-end]
  (let [get-page (partial get-page-name @state)]
    (utils/flip-page (cond-> {:direction           direction
                              :left-page-position  (get-left-page-position @state)
                              :right-page-position (get-right-page-position @state)
                              :page-size           (get-page-size @state)}
                             (= direction "forward") (merge (let [right-page-index (get-in @state [:current-page :right])]
                                                              {:flipped-page               (get-page right-page-index)
                                                               :flipped-page-back          (get-page (+ right-page-index 1))
                                                               :flipped-page-back-neighbor (get-page (+ right-page-index 2))
                                                               :on-end                     (fn []
                                                                                             (swap! state assoc :current-page {:left  (+ right-page-index 1)
                                                                                                                               :right (+ right-page-index 2)})
                                                                                             (on-end))}))
                             (= direction "backward") (merge (let [left-page-index (get-in @state [:current-page :left])]
                                                               {:flipped-page               (get-page left-page-index)
                                                                :flipped-page-back          (get-page (- left-page-index 1))
                                                                :flipped-page-back-neighbor (get-page (- left-page-index 2))
                                                                :on-end                     (fn []
                                                                                              (swap! state assoc :current-page {:left  (- left-page-index 2)
                                                                                                                                :right (- left-page-index 1)})
                                                                                              (on-end))
                                                                }))))))

(defn wrap
  [type name container state]
  (create-wrapper {:name          name
                   :type          type
                   :object        container
                   :container     container
                   :init          (fn []
                                    (let [first-page-index 0
                                          first-page (keyword (get-page-name @state first-page-index))]
                                      (swap! state assoc :current-page {:left nil :right first-page-index})
                                      (utils/set-position first-page (get-right-page-position @state))
                                      (utils/set-visibility first-page true)))
                   :flip-forward  (fn [{:keys [on-end]}]
                                    (flip state "forward" on-end))
                   :flip-backward (fn [{:keys [on-end]}]
                                    (flip state "backward" on-end))}))
