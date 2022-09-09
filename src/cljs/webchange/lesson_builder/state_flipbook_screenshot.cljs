(ns webchange.lesson-builder.state-flipbook-screenshot
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.interpreter.renderer.scene.app :as app]
    [webchange.lesson-builder.blocks.stage.state :as stage]
    [webchange.lesson-builder.state-flipbook :as state]
    [webchange.state.warehouse :as warehouse]
    [webchange.utils.crop-image :refer [get-half-image]]
    [webchange.utils.flipbook :as flipbook-utils]))

(defn- get-page-data
  [activity-data page-idx]
  (let [stage (->> (flipbook-utils/get-stages-data activity-data)
                   (some (fn [{:keys [pages-idx] :as stage-data}]
                           (and (some #{page-idx} pages-idx)
                                stage-data))))]
    {:stage-idx (:idx stage)
     :page-side (if (-> stage :pages-idx first (= page-idx))
                  "left" "right")}))

(defn- take-stage-screenshot
  [callback]
  (app/take-screenshot callback {:extract-canvas? false}))

(defn- take-page-screenshot
  [{:keys [side]} callback]
  (take-stage-screenshot #(get-half-image % callback {:side side})))

(re-frame/reg-event-fx
  ::upload-screenshot
  (fn [{:keys [_]} [_ page-idx image-blob]]
    {:dispatch [::warehouse/upload-image-blob
                {:blob image-blob}
                {:on-success [::upload-screenshot-success page-idx]}]}))

(defonce que (atom {:sequence    []
                    :running?    false
                    :saved-stage nil}))

(defonce page-screenshots (atom {}))

(re-frame/reg-event-fx
  ::upload-screenshot-success
  (fn [{:keys [_]} [_ page-idx response]]
    (swap! page-screenshots assoc page-idx response)
    {:dispatch [::run-que-next]}))

(re-frame/reg-event-fx
  ::run-que
  [(re-frame/inject-cofx :activity-data)]
  (fn [{:keys [activity-data]} [_]]
    (let [[page-idx & rest-que] (:sequence @que)
          {:keys [stage-idx page-side]} (get-page-data activity-data page-idx)]
      (swap! que assoc :sequence rest-que)
      (js/setTimeout (fn [] (take-page-screenshot {:side page-side} #(re-frame/dispatch [::upload-screenshot page-idx %]))) 100)
      {:dispatch [::state/show-flipbook-stage stage-idx]})))

(re-frame/reg-event-fx
  ::run-que-next
  (fn [{:keys [_]} [_]]
    (if (empty? (:sequence @que))
      (let [screenshots @page-screenshots
            saved-stage (:saved-stage @que)]
        (swap! que merge {:running?    false
                          :saved-stage nil})
        (reset! page-screenshots {})
        {:dispatch-n [[::state/update-pages-preview screenshots]
                      [::state/show-flipbook-stage saved-stage]
                      [::stage/set-stage-busy false]]})
      {:dispatch [::run-que]})))

(re-frame/reg-event-fx
  ::take-page-screenshot
  (i/path state/path-to-db)
  (fn [{:keys [db]} [_ page-idx]]
    (swap! que update :sequence conj page-idx)
    (when-not (:running? @que)
      (let [current-stage (state/get-current-stage db)]
        (swap! que merge {:running?    true
                          :saved-stage current-stage})
        {:dispatch-n [[::run-que]
                      [::stage/set-stage-busy true]]}))))