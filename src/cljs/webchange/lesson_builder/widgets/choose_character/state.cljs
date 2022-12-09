(ns webchange.lesson-builder.widgets.choose-character.state
  (:require
    [clojure.string :as str]
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.editor-v2.components.character-form.data :as data]))

(def path-to-db :widget/choose-character)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [on-change default-value]}]]
    (let [form-data (data/animation->character-data default-value)
          initial-data (if (data/sitting-character? (:character form-data))
                         {:selected-character "vera-sitting"
                          :selected-sitting-character (:character form-data)}
                         {:selected-character (:character form-data)
                          :selected-skin (:skin form-data)
                          :selected-head (:head form-data)
                          :selected-clothing (:clothes form-data)})]
      {:db (-> db
               (assoc :form form-data)
               (assoc :on-change on-change)
               (merge initial-data))})))

(defn- form-data->skin-params
  [{:keys [change-skeleton? character clothes head skin scale]
    :or   {change-skeleton? false}}]
  (let [character-str (str/capitalize character)]
    (-> (cond
          (some #{character} ["boy" "girl"])
          {:name        "child"
           :skin-params {:body    (str "BODY/ChildTon-" skin)
                         :head    (str "HEAD/Head-" character-str "-" head "-Ton-" skin)
                         :clothes (str "CLOTHES/" character-str "-" clothes "-Clothes-" skin)}}

          (some #{character} ["man" "woman"])
          {:name        "adult"
           :skin-params {:body    (str "BODY/AdultTon-" skin)
                         :head    (str "HEAD/" character-str "Head-" head "-Ton-" skin)
                         :clothes (str "CLOTHES/" character-str "-" clothes "-Clothes-" skin)}}

          (data/single-skin-character? character)
          {:name        (data/character->skeleton character)
           :skin-params skin})
        (assoc :change-skeleton? change-skeleton?
               :scale scale))))

(defn- character->object-state
  [{:keys [change-skeleton? name skin-params scale]}]
  (cond-> {}
          change-skeleton? (assoc :name name)
          (string? skin-params) (assoc :skin skin-params)
          (map? skin-params) (assoc :skin-names skin-params)
          (some? scale) (assoc :scale scale)))

(defn- form-changed!
  [db data]
  (let [on-change (:on-change db)
        skin-params (-> data form-data->skin-params character->object-state)]
    (when (fn? on-change)
      (on-change skin-params))))

;; Characters
(re-frame/reg-sub
  ::available-characters
  (fn []
    (->> data/characters
         (map (fn [{:keys [value] :as character}]
                (assoc character :image (data/get-preview {:entity    "character"
                                                           :character value})))))))

(re-frame/reg-sub
  ::selected-character
  :<- [path-to-db]
  (fn [db]
    (get db :selected-character)))

(re-frame/reg-event-fx
  ::select-character
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (when (not= value (:selected-character db))
      (let [selected (->> data/characters
                          (filter #(= (:value %) value))
                          first)
            form-data (merge {:change-skeleton? true
                              :character value}
                             (:defaults selected))]
        (form-changed! db form-data)
        {:db (-> db
                 (assoc :selected-character value)
                 (dissoc :selected-sitting-character)
                 (dissoc :selected-head)
                 (dissoc :selected-skin)
                 (dissoc :selected-clothing)
                 (assoc :form form-data))}))))

;; Sitting Characters

(re-frame/reg-sub
  ::available-sitting-characters
  (fn []
    (data/sitting-character-options)))

(re-frame/reg-sub
  ::selected-sitting-character
  :<- [path-to-db]
  (fn [db]
    (get db :selected-sitting-character)))

(re-frame/reg-event-fx
  ::select-sitting-character
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [form-data (-> db
                        :form
                        (assoc :change-skeleton? true)
                        (assoc :character value))]
      (form-changed! db form-data)
      {:db (-> db
               (assoc :selected-sitting-character value)
               (assoc :form form-data))})))

;; Skins

(re-frame/reg-sub
  ::available-skins
  (fn []
    data/skins))

(re-frame/reg-sub
  ::selected-skin
  :<- [path-to-db]
  (fn [db]
    (get db :selected-skin)))

(re-frame/reg-event-fx
  ::select-skin
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [form-data (-> db
                        :form
                        (assoc :skin value))]
      (form-changed! db form-data)
      {:db (-> db
               (assoc :selected-skin value)
               (assoc :form form-data))})))

;; Heads

(re-frame/reg-sub
  ::available-heads
  :<- [::selected-character]
  :<- [::selected-skin]
  (fn [[selected-character selected-skin]]
    (let [skin (or selected-skin "01")
          character-data (some (fn [{:keys [value] :as data}]
                                 (and (= value selected-character) data))
                               data/characters)]
      (->> (:heads character-data)
           (map (fn [head-value]
                  (some (fn [{:keys [value] :as data}]
                          (and (= value head-value) data))
                        data/heads)))
           (map (fn [{:keys [value] :as head-data}]
                  (-> head-data
                      (assoc :image (data/get-preview {:entity    "head"
                                                       :character selected-character
                                                       :skin      skin
                                                       :head      value}))
                      (assoc :background (data/get-background-color {:character selected-character
                                                                     :skin      skin})))))))))

(re-frame/reg-sub
  ::selected-head
  :<- [path-to-db]
  (fn [db]
    (get db :selected-head)))

(re-frame/reg-event-fx
  ::select-head
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [form-data (-> db
                        :form
                        (assoc :head value))]
      (form-changed! db form-data)
      {:db (-> db
               (assoc :selected-head value)
               (assoc :form form-data))})))

;; Clothes

(re-frame/reg-sub
  ::available-clothes
  :<- [::selected-character]
  :<- [::selected-skin]
  (fn [[selected-character selected-skin]]
    (let [skin (or selected-skin "01")
          character-data (some (fn [{:keys [value] :as data}]
                                 (and (= value selected-character) data))
                               data/characters)]
      (->> (:clothes character-data)
           (map (fn [clothes-value]
                  (some (fn [{:keys [value] :as data}]
                          (and (= value clothes-value) data))
                        data/clothes)))
           (map (fn [{:keys [value] :as cloth-data}]
                  (-> cloth-data
                      (assoc :image (data/get-preview {:entity    "clothes"
                                                       :character selected-character
                                                       :skin      skin
                                                       :clothes   value}))
                      (assoc :background (data/get-background-color {:character selected-character
                                                                     :skin      skin})))))))))

(re-frame/reg-sub
  ::selected-clothing
  :<- [path-to-db]
  (fn [db]
    (get db :selected-clothing)))

(re-frame/reg-event-fx
  ::select-clothing
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ value]]
    (let [form-data (-> db
                        :form
                        (assoc :clothes value))]
      (form-changed! db form-data)
      {:db (-> db
               (assoc :selected-clothing value)
               (assoc :form form-data))})))

(re-frame/reg-sub
  ::show-extra-params
  :<- [path-to-db]
  (fn [db]
    (let [character (-> db :selected-character)]
      (cond 
        (some #{character} ["boy" "girl" "man" "woman"])
        {:skin true
         :head true
         :clothes true}

        (some #{character} ["vera-sitting"])
        {:character-options true}))))
