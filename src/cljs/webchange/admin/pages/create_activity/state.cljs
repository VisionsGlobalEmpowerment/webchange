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
                  :description "Build an e-learning game"}
                 {:id 4
                  :name "Learning Assessment"
                  :description "Build an e-learning assessment"}
                 {:id 5
                  :name "Recording Studio"
                  :description "Give learners a chance to practice speaking by recording themselves responding to a spoken prompt, image, or video. "}
                 {:id 6
                  :name "Video Viewer"
                  :description "Insert a teaching video"}
                 {:id 7
                  :name "Writing Studio"
                  :description "Help learners practice writing with tools and prompts"}])

(def templates
  {1 [{:name "Early Reader Book"
       :preview "/upload/XBRPIXIQTLVVHVHA.png"
       :description "Create a simple book with images. Learners turn pages on their own!"
       :activity-ids {:english 691
                      :spanish 909}}]
   2 [{:name "Character Conversations"
       :preview "/upload/JMDFVYWWESOPPVRD.png"
       :description "Model a conversation. Add questions to check for comprehension."
       :activity-ids {:english 574
                      :spanish 1473}}
      {:name "Interactive Read Aloud "
       :description "Show a read aloud book. Add 'Teacher Talk' to support learners and questions to check for comprehension."
       :preview "/upload/YGYOFJOSCVQIVFLY.png"
       :activity-ids {:english 834
                      :spanish 913}}
      {:name "Letter Introduction"
       :description "Introduce the alphabet, one letter at a time."
       :preview "/upload/JCUMLEIECSBEOQSL.png"
       :activity-ids {:english 1786
                      :spanish 1822}}] ;concept
   3 [{:name "Learn and Slide"
       :description "Present a question. Learners tap an answer that 'slides' down if correct!"
       :preview "/upload/ISHGXNYTLBKDFPRB.png"
       :activity-ids {:english 769
                      :spanish 850}}
      {:name "Uncovering Concepts"
       :description "Learners 'dig' with their fingers to uncover new words and ideas."
       :preview "/upload/MNGPWFSJFLFDYSNP.png"
       :activity-ids {:english 756
                      :spanish 849}}
      {:name "I Spy 1 - Town with main street scene"
       :description "Learners find specific items in a scene and tap on them."
       :preview "/upload/FIYTVIKRLHLSWSSW.png"
       :activity-ids {:english 541
                      :spanish 848}}
      {:name "I Spy 2 - Town with bus stop"
       :description "Learners find specific items in a scene and tap on them."
       :preview "/upload/IWUPLROCXKKNGXTJ.png"
       :activity-ids {:english 542
                      :spanish 1469}}
      {:name "I Spy 3 - Town with park"
       :description "Learners find specific items in a scene and tap on them."
       :preview "/upload/ZAASEKRUPBACKUQV.png"
       :activity-ids {:english 543
                      :spanish 1293}}
      {:name "Run and Find"
       :description "Learners help a 'running' character collect correct answers before time runs out!"
       :preview "/upload/PGFOBUACXCCRNFIW.png"
       :activity-ids {:english 1949
                      :spanish 1975}} ;concept
      {:name "Sorting"
       :description "Drag and drop items into their correct category!"
       :preview "/upload/KCSNJZJUQLPOCVOP.png"
       :activity-ids {:english 814
                      :spanish 1384}}
      {:name "Bring Together"
       :description "Tap alternating objects to bring them together."
       :preview "/upload/CWEYFHHOYBZGGUGP.png"
       :activity-ids {:english 590
                      :spanish 1291}}]
   4 [{:name "Learning Assessment"
       :preview "/upload/JMDFVYWWESOPPVRD.png"
       :description "Check learning and understanding with assessment questions."
       :activity-ids {:english 574
                      :spanish 1473}}]
   5 [{:name "Sing-Along!"
       :description "Record learners singing along to a music video."
       :activity-ids {:english 1735
                      :spanish 13}}     ;no source
      {:name "Recording Studio"
       :description "Get learners talking with a prompt, image, or video!"
       :activity-ids {:english 1717
                      :spanish 14}}] ;no source
   6 [{:name "Video Viewer"
       :description "Play a short video."
       :preview "/upload/PYTRJJHHASSZMQKH.png"
       :activity-ids {:english 1896
                      :spanish 1922}}] ;concept
   7 [{:name "Letter Tracing"
       :preview "/upload/YSOKFHVULRWQLTYK.png"
       :activity-ids {:english 1841
                      :spanish 1867}} ;concept
      {:name "Word Tracing"
       :preview "/upload/DKAZCOSCVSHTKPEB.png"
       :activity-ids {:english 579
                      :spanish 1366}}
      {:name "Name Writing"
       :preview "/upload/PHLVUZMQBPAASPTT.png"
       :activity-ids {:english 1727
                      :spansih 1326}}]})

(def path-to-db :page/create-activity)

(re-frame/reg-sub
  path-to-db
  (fn [db]
    (get db path-to-db)))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (dissoc db :selection-confirmed :selected-template :selected-category)}))

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
  (fn [{:keys [db]} [_ template-name]]
    (let [category (get db :selected-category)
          template (->> (get templates (:id category))
                        (filter #(= template-name (:name %)))
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
    (let [lang (or (get-in db [:form :lang]) "english")
          activity-id (or (get-in db [:selected-template :activity-ids (keyword lang)])
                          (get-in db [:selected-template :activity-ids :english]))
          defaults {:lang lang}
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
     :dispatch [::routes/redirect :lesson-builder :activity-id id]}))

(re-frame/reg-event-fx
  ::build-failure
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {:db (assoc db :saving false)}))
