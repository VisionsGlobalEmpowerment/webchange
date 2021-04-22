(ns webchange.state.state-flipbook
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.state :as state-layout]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.interpreter.renderer.state.overlays :as overlays]
    [webchange.interpreter.renderer.state.scene :as scene]
    [webchange.logger.index :as logger]
    [webchange.state.state :as state]
    [webchange.editor-v2.assets.events :as assets-events]
    [webchange.state.state-activity :as state-activity]
    [webchange.utils.flipbook :as flipbook-utils]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:flipbook])
       (state/path-to-db)))

(defn- get-current-stage-idx
  [db]
  (get-in db (state-layout/path-to-db [:current-stage]) 0))

(defn- set-current-stage-idx
  [db idx]
  (assoc-in db (state-layout/path-to-db [:current-stage]) idx))

(re-frame/reg-sub
  ::current-stage-idx
  get-current-stage-idx)

(re-frame/reg-sub
  ::stage-pages
  (fn []
    [(re-frame/subscribe [::state/scene-data])
     (re-frame/subscribe [::current-stage-idx])])
  (fn [[scene-data current-stage-idx] [_ page-side stage-idx]]
    (let [current-stage (flipbook-utils/get-stage-data scene-data (if (some? stage-idx) stage-idx current-stage-idx))
          pages (flipbook-utils/get-pages-data scene-data)
          current-pages-data (->> (:pages-idx current-stage)
                                  (map (fn [page-idx]
                                         (when (some? page-idx)
                                           (nth pages page-idx)))))]
      (if (some? page-side)
        (case page-side
          :left (first current-pages-data)
          :right (second current-pages-data))
        current-pages-data))))

(re-frame/reg-sub
  ::page-removable?
  (fn [[_ page-side stage]]
    [(re-frame/subscribe [::stage-pages page-side stage])])
  (fn [[current-pages]]
    (:removable? current-pages)))

(re-frame/reg-sub
  ::page-draggable?
  (fn [[_ page-side stage]]
    [(re-frame/subscribe [::page-removable? page-side stage])])
  (fn [[removable?]]
    removable?))

(re-frame/reg-event-fx
  ::move-page
  (fn [{:keys [db]} [_
                     {source-stage-idx :stage source-page-side :side}
                     {target-stage-idx :stage target-page-side :side}
                     relative-position
                     handlers]]
    (let [activity-data (state/scene-data db)
          page-idx-from (flipbook-utils/stage-idx->page-idx activity-data source-stage-idx source-page-side)
          page-idx-to (cond-> (flipbook-utils/stage-idx->page-idx activity-data target-stage-idx target-page-side)
                              (< target-stage-idx source-stage-idx)
                              (cond->
                                (= relative-position "after") (inc))

                              (= target-stage-idx source-stage-idx)
                              (cond->
                                (= target-page-side "left")
                                (cond->
                                  (= relative-position "after") (inc))
                                (= target-page-side "right")
                                (cond->
                                  (= relative-position "before") (dec)))

                              (> target-stage-idx source-stage-idx)
                              (cond->
                                (= relative-position "before") (dec)))]

      (logger/group-folded "::move-page")
      (logger/trace "source" source-stage-idx source-page-side)
      (logger/trace "target" target-stage-idx target-page-side)
      (logger/trace "relative-position" relative-position)
      (logger/trace "page-idx-from" page-idx-from)
      (logger/trace "page-idx-to" page-idx-to)
      (logger/group-end "::move-page")

      {:dispatch [::state-activity/call-activity-action
                  {:action "move-page"
                   :data   {:page-idx-from page-idx-from
                            :page-idx-to   page-idx-to}}
                  {:on-success [::update-activity-success handlers]}]})))

(re-frame/reg-event-fx
  ::remove-current-stage-page
  (fn [{:keys [db]} [_ page-side]]
    (let [current-stage (get-current-stage-idx db)]
      {:dispatch [::state-activity/call-activity-action
                  {:action "remove-page"
                   :data   {:stage     current-stage
                            :page-side page-side}}
                  {:on-success [::update-activity-success]}]})))

(re-frame/reg-event-fx
  ::update-activity-success
  (fn [{:keys [db]} [_ {:keys [on-success]}]]
    {:dispatch-n (cond-> [[::reset-stage]]
                         (some? on-success) (conj on-success))}))

(re-frame/reg-event-fx
  ::set-generate-screenshots-running-state
  (fn [{:keys [db]} [_ in-progress?]]
    {:db (assoc-in db (path-to-db [:generate-screenshots-running?]) in-progress?)}))

(re-frame/reg-sub
  ::generate-screenshots-running?
  (fn [db]
    (get-in db (path-to-db [:generate-screenshots-running?]) false)))

(re-frame/reg-sub
  ::stages-screenshots
  (fn [db]
    (get-in db (path-to-db [:stages-screenshots]) [])))

(re-frame/reg-sub
  ::stages-blob-screenshots
  (fn [db]
    (get-in db (path-to-db [:stages-blob-screenshots]) [])))

(re-frame/reg-event-fx
  ::set-stages-screenshots
  (fn [{:keys [db]} [_ screenshots]]
    {:db (assoc-in db (path-to-db [:stages-screenshots]) screenshots)}))

(def show-generated-pages-path (path-to-db [:show-generated-pages?]))

(defn get-show-generated-pages
  [db]
  (get-in db show-generated-pages-path false))

(re-frame/reg-sub
  ::show-generated-pages?
  get-show-generated-pages)

(re-frame/reg-event-fx
  ::set-show-generated-pages?
  (fn [{:keys [db]} [_ value]]
    {:db       (assoc-in db show-generated-pages-path value)
     :dispatch [::generate-stages-screenshots {:hide-generated-pages? (not value)}]}))

(re-frame/reg-event-fx
  ::set-stages-blob-screenshots
  (fn [{:keys [db]} [_ screenshots]]
    {:db (assoc-in db (path-to-db [:stages-blob-screenshots]) screenshots)}))

(re-frame/reg-event-fx
  ::generate-stages-screenshots
  (fn [{:keys [db]} [_ {:keys [hide-generated-pages?] :or {hide-generated-pages? true}}]]
    (let [show-generated-pages? (get-show-generated-pages db)
          current-stage (get-current-stage-idx db)
          metadata (state/scene-metadata db)
          stages-idx (->> (state/scene-metadata db)
                          (:stages)
                          (map :idx))
          ;"Interactive Read Aloud workaround to show book and it's background for screenshots"
          book-name (get metadata :flipbook-name)
          scene-id (:current-scene db)
          book-background (str book-name "-background")
          component-wrapper @(get-in db [:transitions scene-id book-name])
          book-background-wrapper (get-in db [:transitions scene-id book-background])
          visibility ((:get-visibility component-wrapper))]
      (if (not visibility) (do
                             ((:set-visibility component-wrapper) true)
                             (if book-background-wrapper ((:set-visibility @book-background-wrapper) true))))
      {:dispatch-n              [[::set-generate-screenshots-running-state true]
                                 [::overlays/show-waiting-screen]]
       :take-stages-screenshots {:stages-idx            stages-idx
                                 :hide-generated-pages? (not show-generated-pages?)
                                 :callback              (fn [screenshots]
                                                          (let [screenshots-blobs (->> screenshots
                                                                                       (map (fn [[idx blob]]
                                                                                              [idx (.createObjectURL js/URL blob)]))
                                                                                       (into {}))
                                                                blobs (->> screenshots
                                                                           (map (fn [[idx blob]] [idx blob]))
                                                                           (into {}))]
                                                            (re-frame/dispatch [::set-stages-screenshots screenshots-blobs])
                                                            (re-frame/dispatch [::set-stages-blob-screenshots blobs])
                                                            )
                                                          (re-frame/dispatch [::select-stage (or current-stage 0)])
                                                          (re-frame/dispatch [::overlays/hide-waiting-screen])
                                                          (re-frame/dispatch [::set-generate-screenshots-running-state false])
                                                          (if (not visibility)
                                                            (do
                                                              ((:set-visibility component-wrapper) false)
                                                              (if book-background-wrapper ((:set-visibility @book-background-wrapper) false))))
                                                          )}})))

(re-frame/reg-event-fx
  ::upload-stage-screenshot
  (fn [{:keys [db]} [_ stages-idx callback]]
    (let [screenshots @(re-frame/subscribe [::stages-blob-screenshots])]
      (re-frame/dispatch [::assets-events/upload-asset (get screenshots stages-idx) {:type      "image"
                                                                                     :on-finish (fn [result]
                                                                                                  (callback result))}]))
    {}))

(defn- run-seq
  ([seq callback]
   (run-seq seq callback 0 (atom {})))
  ([seq callback idx data]
   (if (empty? seq)
     (callback @data)
     (let [last-index? (-> (count seq) (dec) (= idx))
           f (nth seq idx)]
       (f (fn [result]
            (swap! data assoc idx result)
            (if last-index?
              (callback @data)
              (run-seq seq callback (inc idx) data))))))))

(defn- take-stage-screenshot
  [stage-idx hide-generated-pages?]
  (fn [callback]
    (re-frame/dispatch [::show-flipbook-stage stage-idx {:hide-generated-pages? hide-generated-pages?}])
    (js/setTimeout (fn [] (app/take-screenshot callback {:extract-canvas? false})) 100)))

(re-frame/reg-fx
  :take-stages-screenshots
  (fn [{:keys [stages-idx callback hide-generated-pages?]}]
    (run-seq (map #(take-stage-screenshot % hide-generated-pages?) stages-idx) callback)))

(re-frame/reg-sub
  ::scene-stages
  (fn []
    [(re-frame/subscribe [::state/scene-data])])
  (fn [[scene-data]]
    (flipbook-utils/get-stages-data scene-data)))

(defn- filter-generated-page
  [scene-data page-idx]
  (let [generated? (->> page-idx (flipbook-utils/get-page-data scene-data) (:generated?))]
    (when-not generated?
      page-idx)))

(re-frame/reg-sub
  ::stage-options
  (fn []
    [(re-frame/subscribe [::state/scene-data])
     (re-frame/subscribe [::scene-stages])
     (re-frame/subscribe [::current-stage-idx])
     (re-frame/subscribe [::stages-screenshots])])
  (fn [[scene-data stages current-stage-idx stages-screenshots] [_ {:keys [filter-generated?] :or {filter-generated? false}}]]
    (->> stages
         (map-indexed (fn [idx stage]
                        (cond-> (-> stage
                                    (assoc :idx idx)
                                    (assoc :img (get stages-screenshots idx))
                                    (assoc :active? (= idx current-stage-idx)))
                                filter-generated? (assoc :pages-idx [(->> (:pages-idx stage) (first) (filter-generated-page scene-data))
                                                                     (->> (:pages-idx stage) (second) (filter-generated-page scene-data))]))))
         (filter (fn [stage]
                   (not (and (nil? (first (:pages-idx stage)))
                             (nil? (second (:pages-idx stage))))))))))

;; Select Stage

(re-frame/reg-event-fx
  ::select-stage
  (fn [{:keys [db]} [_ idx]]
    (let [metadata (get-in db [:current-scene-data :metadata])]
      (if (contains? metadata :flipbook-name)
        {:dispatch [::select-flipbook-stage idx]}
        (let [objects (get-in db [:current-scene-data :scene-objects])
              stage (get-in db [:current-scene-data :metadata :stages idx])
              visible? (fn [name] (some #{name} (:objects stage)))]
          {:db         (assoc-in db (path-to-db [:current-stage]) idx)
           :dispatch-n (->> objects
                            flatten
                            (map (fn [object-name]
                                   [::scene/change-scene-object (keyword object-name) [[:set-visibility {:visible (visible? object-name)}]]])))})))))

(re-frame/reg-event-fx
  ::select-flipbook-stage
  (fn [{:keys [db]} [_ idx]]
    (let [show-generated-pages? (get-show-generated-pages db)
          stage (-> (get-in db [:current-scene-data :metadata])
                    (get-in [:stages idx]))]
      {:db       (set-current-stage-idx db idx)
       :dispatch [::show-flipbook-stage (:idx stage) {:hide-generated-pages? (not show-generated-pages?)}]})))

(re-frame/reg-event-fx
  ::show-flipbook-stage
  (fn [{:keys [db]} [_ spread-index {:keys [hide-generated-pages?]}]]
    (let [metadata (state/scene-metadata db)
          book-name (get metadata :flipbook-name)
          scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id book-name])]
      {:flipbook-show-spread {:component-wrapper     component-wrapper
                              :spread-idx            spread-index
                              :hide-generated-pages? hide-generated-pages?}})))

(re-frame/reg-fx
  :flipbook-show-spread
  (fn [{:keys [component-wrapper spread-idx hide-generated-pages?]}]
    ((:show-spread component-wrapper) spread-idx {:hide-generated-pages? hide-generated-pages?})))

;; Stages Navigation

(defn- scene-stages
  [db scene-id]
  (get-in db [:scenes scene-id :metadata :stages] []))

(defn- get-filtered-scene-stages
  [db]
  (let [show-generated-pages? (get-show-generated-pages db)
        scene-data (state/scene-data db)]
    (cond->> (flipbook-utils/get-stages-data scene-data)
             (not show-generated-pages?) (filter (fn [{:keys [pages-idx]}]
                                                   (not (and (->> (first pages-idx) (flipbook-utils/get-page-data scene-data) (:generated?))
                                                             (->> (second pages-idx) (flipbook-utils/get-page-data scene-data) (:generated?)))))))))

(defn- get-next-stage-idx
  [db]
  (let [scene-stages (get-filtered-scene-stages db)
        current-stage-idx (get-current-stage-idx db)]
    (some (fn [stage-data]
            (let [stage-idx (:idx stage-data)]
              (and (> stage-idx current-stage-idx)
                   stage-idx)))
          scene-stages)))

(defn- next-stage-available?
  [db]
  (-> (get-next-stage-idx db) (some?)))

(re-frame/reg-sub
  ::next-stage-available?
  (fn [db]
    (next-stage-available? db)))

(re-frame/reg-event-fx
  ::select-next-stage
  (fn [{:keys [db]} [_]]
    (let [next-stage-idx (get-next-stage-idx db)]
      (cond-> {}
              (next-stage-available? db) (assoc :dispatch [::select-stage next-stage-idx])))))

(defn- get-prev-stage-idx
  [db]
  (let [scene-stages (get-filtered-scene-stages db)
        current-stage-idx (get-current-stage-idx db)]
    (some (fn [stage-data]
            (let [stage-idx (:idx stage-data)]
              (and (< stage-idx current-stage-idx)
                   stage-idx)))
          (reverse scene-stages))))

(defn- prev-stage-available?
  [db]
  (-> (get-prev-stage-idx db) (some?)))

(re-frame/reg-sub
  ::prev-stage-available?
  (fn [db]
    (prev-stage-available? db)))

(re-frame/reg-event-fx
  ::select-prev-stage
  (fn [{:keys [db]} [_]]
    (let [prev-stage-idx (get-prev-stage-idx db)]
      (cond-> {}
              (prev-stage-available? db) (assoc :dispatch [::select-stage prev-stage-idx])))))

;; Reset Stage

(re-frame/reg-sub
  ::stage-key
  (fn [db]
    (get-in db (path-to-db [:stage-key]) "default")))

(re-frame/reg-event-fx
  ::reset-stage
  (fn [{:keys [db]} [_]]
    {:db (assoc-in db (path-to-db [:stage-key]) (-> (random-uuid) str))}))
