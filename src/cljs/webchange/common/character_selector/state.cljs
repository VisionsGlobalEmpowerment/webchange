(ns webchange.common.character-selector.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.state.warehouse :as warehouse]))

(defn path-to-db [relative-path] (concat [:character-selector] relative-path))

;; Common

(def available-animations-path (path-to-db [:available-animations]))

(defn- get-available-animations
  [db]
  (get-in db available-animations-path []))

(re-frame/reg-sub
  ::available-animations
  (fn [db]
    (get-available-animations db)))

(defn- check-skin-option
  [value type]
  (-> value
      (clojure.string/split #"/")
      first
      (clojure.string/lower-case)
      keyword
      (= type)))

(defn- set-skins-type
  [skeletons]
  "Set if skeleton hes single or combined skins"
  (->> skeletons
       (map (fn [{:keys [skins] :as skeleton-data}]
              (->> (if (-> skins first :name (clojure.string/split #"/") count (> 1))
                     :combined :single)
                   (assoc skeleton-data :skin-type))))))

(defn- set-default-skin
  [skeletons]
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
                              ;; Take first not 'default' skin because 'default' skin is broken in most old animations
                              first-not-default-skin (some (fn [{:keys [name]}] (and (not= name "default") name)) skins)]
                          (->> (or first-not-default-skin first-skin)
                               (assoc skeleton-data :default-skin))))))))

(defn- filter-extra-skeletons
  [skeletons]
  (let [skeletons-to-avoid ["book" "boxes" "pinata" "vera-go" "vera-45" "vera-90"]]
    (->> skeletons
         (filter (fn [{:keys [name]}]
                   (-> #{name} (some skeletons-to-avoid) not))))))

(re-frame/reg-event-fx
  ::set-available-animations
  (fn [{:keys [db]} [_ animations]]
    (let [animations-data (->> animations filter-extra-skeletons set-skins-type set-default-skin)]
      {:db (assoc-in db available-animations-path animations-data)})))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_]]
    {:dispatch-n (cond-> []
                         (-> (get-available-animations db) (empty?))
                         (conj [::warehouse/load-animation-skins {:on-success [::set-available-animations]}]))}))

(re-frame/reg-sub
  ::combined-skins?
  (fn []
    [(re-frame/subscribe [::available-animations])])
  (fn [[available-animations] [_ skeleton-name]]
    (->> available-animations
         (some (fn [{:keys [name skin-type]}]
                 (and (= name skeleton-name) skin-type)))
         (= :combined))))

;; Skeleton

(re-frame/reg-sub
  ::skeletons-options
  (fn []
    [(re-frame/subscribe [::available-animations])])
  (fn [[available-animations]]
    (->> available-animations
         (map (fn [{:keys [name preview]}]
                {:value     name
                 :thumbnail preview})))))

;; Skin

(re-frame/reg-sub
  ::skins-options
  (fn []
    [(re-frame/subscribe [::available-animations])])
  (fn [[available-animations] [_ skeleton-name]]
    (->> available-animations
         (some (fn [{:keys [name skins]}]
                 (and (= name skeleton-name) skins)))
         (map (fn [{:keys [name preview]}]
                {:value     name
                 :thumbnail preview})))))

(re-frame/reg-sub
  ::combined-skins-options
  (fn [[_ skeleton-name]]
    [(re-frame/subscribe [::skins-options skeleton-name])])
  (fn [[skins-options] [_ _ skin-type]]
    (->> skins-options
         (filter #(check-skin-option (:value %) skin-type)))))
