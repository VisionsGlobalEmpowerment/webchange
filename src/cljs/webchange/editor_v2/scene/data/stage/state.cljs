(ns webchange.editor-v2.scene.data.stage.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.interpreter.renderer.state.overlays :as overlays]
    [webchange.state.state :as state]))

(re-frame/reg-event-fx
  ::show-flipbook-stage
  (fn [{:keys [db]} [_ spread-index]]
    (let [metadata (state/scene-metadata db)
          book-name (get metadata :flipbook-name)
          scene-id (:current-scene db)
          component-wrapper @(get-in db [:transitions scene-id book-name])]
      {:flipbook-show-spread {:component-wrapper component-wrapper
                              :spread-idx        spread-index}})))

(re-frame/reg-event-fx
  ::generate-stages-screenshots
  (fn [{:keys [db]} [_]]
    (let [stages-idx (->> (state/scene-metadata db)
                          (:stages)
                          (map :idx))]
      {:dispatch                [::overlays/show-waiting-screen]
       :take-stages-screenshots {:stages-idx stages-idx
                                 :callback   #(do (re-frame/dispatch [::update-stages-screenshots %])
                                                  (re-frame/dispatch [::show-flipbook-stage 0])
                                                  (re-frame/dispatch [::overlays/hide-waiting-screen]))}})))

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
  [stage-idx]
  (fn [callback]
    (re-frame/dispatch [::show-flipbook-stage stage-idx])
    (js/setTimeout (fn [] (app/take-screenshot callback)) 100)))

(re-frame/reg-fx
  :take-stages-screenshots
  (fn [{:keys [stages-idx callback]}]
    (run-seq (map take-stage-screenshot stages-idx) callback)))

(re-frame/reg-event-fx
  ::update-stages-screenshots
  (fn [{:keys [db]} [_ stages-screenshots]]
    (let [stages (->> (state/scene-metadata db)
                      (:stages))
          updated-stages (map (fn [{:keys [idx] :as stage}]
                                (assoc stage :img (.createObjectURL js/URL (get stages-screenshots idx))))
                              stages)]
      {:dispatch [::state/update-scene-metadata {:metadata-patch {:stages updated-stages}}]})))
