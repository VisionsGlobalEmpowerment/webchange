(ns webchange.editor-v2.activity-form.generic.components.info-action.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.error-message.state :as error]
    [webchange.interpreter.renderer.scene.components.collisions :as collisions]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.interpreter.renderer.state.editor :as editor-state]
    [webchange.interpreter.renderer.scene.components.flipbook.decorations :as decorations]
    [webchange.interpreter.renderer.scene.components.utils :as utils]
    [webchange.state.state-course :as state-course]
    [webchange.state.warehouse :as warehouse]
    [webchange.subs :as subs]
    [webchange.utils.flipbook :refer [flipbook-activity?]]
    [webchange.utils.list :refer [sort-by-getters]]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:editor-v2 :info-action-modal])))

(def modal-state-path [:editor-v2 :info-action-modal :state])
(def book-categories-path [:editor-v2 :info-action-modal :book-categories])

;; Subs

(re-frame/reg-sub
  ::modal-state
  (fn [db]
    (-> db
        (get-in modal-state-path)
        boolean)))

;; Events

(defn- scene-data->book-keywords
  [scene-data]
  (let [text-objects (-> (get scene-data :objects)
                         (select-keys [:page-cover-title-text :page-cover-illustrators :page-cover-authors]))]
    (->> text-objects
         (map second)
         (map :text)
         (map #(clojure.string/split % #" "))
         (flatten))))

(re-frame/reg-event-fx
  ::open
  (fn [{:keys [db]} [_]]
    {:db       (assoc-in db modal-state-path true)
     :dispatch [::init]}))

(re-frame/reg-event-fx
  ::init
  (fn [{:keys [db]} [_]]
    (let [course-info (state-course/get-course-info db)
          scene-data (subs/current-scene-data db)
          book? (flipbook-activity? scene-data)]
      {:dispatch-n (cond-> [[::set-course-info course-info]]
                           book? (concat [[::set-keywords (scene-data->book-keywords scene-data)]])
                           :always (concat [[::warehouse/load-book-categories {:on-success [::set-book-categories]}]
                                            [::warehouse/load-book-languages {:on-success [::set-available-languages]}]
                                            [::warehouse/load-book-ages {:on-success [::set-available-ages]}]
                                            [::warehouse/load-book-genres {:on-success [::set-available-genres]}]
                                            [::warehouse/load-book-reading-levels {:on-success [::set-available-reading-levels]}]
                                            [::warehouse/load-book-tags {:on-success [::set-available-tags]}]]))})))

(re-frame/reg-event-fx
  ::reset
  (fn [{:keys [_]} [_]]
    {:dispatch-n [[::set-course-info nil]
                  [::set-save-in-progress false]
                  [::update-upload-status {:in-progress? false}]]}))

(re-frame/reg-event-fx
  ::close
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db modal-state-path false)}))

(defn open-info-window
  []
  (re-frame/dispatch [::open]))

;; Course Info

(def course-info-path (path-to-db [:course-info]))

(defn- get-course-info
  [db]
  (get-in db course-info-path))

(re-frame/reg-sub
  ::course-info
  get-course-info)

(re-frame/reg-event-fx
  ::set-course-info
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db course-info-path value)}))

(re-frame/reg-event-fx
  ::update-course-info
  (fn [{:keys [db]} [_ path value]]
    {:db (update-in db course-info-path assoc-in path value)}))

(re-frame/reg-sub
  ::book?
  (fn []
    (re-frame/subscribe [::subs/current-scene-data]))
  (fn [scene-data]
    (flipbook-activity? scene-data)))

;; Name

(def name-key [:name])

(re-frame/reg-sub
  ::name
  (fn []
    (re-frame/subscribe [::course-info]))
  (fn [course-info]
    (get-in course-info name-key "")))

(re-frame/reg-event-fx
  ::set-name
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-course-info name-key value]}))

;; Language

(def language-key [:lang])

(re-frame/reg-sub
  ::language
  (fn []
    (re-frame/subscribe [::course-info]))
  (fn [course-info]
    (get-in course-info language-key "")))

(re-frame/reg-event-fx
  ::set-language
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-course-info language-key value]}))

(def available-languages-path (path-to-db [:available-languages]))

(re-frame/reg-sub
  ::available-languages
  (fn [db]
    (get-in db available-languages-path)))

(re-frame/reg-event-fx
  ::set-available-languages
  (fn [{:keys [db]} [_ data]]
    {:db (->> data
              (sort-by-getters [#(get-in % [:metadata :primary?] false) #(get % :name)])
              (map #(select-keys % [:name :value]))
              (assoc-in db available-languages-path))}))

;; Image

(def image-key [:image-src])

(re-frame/reg-sub
  ::image
  (fn []
    (re-frame/subscribe [::course-info]))
  (fn [course-info]
    (get-in course-info image-key "")))

(re-frame/reg-event-fx
  ::set-image
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-course-info image-key value]}))

;; Book categories

(def category-key [:metadata :categories])

(re-frame/reg-sub
  ::category
  (fn []
    (re-frame/subscribe [::course-info]))
  (fn [course-info]
    (get-in course-info category-key [])))

(re-frame/reg-event-fx
  ::set-category
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-course-info category-key value]}))

(fn [db]
  (get-in db book-categories-path))

(re-frame/reg-sub
  ::book-categories
  (fn [db]
    (get-in db book-categories-path)))

(re-frame/reg-event-fx
  ::set-book-categories
  (fn [{:keys [db]} [_ data]]
    {:db (->> data
              (sort-by :name)
              (assoc-in db book-categories-path))}))

;; Ages

(def age-key [:metadata :ages])

(re-frame/reg-sub
  ::age
  (fn []
    (re-frame/subscribe [::course-info]))
  (fn [course-info]
    (get-in course-info age-key [])))

(re-frame/reg-event-fx
  ::set-age
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-course-info age-key value]}))

(def available-ages-path (path-to-db [:available-ages]))

(re-frame/reg-sub
  ::available-ages
  (fn [db]
    (get-in db available-ages-path)))

(re-frame/reg-event-fx
  ::set-available-ages
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db available-ages-path data)}))

;; Genres

(def genre-key [:metadata :genres])

(re-frame/reg-sub
  ::genre
  (fn []
    (re-frame/subscribe [::course-info]))
  (fn [course-info]
    (get-in course-info genre-key [])))

(re-frame/reg-event-fx
  ::set-genre
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-course-info genre-key value]}))

(def available-genres-path (path-to-db [:available-genres]))

(re-frame/reg-sub
  ::available-genres
  (fn [db]
    (get-in db available-genres-path)))

(re-frame/reg-event-fx
  ::set-available-genres
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db available-genres-path data)}))

;; Reading levels

(def reading-level-key [:metadata :reading-level])

(re-frame/reg-sub
  ::reading-level
  (fn []
    (re-frame/subscribe [::course-info]))
  (fn [course-info]
    (get-in course-info reading-level-key "")))

(re-frame/reg-event-fx
  ::set-reading-level
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-course-info reading-level-key value]}))

(def available-reading-levels-path (path-to-db [:available-reading-levels]))

(re-frame/reg-sub
  ::available-reading-levels
  (fn [db]
    (get-in db available-reading-levels-path)))

(re-frame/reg-event-fx
  ::set-available-reading-levels
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db available-reading-levels-path data)}))

;; Tags

(def tags-key [:metadata :tags])

(re-frame/reg-sub
  ::tags
  (fn []
    (re-frame/subscribe [::course-info]))
  (fn [course-info]
    (get-in course-info tags-key [])))

(re-frame/reg-event-fx
  ::set-tags
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-course-info tags-key value]}))

(def available-tags-path (path-to-db [:available-tags]))

(re-frame/reg-sub
  ::available-tags
  (fn [db]
    (get-in db available-tags-path)))

(re-frame/reg-event-fx
  ::set-available-tags
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db available-tags-path data)}))

;; Keywords

(def keywords-key [:metadata :keywords])

(re-frame/reg-event-fx
  ::set-keywords
  (fn [{:keys [_]} [_ value]]
    {:dispatch [::update-course-info keywords-key value]}))

;; Book preview

(re-frame/reg-event-fx
  ::update-book-preview
  (fn [{:keys [_]} [_]]
    {:get-book-screenshot {:on-success [::book-preview-ready]
                           :on-failure [::book-preview-failed]}}))

(re-frame/reg-event-fx
  ::book-preview-ready
  (fn [{:keys [_]} [_ img]]
    {:dispatch [::upload-preview img]}))

(re-frame/reg-event-fx
  ::book-preview-failed
  (fn [{:keys [_]} [_ error]]
    {:dispatch [::error/show "Get book preview error." [(str (.-name error) ": " (.-message error))]]}))

(re-frame/reg-fx
  :get-book-screenshot
  (fn [{:keys [on-success on-failure]}]
    (try
      (let [cover-object (-> @collisions/objects (get "page-cover") :object)
            shadow-object-name (decorations/crease-name 0)
            shadow-object (-> @collisions/objects (get shadow-object-name) :object)
            pages-object-name (decorations/right-pages-name 0)
            pages-object (-> @collisions/objects (get pages-object-name) :object)]
        (editor-state/hide-frames)
        (utils/set-visibility shadow-object false)
        (utils/set-visibility pages-object false)
        (app/take-object-screenshot cover-object #(re-frame/dispatch (conj on-success %))))
      (catch js/Error e
        (when (some? on-failure)
          (re-frame/dispatch (conj on-failure e)))))))

(def upload-status-path (path-to-db [:upload-status]))

(re-frame/reg-sub
  ::upload-status
  (fn [db]
    (get-in db upload-status-path)))

(re-frame/reg-sub
  ::uploading?
  (fn []
    (re-frame/subscribe [::upload-status]))
  (fn [upload-status]
    (get upload-status :in-progress? false)))

(re-frame/reg-event-fx
  ::update-upload-status
  (fn [{:keys [db]} [_ value]]
    {:db (update-in db upload-status-path merge value)}))

(re-frame/reg-event-fx
  ::upload-preview
  (fn [{:keys [_]} [_ file]]
    {:dispatch-n [[::update-upload-status {:in-progress? true}]
                  [::warehouse/upload-file
                   {:file        file
                    :form-params [["type" "image"]
                                  ["max-width" 384]
                                  ["max-height" 432]]}
                   {:on-success          [::upload-preview-success]
                    :on-failure          [::upload-preview-failure]
                    :suppress-api-error? true}]]}))

(re-frame/reg-event-fx
  ::upload-preview-success
  (fn [{:keys [_]} [_ {:keys [url]}]]
    {:dispatch-n [[::update-upload-status {:in-progress? false}]
                  [::set-image url]]}))

(re-frame/reg-event-fx
  ::upload-preview-failure
  (fn [{:keys [_]} [_ {:keys [status-text]}]]
    {:dispatch-n [[::update-upload-status {:in-progress? false}]
                  [::error/show "Upload preview image error." status-text]]}))

;; Save

(def save-in-progress-path (path-to-db [:save-in-progress]))

(re-frame/reg-sub
  ::save-in-progress?
  (fn [db]
    (get-in db save-in-progress-path)))

(re-frame/reg-event-fx
  ::set-save-in-progress
  (fn [{:keys [db]} [_ value]]
    {:db (assoc-in db save-in-progress-path value)}))

(re-frame/reg-event-fx
  ::save
  (fn [{:keys [db]} [_]]
    (let [course-info (get-course-info db)
          course-id (:id course-info)]
      {:dispatch-n [[::set-save-in-progress true]
                    [::warehouse/save-course-info
                     {:course-id course-id
                      :data      (select-keys course-info [:name :slug :image-src :lang :metadata])}
                     {:on-success [::save-success]
                      :on-failure [::save-failure]}]]})))

(re-frame/reg-event-fx
  ::save-success
  (fn [{:keys [_]} [_ course-info]]
    {:dispatch-n [[::set-save-in-progress false]
                  [::state-course/set-course-info course-info]
                  [::close]]}))

(re-frame/reg-event-fx
  ::save-failure
  (fn [{:keys [_]} [_ {:keys [status-text]}]]
    {:dispatch-n [[::set-save-in-progress false]
                  [::error/show "Save course info error." status-text]]}))
