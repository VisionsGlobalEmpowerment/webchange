(ns webchange.state.warehouse-animations
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:animations])
       (warehouse/path-to-db)))

(def available-animations-path (path-to-db []))

(defn- get-available-animations
  [db]
  (get-in db available-animations-path nil))

(re-frame/reg-sub
  ::available-animations
  get-available-animations)

(re-frame/reg-event-fx
  ::load-available-animation
  (fn [{:keys [db]} [_ {:keys [on-success on-failure] :as handlers}]]
    (let [current-available-animations (get-available-animations db)]
      (if (some? current-available-animations)
        {:dispatch (conj on-success current-available-animations)}
        {:dispatch [::warehouse/load-animation-skins {:on-success [::set-available-animation handlers]
                                                      :on-failure on-failure}]}))))

(defn- set-skins-type
  [skeletons]
  "Set if skeleton uses single or combined skins"
  (->> skeletons
       (map (fn [{:keys [skins] :as skeleton-data}]
              (->> (if (-> skins first :name (clojure.string/split #"/") count (> 1))
                     :combined :single)
                   (assoc skeleton-data :skin-type))))))

(defn- check-skin-option
  [value type]
  (-> value
      (clojure.string/split #"/")
      first
      (clojure.string/lower-case)
      keyword
      (= type)))

(defn- set-default-skin
  [skeletons]
  "Set default skin name for current animation."
  (->> skeletons
       (map (fn [{:keys [skins skin-type] :as skeleton-data}]
              (case skin-type
                :combined (let [get-first-suitable (partial (fn [options option-key]
                                                              (->> options
                                                                   (filter #(check-skin-option (:name %) option-key))
                                                                   (first)
                                                                   (:name)))
                                                            skins)]
                            (assoc skeleton-data :default-skins {:body    (get-first-suitable :body)
                                                                 :clothes (get-first-suitable :clothes)
                                                                 :head    (get-first-suitable :head)}))
                :single (let [first-skin (-> skins first :name)
                              ;; Take first not 'default' skin because 'default' skin is broken in most old animations.
                              first-not-default-skin (some (fn [{:keys [name]}] (and (not= name "default") name)) skins)]
                          (->> (or first-not-default-skin first-skin)
                               (assoc skeleton-data :default-skin))))))))

(defn- set-available-emotions
  [skeletons]
  "Find emotion animations and store in separate field."
  (->> skeletons
       (map (fn [{:keys [animations] :as skeleton-data}]
              (->> animations
                   (map #(re-matches #"^emotion_(.+)" %))
                   (filter some?)
                   (map (fn [[animation-name emotion]]
                          {:animation animation-name
                           :emotion   emotion}))
                   (assoc skeleton-data :emotions))))))

(re-frame/reg-event-fx
  ::set-available-animation
  (fn [{:keys [db]} [_ {:keys [on-success]} data]]
    (let [animations-data (->> data
                               set-skins-type
                               set-default-skin
                               set-available-emotions)]
      {:db       (assoc-in db available-animations-path animations-data)
       :dispatch (conj on-success animations-data)})))
