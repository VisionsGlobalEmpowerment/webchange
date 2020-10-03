(ns webchange.interpreter.renderer.state.scene
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.state.db :refer [path-to-db]]
    [webchange.interpreter.renderer.scene.components.wrapper-interface :as w]
    [webchange.logger :as logger]))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_]]
    (let [objects (get-in db (path-to-db [:objects]) (atom {}))
          groups (get-in db (path-to-db [:groups]) (atom {}))]
      (reset! objects {})
      (reset! groups {})
      {:db (-> db
               (assoc-in (path-to-db [:objects]) objects)
               (assoc-in (path-to-db [:groups]) groups))})))

(re-frame/reg-fx
  :add-scene-object
  (fn [{:keys [objects groups object-wrapper]}]
    (let [object-name (:name object-wrapper)
          group-name (:group-name object-wrapper)]
      (when (contains? @objects object-name)
        (-> (str "Object with name " object-name " already exists.") js/Error. throw))
      (swap! objects assoc object-name object-wrapper)
      (when-not (nil? group-name)
        (swap! groups update group-name conj object-name)))))

(re-frame/reg-event-fx
  ::register-object
  (fn [{:keys [db]} [_ object-wrapper]]
    {:add-scene-object {:objects        (get-in db (path-to-db [:objects]))
                        :groups         (get-in db (path-to-db [:groups]))
                        :object-wrapper object-wrapper}}))

(defn get-scene-object
  [db name]
  (if-not (nil? name)
    (let [objects @(get-in db (path-to-db [:objects]))]
      (get objects name))
    nil))

(defn get-scene-group
  [db group-name]
  (let [groups @(get-in db (path-to-db [:groups]))
        group (get groups group-name)]
    (when-not (nil? group)
      (map #(get-scene-object db %) group))))

;; Change object events

(defn- filter-extra-props
  [props extra-props-names]
  (->> props
       (filter (fn [[prop-name]] (not (some #{prop-name} extra-props-names))))
       (into {})))

(re-frame/reg-event-fx
  ::set-scene-object-state
  (fn [{:keys [_]} [_ object-name state]]
    (let [filtered-state (filter-extra-props state [:start :revert :target])
          available-actions {:set-position   [:x :y]
                             :set-scale      [:scale :scale-x :scale-y]
                             :set-visibility [:visible]
                             :set-src        [:src]
                             :set-text       [:text]
                             :set-filter     [:filter :brightness :eager]
                             :set-opacity    [:opacity]
                             :set-tool       [:tool]
                             :set-color      [:color]
                             :set-stroke     [:stroke]
                             :set-data       [:data]
                             :set-path       [:path]
                             :set-font-size  [:font-size]}
          execute-actions (->> available-actions
                               (map (fn [[action params]] [action (select-keys filtered-state params)]))
                               (filter (fn [[_ params]] (-> params empty? not))))
          not-handled-params (clojure.set/difference (->> filtered-state (keys) (set))
                                                     (->> available-actions (vals) (apply concat) (set)))]
      (when-not (empty? not-handled-params)
        (logger/warn (str "[Set object state] Not-processed-params for <" object-name "> object:") (clj->js not-handled-params)))
      {:dispatch [::change-scene-object object-name execute-actions]})))

(re-frame/reg-event-fx
  ::change-scene-object
  (fn [{:keys [db]} [_ object-name actions]]
    (let [wrappers (or (get-scene-object db object-name)
                       (get-scene-group db object-name))]
      (->> actions
           (map (fn [[action params]] [action [wrappers params]]))
           (into {})))))

(defn- apply-to-wrapper
  [method target & params]
  (let [targets (if (sequential? target) target [target])]
    (doseq [wrapper targets]
      (apply method (concat [wrapper] params)))))

(re-frame/reg-fx
  :add-filter
  (fn [[object-wrapper filter-data]]
    (apply-to-wrapper w/add-filter object-wrapper filter-data)))

(re-frame/reg-fx
  :set-filter
  (fn [[object-wrapper {:keys [filter] :as params}]]
    (apply-to-wrapper w/set-filter object-wrapper filter params)))

(re-frame/reg-fx
  :set-position
  (fn [[object-wrapper position]]
    (apply-to-wrapper w/set-position object-wrapper position)))

(re-frame/reg-fx
  :set-scale
  (fn [[object-wrapper scale]]
    (apply-to-wrapper w/set-scale object-wrapper scale)))

(re-frame/reg-fx
  :set-visibility
  (fn [[object-wrapper {:keys [visible]}]]
    (apply-to-wrapper w/set-visibility object-wrapper visible)))

(re-frame/reg-fx
  :set-value
  (fn [[object-wrapper value]]
    (apply-to-wrapper w/set-value object-wrapper value)))

(re-frame/reg-fx
  :set-text
  (fn [[object-wrapper {:keys [text]}]]
    (apply-to-wrapper w/set-text object-wrapper text)))

(re-frame/reg-fx
  :set-font-size
  (fn [[object-wrapper {:keys [font-size]}]]
    (apply-to-wrapper w/set-font-size object-wrapper font-size)))

(re-frame/reg-fx
  :set-src
  (fn [[object-wrapper {:keys [src options]}]]
    (apply-to-wrapper w/set-src object-wrapper src options)))

(re-frame/reg-fx
  :set-opacity
  (fn [[object-wrapper {:keys [opacity]}]]
    (apply-to-wrapper w/set-opacity object-wrapper opacity)))

(re-frame/reg-fx
  :set-tool
  (fn [[object-wrapper {:keys [tool]}]]
    (apply-to-wrapper w/set-tool object-wrapper tool)))

(re-frame/reg-fx
  :set-color
  (fn [[object-wrapper {:keys [color]}]]
    (apply-to-wrapper w/set-color object-wrapper color)))

(re-frame/reg-fx
  :stop
  (fn [[object-wrapper]]
    (apply-to-wrapper w/stop object-wrapper)))

(re-frame/reg-fx
  :set-data
  (fn [[object-wrapper params]]
    (apply-to-wrapper w/set-data object-wrapper params)))

(re-frame/reg-fx
  :set-path
  (fn [[object-wrapper {:keys [path]}]]
    (apply-to-wrapper w/set-path object-wrapper path)))

(re-frame/reg-fx
  :set-stroke
  (fn [[object-wrapper params]]
    (apply-to-wrapper w/set-stroke object-wrapper params)))
