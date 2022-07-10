(ns webchange.admin.pages.create-activity.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.admin.routes :as routes]
    [webchange.state.warehouse :as warehouse]))

(def categories [{:id 1
                  :name "Book Activity"
                  :description "Make a book!"}
                 {:id 2
                  :name "Interactive Dialogue"
                  :description "Teach with books, images, and animated characters"}
                 {:id 3
                  :name "Learning Game"
                  :description "Build an e-learning or assessment game"}
                 {:id 4
                  :name "Recording Studio"
                  :description "Give learners a chance to practice speaking by recording themselves responding to a spoken prompt, image, or video. "}
                 {:id 5
                  :name "Video Viewer"
                  :description "Insert a teaching video"}
                 {:id 6
                  :name "Writing Studio"
                  :description "Help learners practice writing with tools and prompts"}])

(def templates
  {1 [{:name "Early Reader Book"
       :description "Create a simple book with images. Learners turn pages on their own!"
       :activity-id 691}]
   2 [{:name "Character Conversations"
       :description "Model a conversation. Add questions to check for comprehension."
       :activity-id 574}
      {:name "Interactive Read Aloud "
       :description "Show a read aloud book. Add 'Teacher Talk' to support learners and questions to check for comprehension."
       :activity-id 834}
      {:name "Letter Introduction"
       :description "Introduce the alphabet, one letter at a time."
       :activity-id 2654}] ;concept
   3 [{:name "Learn and Slide"
       :description "Present a question. Learners tap an answer that 'slides' down if correct!"
       :activity-id 769}
      {:name "Uncovering Concepts"
       :description "Learners 'dig' with their fingers to uncover new words and ideas."
       :activity-id 756}
      {:name "I Spy 1 - Town with main street scene"
       :description "Learners find specific items in a scene and tap on them."
       :activity-id 541}
      {:name "I Spy 2 - Town with bus stop"
       :description "Learners find specific items in a scene and tap on them."
       :activity-id 542}
      {:name "I Spy 3 - Town with park"
       :description "Learners find specific items in a scene and tap on them."
       :activity-id 543}
      {:name "Run and Find"
       :description "Learners help a 'running' character collect correct answers before time runs out!"
       :activity-id 2759} ;concept
      {:name "Sorting"
       :description "Drag and drop items into their correct category!"
       :activity-id 814}
      {:name "Bring Together"
       :description "Tap alternating objects to bring them together."
       :activity-id 590}]
   4 [{:name "Sing-Along!"
       :description "Record learners singing along to a music video."
       :activity-id 13} ;no source
      {:name "Recording Studio"
       :description "Get learners talking with a prompt, image, or video!"
       :activity-id 14}] ;no source
   5 [{:name "Video Viewer"
       :description "Play a short video."
       :activity-id 2706}] ;concept
   6 [{:name "Letter Tracing"
       :activity-id 2680} ;concept
      {:name "Word Tracing"
       :activity-id 579}
      {:name "Name Writing"
       :activity-id 1727}]})

(def path-to-db :page/create-activity)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {}))

(re-frame/reg-sub
  ::categories
  :<- [path-to-db]
  (fn [db]
    categories))

(re-frame/reg-sub
  ::selected-category
  :<- [path-to-db]
  (fn [db]
    (get db :selected-category)))

(re-frame/reg-event-fx
  ::select-category
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ category-id]]
    (let [category (->> categories
                        (filter #(= category-id (:id %)))
                        first)]
      {:db (-> db
               (assoc :selected-category category)
               (dissoc :selected-template))})))

(re-frame/reg-sub
  ::templates
  :<- [path-to-db]
  (fn [db]
    (if-let [category-id (get-in db [:selected-category :id])]
      (get templates category-id [])
      [])))

(re-frame/reg-sub
  ::selected-template
  :<- [path-to-db]
  (fn [db]
    (get db :selected-template)))

(re-frame/reg-event-fx
  ::select-template
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-id]]
    (let [category (get db :selected-category)
          template (->> (get templates (:id category))
                        (filter #(= activity-id (:activity-id %)))
                        first)]
      {:db (assoc db :selected-template template)})))

(re-frame/reg-sub
  ::selected-template-name
  :<- [::selected-category]
  :<- [::selected-template]
  (fn [[category template]]
    (when (and category template)
      (let [category-name (:name category)
            template-name (:name template)]
        (str category-name " > " template-name)))))

(re-frame/reg-sub
  ::selection-confirmed
  :<- [path-to-db]
  (fn [db]
    (get db :selection-confirmed)))

(re-frame/reg-event-fx
  ::confirm-selection
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :selection-confirmed true)}))

(re-frame/reg-event-fx
  ::back
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (dissoc db :selection-confirmed)}))


;; Form

(re-frame/reg-sub
  ::activity-name
  :<- [path-to-db]
  (fn [db]
    (get-in db [:form :name])))

(re-frame/reg-event-fx
  ::change-name
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ activity-name]]
    {:db (assoc-in db [:form :name] activity-name)}))

(re-frame/reg-sub
  ::language
  :<- [path-to-db]
  (fn [db]
    (get-in db [:form :lang])))

(re-frame/reg-event-fx
  ::change-lang
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ lang]]
    {:db (assoc-in db [:form :lang] lang)}))

(re-frame/reg-event-fx
  ::build
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    (let [activity-id (get-in db [:selected-template :activity-id])
          defaults {:lang "english"}
          data (merge defaults
                      (get db :form))]
      {:db (assoc db :saving true)
       :dispatch [::warehouse/duplicate-activity {:activity-id activity-id
                                                  :data data}
                  {:on-success [::build-success]
                   :on-failure [::build-failure]}]})))

(re-frame/reg-event-fx
  ::build-success
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ {:keys [id]}]]
    {:db (assoc db :saving true)
     :dispatch [::routes/redirect :activity-edit :activity-id id]}))

(re-frame/reg-event-fx
  ::build-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :saving false)}))
