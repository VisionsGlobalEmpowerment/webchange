(ns webchange.interpreter.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [webchange.events :as events]
    [react-spring :refer [Spring]]
    [react-konva :refer [Stage, Layer, Group, Rect]]
    [konva :refer [Tween]]
    [webchange.common.anim :as anim]
    [webchange.common.events :as ce]
    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]]
    ["gsap/umd/TweenMax" :refer [TweenMax SlowMo]]
    [webchange.interpreter.defaults :as defaults]
    [webchange.interpreter.renderer.scene.components.wrapper-interface :as w]))

(def default-assets defaults/default-assets)

(def assets (atom {}))

(defn put-data
  [data key]
  (swap! assets assoc (name key) data))

(defn get-data
  [key]
  (get @assets key))

(defn has-data
  [key]
  (contains? @assets key))

(defn create-tagged-key
  [tag key]
  (str tag "_" key))

(defn new-blob
  [arraybuffer]
  (js/Blob. [arraybuffer]))

(defn get-data-as-blob [key]
  (-> key
      get-data
      new-blob))

(defn wait-data-as-blob
  [key callback]
  (let [timeout 100
        data (get-data key)]
    (if-not (nil? data)
      (callback (new-blob data))
      (.setTimeout js/window #(wait-data-as-blob key callback) timeout))))

(def host "/api")
(def resources "")
(def http-buffer (atom {}))

(defn get-url [url]
  (if (contains? @http-buffer url)
    (let [response (get @http-buffer url)]
      (swap! http-buffer dissoc url)
      response)
    (http/get url {:with-credentials? false})))

(defn course-url
  [course-id]
  (str host "/courses/" course-id))

(defn get-course
  [course-id]
  (let [url (course-url course-id)]
    (get-url url)))

(defn scene-url
  [course-id scene-id]
  (str host "/courses/" course-id "/scenes/" scene-id))

(defn get-scene
  [course-id scene-id]
  (let [url (scene-url course-id scene-id)]
    (get-url url)))

(defn progress-url
  [course-id]
  (str host "/courses/" course-id "/current-progress"))

(defn get-progress
  [course-id]
  (let [url (progress-url course-id)]
    (get-url url)))

(defn lessons-url
  [course-id]
  (str host "/courses/" course-id "/lesson-sets"))

(defn get-lessons
  [course-id]
  (let [url (lessons-url course-id)]
    (get-url url)))

(defn get-total-size
  [assets]
  (->> assets
       (filter :url)
       (map :size)
       (reduce +)))

(defn load-base-asset
  ([asset]
   (load-base-asset asset nil))
  ([asset progress]
   (when (:url asset)
     (go (let [response (<! (http/get (str resources (:url asset)) {:response-type :array-buffer :with-credentials? false}))]
           (put-data (:body response) (:url asset))
           (when-not (nil? progress)
             (swap! progress + (:size asset))))))))

(defn load-asset
  ([asset]
   (let [progress (atom 0)]
     (load-asset asset progress)))
  ([asset progress]
   (case (-> asset :type keyword)
     :anim-text (anim/load-anim-text asset progress)
     :animation (anim/load-anim-text asset progress)
     :anim-texture (anim/load-anim-texture asset progress)
     (load-base-asset asset progress))))

(defn load-assets
  ([assets]
   (load-assets assets #() #()))
  ([assets on-asset-progress on-asset-complete]
   (let [total (get-total-size assets)
         current-progress (atom 0)]
     (add-watch current-progress :inc
                (fn [_ _ _ n]
                  (on-asset-progress (Math/round (* n (/ 100 total))))
                  (if (>= n total)
                    (on-asset-complete))))
     (if (> total 0)
       (doseq [asset assets]
         (load-asset asset current-progress))
       (on-asset-complete)))))

(defn load-course
  [{:keys [course-id load-assets?]} cb]
  (go (let [course-response (<! (get-course course-id))
            course (:body course-response)]
        (when load-assets?
          (load-assets (->> course :templates vals (map :assets) (apply concat))))
        (cb course))))

(defn load-scene
  [{:keys [course-id scene-id load-assets?]} cb]
  (go (let [scene-response (<! (get-scene course-id scene-id))
            scene (:body scene-response)]
        (when load-assets?
          (load-assets (concat default-assets (:assets scene))
                       #(re-frame/dispatch [::events/set-loading-progress scene-id %])
                       #(re-frame/dispatch [::events/set-scene-loaded scene-id true])))
        (cb scene))))

(defn load-progress
  [course-id cb]
  (go (let [response (<! (get-progress course-id))
            result (-> response :body :progress)]
        (cb result))))

(defn load-lessons
  [course-id load-assets? cb on-asset-progress on-asset-complete]
  (go (let [response (<! (get-lessons course-id))
            result (-> response :body)]
        (if load-assets?
          (load-assets (:assets result) on-asset-progress on-asset-complete)
          (on-asset-complete))
        (cb result))))

(defn get-data-as-url
  [key]
  (if-not (has-data key)
    (do (load-base-asset {:url key}) key)
    (let [object-url-key (create-tagged-key "object-url" key)]
      (when-not (has-data object-url-key)
        (-> key
            get-data-as-blob
            js/URL.createObjectURL
            (put-data object-url-key)))
      (get-data object-url-key))))

(defn length
  [cx cy x y]
  (Math/sqrt (+ (Math/pow (- cx x) 2) (Math/pow (- cy y) 2))))

(defn transition-duration
  [component {:keys [x y]} {:keys [duration speed]}]
  (let [position (w/get-position component)
        cx (:x position)
        cy (:y position)]
    (cond
      (> duration 0) duration
      (> speed 0) (/ (length cx cy x y) speed)
      :else (/ (length cx cy x y) 100))))

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

(defn- get-tween-object-params
  [object params]
  (let [params-to-animate (if (contains? params :bezier)
                            (get-in params [:bezier 0])
                            params)]
    (-> (cond-> {}
                (or (contains? params-to-animate :x)
                    (contains? params-to-animate :y)) (merge (w/get-position object))
                (contains? params-to-animate :brightness) (assoc :brightness (w/get-filter-value object "brightness"))
                (contains? params-to-animate :rotation) (assoc :rotation (w/get-rotation object))
                (contains? params-to-animate :opacity) (assoc :opacity (w/get-opacity object)))
        (clj->js))))

(defn- set-tween-object-params
  [object params]
  (when (or (contains? params :x)
            (contains? params :y))
    (w/set-position object (select-keys params [:x :y])))
  (when (contains? params :brightness)
    (w/set-filter-value object "brightness" (:brightness params)))
  (when (contains? params :rotation)
    (w/set-rotation object (:rotation params)))
  (when (contains? params :opacity)
    (w/set-opacity object (:opacity params))))

;; ToDo: Test :loop param
;; ToDo: Test :skippable param
;; ToDo: Implement :ease param
(defn interpolate
  [{:keys [id component to from params on-ended skippable]}]

  (when from
    (set-tween-object-params @component from))

  (let [container (get-tween-object-params @component to)
        duration (transition-duration @component to params)
        ease-params (or (:ease params) [1 1])
        vars (cond-> (-> to
                         (merge params)
                         (assoc :ease (apply SlowMo.ease.config (conj ease-params false)))
                         (assoc :onUpdate (fn [] (set-tween-object-params @component (js->clj container :keywordize-keys true))))
                         (assoc :onComplete (if (:loop params)
                                              (fn [] (this-as t (.restart t)))
                                              (fn []
                                                (on-ended)
                                                (this-as t (.kill t))))))
                     (:yoyo params) (merge {:yoyo   true
                                            :repeat -1}))
        tween (TweenMax.to container duration (clj->js vars))]

    (when skippable
      (ce/on-skip! #(.progress tween 1)))

    (register-transition! id #(.kill tween))))

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

(defn animation-sequence->actions [{audio-start :start :keys [target track data skippable] :or {track 1}}]
  (into [] (map (fn [{:keys [start end anim]}]
                  {:type      "sequence-data"
                   :data      [{:type "empty" :duration (* (- start audio-start) 1000)}
                               {:type "animation" :target target :track track :id anim}
                               {:type "empty" :duration (* (- end start) 1000)}
                               {:type "remove-animation" :target target :track track}]
                   :skippable skippable
                   :on-skip   #(re-frame/dispatch [::ce/execute-action {:type "remove-animation" :target target :track track}])})
                data)))

(defn animation-sequence->audio-action [{:keys [start duration audio] :as action}]
  (if audio
    {:type     "audio"
     :id       audio
     :start    start
     :duration duration}))

(defn chunk-transition-name [name index]
  (if index (str "chunk-" name "-" index)))

(defn text-animation-sequence->actions [{:keys [target animation start data] :as action}]
  (into [] (map (fn [{:keys [at chunk]}]
                  {:type "sequence-data"
                   :data [{:type "empty" :duration (* (- at start) 1000)}
                          {:type "transition" :transition-id (chunk-transition-name target chunk) :to {:y -20 :duration 0.01}}
                          {:type "transition" :transition-id (chunk-transition-name target chunk) :to {:y 0 :duration 0.1}}]})
                data)))

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
