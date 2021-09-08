(ns webchange.state.warehouse-recognition
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.audio-analyzer.region-data :refer [get-region-data-if-possible
                                                            get-region-options]]
    [webchange.editor-v2.audio-analyzer.talk-data :refer [get-chunks-data-if-possible
                                                          get-talk-data-if-possible]]
    [webchange.state.warehouse :as warehouse]))

(defn- dispatch-if-defined
  [handler & args]
  (if (some? handler)
    {:dispatch (-> handler (concat args) (vec))}
    {}))

;; Stored scripts

(defonce stored-results (atom {}))

(re-frame/reg-cofx
  :audio-scripts
  (fn [co-effects]
    (assoc co-effects :audio-scripts @stored-results)))

(re-frame/reg-fx
  :store-audio-script
  (fn [{:keys [audio-url audio-script]}]
    (swap! stored-results assoc audio-url audio-script)))

;; Script data

(re-frame/reg-event-fx
  ::get-audio-script-data
  [(re-frame/inject-cofx :audio-scripts)]
  (fn [{:keys [audio-scripts]} [_ {:keys [audio-url]} {:keys [on-success on-failure] :as handlers}]]
    (if-let [loaded-audio-script (get audio-scripts audio-url)]
      (dispatch-if-defined on-success loaded-audio-script)
      {:dispatch [::warehouse/load-audio-script-polled
                  {:file audio-url}
                  {:on-success [::audio-script-data-loaded {:audio-url audio-url} handlers]
                   :on-failure on-failure}]})))

(re-frame/reg-event-fx
  ::audio-script-data-loaded
  (fn [{:keys [_]} [_ {:keys [audio-url]} {:keys [on-success]} script-data]]
    {:dispatch           (conj on-success script-data)
     :store-audio-script {:audio-url    audio-url
                          :audio-script (->> script-data (remove #(= "[unk]" (:word %))))}}))

;; Script region

(re-frame/reg-event-fx
  ::get-audio-script-region
  (fn [{:keys [_]} [_
                    {:keys [audio-url script-text update-text-animation?]}
                    {:keys [on-failure] :as handlers}]]
    {:dispatch [::get-audio-script-data
                {:audio-url audio-url}
                {:on-success [::parse-audio-script-region
                              {:script-text            script-text
                               :update-text-animation? update-text-animation?}
                              handlers]
                 :on-failure on-failure}]}))

(re-frame/reg-event-fx
  ::parse-audio-script-region
  (fn [{:keys [_]} [_
                    {:keys [script-text update-text-animation? update-talk-animation?]}
                    {:keys [on-success on-failure]}
                    script-data]]
    (let [{:keys [matched? regions]} (get-region-options {:text script-text
                                                          :script script-data})]
      (if matched?
        (let [region-data (first regions)
              match-data {:region region-data
                          :script script-data
                          :text   script-text}]
          (dispatch-if-defined
            on-success
            (cond-> region-data
              update-text-animation? (assoc :data (get-chunks-data-if-possible match-data))
              update-talk-animation? (assoc :data (get-talk-data-if-possible match-data)))
            regions))
        (dispatch-if-defined on-failure)))))


(re-frame/reg-event-fx
  ::parse-audio-script-option
  [(re-frame/inject-cofx :audio-scripts)]
  (fn [{:keys [audio-scripts]}
       [_
        {:keys [audio-url script-text region-data update-text-animation? update-talk-animation?]}
        {:keys [on-success on-failure]}]]
    (let [script-data (get audio-scripts audio-url)
          match-data {:region region-data
                      :script script-data
                      :text   script-text}]
      (if script-data
        (dispatch-if-defined
         on-success
         (cond-> region-data
           update-text-animation? (assoc :data (get-chunks-data-if-possible match-data))
           update-talk-animation? (assoc :data (get-talk-data-if-possible match-data))))
        (dispatch-if-defined on-failure)))))
