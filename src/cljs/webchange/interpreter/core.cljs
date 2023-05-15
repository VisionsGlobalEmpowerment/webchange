(ns webchange.interpreter.core
  (:require
    ["gsap/umd/TweenMax" :refer [TweenMax SlowMo]]
    [webchange.interpreter.renderer.scene.components.wrapper-interface :as w]
    [webchange.interpreter.renderer.scene.components.text.chunks :refer [chunk-transition-name chunk-animated-variable]]
    [webchange.logger.index :as logger]
    [webchange.utils.scene-action-data :as scene-action-data]))

(def host "/api")
(def http-buffer (atom {}))

(defn course-url
  [course-id]
  (str host "/courses/" course-id))

(defn scene-url
  [_course-id scene-id]
  (str host "/activities/" scene-id "/current-version"))

(defn progress-url
  [course-id]
  (str host "/courses/" course-id "/current-progress"))

(defn lessons-url
  [course-id]
  (str host "/courses/" course-id "/lesson-sets"))

(defn- length
  [p1 p2]
  (Math/sqrt (+ (Math/pow (- (:x p1) (:x p2)) 2)
                (Math/pow (- (:y p1) (:y p2)) 2))))

(defn- bezier-length
  [p1 p2 p3 p4]
  (->> [[p1 p2] [p2 p3] [p3 p4]]
       (map (fn [points-pair] (apply length points-pair)))
       (apply +)))

(defn transition-duration
  [component {:keys [x y bezier]} {:keys [duration speed]}]
  (let [position (w/get-position component)
        cx (:x position)
        cy (:y position)
        bezier-values (or (:values bezier) bezier)]
    (cond
      (> duration 0) duration
      (> speed 0) (if (some? bezier-values)
                    (/ (apply bezier-length (concat [{:x cx :y cy}] bezier-values)) speed)
                    (/ (length {:x cx :y cy} {:x x :y y}) speed))
      :else (/ (length {:x cx :y cy} {:x x :y y}) 100))))

;(defn ->easing
;  [easing]
;  (case easing
;    "ease-in" k/Easings.EaseIn
;    "ease-in-out" k/Easings.EaseInOut
;    "ease-out" k/Easings.EaseOut
;    "strong-ease-in" k/Easings.EaseIn
;    "strong-ease-in-out" k/Easings.EaseInOut
;    "strong-ease-out" k/Easings.EaseOut
;    "linear" k/Easings.Linear
;    k/Easings.Linear))

(defonce transitions (atom {}))

(defn register-transition!
  [id on-kill]
  (swap! transitions assoc id on-kill))

(defn kill-transition!
  [id]
  (let [on-kill (get @transitions id)]
    (when on-kill
      (on-kill))))

(defn kill-transitions!
  []
  (doseq [on-kill (vals @transitions)]
    (when on-kill (on-kill))))

(defn- get-tween-object-params
  [object params]
  (let [params-to-animate (if (contains? params :bezier)
                            (get-in params [:bezier 0])
                            params)
        params-handled-manually [:x :y :brightness :rotation :opacity]
        rest-params (->> (keys params-to-animate)
                         (filter (fn [param] (->> params-handled-manually (some #{param}) (not)))))]
    (-> (cond-> (reduce (fn [result param]
                          (if (some #{param} (w/get-wrapped-props object))
                            (assoc result param (w/get-prop object param))
                            result))
                        {}
                        rest-params)
                (or (contains? params-to-animate :x)
                    (contains? params-to-animate :y)) (merge (w/get-position object))
                (contains? params-to-animate :brightness) (assoc :brightness (w/get-filter-value object "brightness"))
                (contains? params-to-animate :hue) (assoc :hue (w/get-filter-value object "hue"))
                (contains? params-to-animate :glow) (assoc :glow (w/get-filter-value object "glow"))
                (contains? params-to-animate :rotation) (assoc :rotation (w/get-rotation object))
                (contains? params-to-animate :opacity) (assoc :opacity (w/get-opacity object))
                (contains? params-to-animate :fill) (assoc :fill (w/get-fill object)))
        (clj->js))))

(defn- set-tween-object-params
  [object params]
  (let [params-handled-manually [:x :y :brightness :rotation :opacity :fill]
        rest-params (->> (keys params)
                         (filter (fn [param] (->> params-handled-manually (some #{param}) (not)))))]
    (when (or (contains? params :x)
              (contains? params :y))
      (w/set-position object (select-keys params [:x :y])))
    (when (contains? params :brightness)
      (w/set-filter-value object "brightness" (:brightness params)))
    (when (contains? params :hue)
      (w/set-filter-value object "hue" (:hue params)))
    (when (contains? params :glow)
      (w/set-filter-value object "glow" (:glow params)))
    (when (contains? params :rotation)
      (w/set-rotation object (:rotation params)))
    (when (contains? params :opacity)
      (w/set-opacity object (:opacity params)))
    (when (contains? params :fill)
      (w/set-fill object (:fill params)))

    (doseq [param rest-params]
      (when (some #{param} (w/get-wrapped-props object))
        (w/set-prop object param (get params param))))))

;; ToDo: Test :loop param
;; ToDo: Test :skippable param
;; ToDo: Implement :ease param
(defn interpolate
  [{:keys [id component to from params on-ended skippable kill-after] :as props}]
  (logger/trace-folded "interpolate" props)
  (when from
    (set-tween-object-params component from))

  (let [position (w/get-position component)
        to (cond-> to
                   (contains? to :offset-x) (assoc :x (+ (:offset-x to) (:x position)))
                   (contains? to :offset-y) (assoc :y (+ (:offset-y to) (:y position)))
                   (:init-position to) (merge (w/get-init-position component))
                   (:init-position to) (dissoc :init-position))

        container (get-tween-object-params component to)
        duration (transition-duration component to params)
        ease-params (or (:ease params) [1 1])
        vars (cond-> (-> to
                         (merge params)
                         (assoc :ease (apply SlowMo.ease.config (conj ease-params false)))
                         (assoc :onUpdate (fn [] (set-tween-object-params component (js->clj container :keywordize-keys true))))
                         (assoc :onComplete (if (:loop params)
                                              (fn [] (this-as t (.restart t)))
                                              (fn []
                                                (when on-ended
                                                  (on-ended))
                                                (this-as t (.kill t))))))
                     (:yoyo params) (merge {:yoyo   true
                                            :repeat (or (:repeat params) -1)}))
        tween (TweenMax.to container duration (clj->js vars))]
    (register-transition! id #(.kill tween))
    (when kill-after
      (js/setTimeout #(kill-transition! id) kill-after))))

(defn collide?
  [display-object1 display-object2]
  (let [r1 (.getBounds display-object1)
        r2 (.getBounds display-object2)
        r1x (.-x r1)
        r1y (.-y r1)
        r1width (.-width r1)
        r1height (.-height r1)
        r2x (.-x r2)
        r2y (.-y r2)
        r2width (.-width r2)
        r2height (.-height r2)]
    (not (or (> r2x (+ r1x r1width))
             (< (+ r2x r2width) r1x)
             (> r2y (+ r1y r1height))
             (< (+ r2y r2height) r1y)))))

(defn collide-with-coords?
  [display-object1 coords]
  (let [r1 (.getBounds display-object1)
        r1x (.-x r1)
        r1y (.-y r1)
        r1width (.-width r1)
        r1height (.-height r1)
        r2x (:x coords)
        r2y (:y coords)]
    (and (< r2x (+ r1x r1width))
         (> r2x r1x)
         (< r2y (+ r1y r1height))
         (> r2y r1y))))

(defn- merge-animations
  "Try to merge talk animations when there is no silence between.
  E.g (:next start) is the same as (:end current)"
  [data]
  (let [continued? (fn [current next]
                     (< (- (:start next) (:end current)) 0.1))
        merge-chunks (fn [current next]
                       (assoc current :end (:end next)))]
    (if (empty? data)
      data
      (loop [result []
             current (first data)
             tail (rest data)]
        (let [next (first tail)]
          (if (nil? next)
            (concat result [current])
            (if (continued? current next)
              (recur result
                     (merge-chunks current next)
                     (rest tail))
              (recur (concat result [current])
                     next
                     (rest tail)))))))))

(defn animation-sequence->actions [{audio-start :start :keys [target track data skippable start end]}]
  (let [track (or track (get scene-action-data/animation-tracks :mouth))
        with-default (fn [data] (if (empty? data)
                                  [{:start start
                                    :end end
                                    :anim "talk"}]
                                  data))]
    (->> data
         (merge-animations)
         (remove (fn [{chunk-end :end chunk-start :start}]
                   (or
                    (<= chunk-end start)
                    (>= chunk-start end))))
         (with-default)
         (map (fn [{:keys [start end anim]}]
                {:type         "sequence-data"
                 :data         [{:type "empty" :duration (* (- start audio-start) 1000)}
                                {:type "animation" :target target :track track :id anim}
                                {:type "empty" :duration (* (- end start) 1000)}
                                {:type "remove-animation" :target target :track track}]
                 :on-interrupt {:type "remove-animation" :target target :track track}
                 :skippable    skippable}))
         (into []))))

(defn animation-sequence->audio-action [{:keys [audio] :as action}]
  (if audio
    (merge {:type "audio"
            :id   audio}
           (select-keys action [:duration :start :volume]))))

(defn text-animation-sequence->actions [db {:keys [target animation start duration data fill] :or {fill 45823}}]
  (let [end (+ start duration)]
    (->> data
         (remove (fn [{chunk-end :end chunk-start :at}]
                   (or
                     (<= chunk-end start)
                     (>= chunk-start end))))
         (filter (fn [{:keys [chunk]}]
                   (let [transition-id (chunk-transition-name target chunk)
                         scene-id (:current-scene db)]
                     (get-in db [:transitions scene-id transition-id]))))
         (map (fn [{:keys [at chunk duration]}]
                (let [transition-id (chunk-transition-name target chunk)
                      scene-id (:current-scene db)
                      transition (get-in db [:transitions scene-id transition-id])
                      start-fill ((:get-fill @transition))]
                  {:type         "sequence-data"
                   :data         [{:type "empty" :duration (* (- at start) 1000)}
                                  {:type "transition" :transition-id transition-id :to {:fill fill :duration 0.01}}
                                  {:type "empty" :duration (* duration 1000)}
                                  {:type "transition" :transition-id transition-id :to {:fill start-fill :duration 0.1}}]
                   :on-interrupt {:type "transition" :transition-id transition-id :to {:fill start-fill :duration 0.001}}})))
         (into []))))

(defn find-nav-path
  [from to graph]
  (let [visited (atom #{from})]
    (loop [[head & tail] [[from]]]
      (if head
        (let [node-name (last head)
              node (get graph (keyword node-name))
              sibling-names (->> node :links (into #{}))
              non-visited (clojure.set/difference sibling-names @visited)
              _ (swap! visited #(clojure.set/union % non-visited))
              new-paths (map (fn [next-node] (conj head next-node)) non-visited)]
          (if (= node-name to)
            head
            (recur (concat tail new-paths))))))))

(defn nav-node-exists?
  [graph node-name]
  (contains? graph (keyword node-name)))
