(ns webchange.state.state-flipbook
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage-state]
    [webchange.editor-v2.layout.state :as state-layout]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.interpreter.renderer.state.overlays :as overlays]
    [webchange.state.state :as state]
    [webchange.state.state-activity :as state-activity]
    [webchange.utils.flipbook :as flipbook-utils]))

(defn path-to-db
  [relative-path]
  (->> relative-path
       (concat [:flipbook])
       (state/path-to-db)))

(defn current-stage
  [db]
  (get-in db (state-layout/path-to-db [:current-stage]) 0))

(re-frame/reg-sub
  ::current-stage
  current-stage)

(re-frame/reg-sub
  ::stage-pages
  (fn []
    [(re-frame/subscribe [::state/scene-data])
     (re-frame/subscribe [::current-stage])])
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
                     {:keys [on-success]}]]
    (let [activity-data (state/scene-data db)
          page-idx-from (flipbook-utils/stage-idx->page-idx activity-data source-stage-idx source-page-side)
          page-idx-to (flipbook-utils/stage-idx->page-idx activity-data target-stage-idx target-page-side)]
      {:dispatch [::state-activity/call-activity-action
                  {:action "move-page"
                   :data   {:page-idx-from page-idx-from
                            :page-idx-to   (case relative-position
                                             "before" (dec page-idx-to)
                                             "after" page-idx-to)}}
                  {:on-success on-success}]})))

(re-frame/reg-event-fx
  ::remove-current-stage-page
  (fn [{:keys [db]} [_ page-side]]
    (let [current-stage (stage-state/current-stage db)]
      {:dispatch [::state-activity/call-activity-action {:action "remove-page"
                                                         :data   {:stage     current-stage
                                                                  :page-side page-side}}]})))

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
  ::generate-stages-screenshots
  (fn [{:keys [db]} [_ {:keys [hide-generated-pages?] :or {hide-generated-pages? true}}]]
    (let [show-generated-pages? (get-show-generated-pages db)
          current-stage (stage-state/current-stage db)
          stages-idx (->> (state/scene-metadata db)
                          (:stages)
                          (map :idx))]
      {:dispatch-n              [[::set-generate-screenshots-running-state true]
                                 [::overlays/show-waiting-screen]]
       :take-stages-screenshots {:stages-idx            stages-idx
                                 :hide-generated-pages? (not show-generated-pages?)
                                 :callback              (fn [screenshots]
                                                          (let [screenshots-blobs (->> screenshots
                                                                                       (map (fn [[idx blob]]
                                                                                              [idx (.createObjectURL js/URL blob)]))
                                                                                       (into {}))]
                                                            (re-frame/dispatch [::set-stages-screenshots screenshots-blobs]))
                                                          (re-frame/dispatch [::stage-state/select-stage (or current-stage 0)])
                                                          (re-frame/dispatch [::overlays/hide-waiting-screen])
                                                          (re-frame/dispatch [::set-generate-screenshots-running-state false]))}})))

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
    (re-frame/dispatch [::stage-state/show-flipbook-stage stage-idx {:hide-generated-pages? hide-generated-pages?}])
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
     (re-frame/subscribe [::current-stage])
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
