(ns webchange.ui.components.audio-wave.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.audio-wave.constructor :refer [create-wavesurfer]]
    [webchange.ui.components.audio-wave.core :as core]
    [webchange.ui.components.audio-wave.controls :refer [get-controls]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- non-nil-regions
  [regions]
  (remove nil? regions))

(defn- handle-wave-surfer-ready
  [wave-surfer-instance
   {:keys [wave-surfer initial-regions] :as instances}
   {:keys [on-ready ref] :as props}]
  (reset! wave-surfer wave-surfer-instance)
  (core/subscribe-to-events instances props)
  (when (seq (non-nil-regions @initial-regions))
    (doseq [region (non-nil-regions @initial-regions)]
      (core/add-region wave-surfer-instance region)))
  (when (fn? on-ready)
    (on-ready))
  (when (fn? ref)
    (-> (get-controls instances)
        (ref))))

(defn audio-wave
  []
  (let [element (atom nil)
        instances {:wave-surfer (atom nil)
                   :regions     (atom [])
                   :initial-regions (atom [])}]
    (r/create-class
     {:component-did-mount
      (fn [this]
        (let [{:keys [url initial-regions] :as props} (r/props this)]
          (reset! (:initial-regions instances) initial-regions)
          (->> {:on-ready            #(handle-wave-surfer-ready % instances props)
                :script-class-name   "bbs--audio-wave--script"
                :timeline-class-name "bbs--audio-wave--timeline"
                :wave-class-name     "bbs--audio-wave--wave"}
               (create-wavesurfer @element url))))

      :component-did-update
      (fn [this [_ old-props]]
        (let [{old-regions     :initial-regions
               old-script-data :script-data} old-props
              {new-regions     :initial-regions
               new-script-data :script-data} (r/props this)]
          (when-not (= old-script-data new-script-data)
            (core/update-audio-script instances new-script-data))
          (when-not (= (non-nil-regions old-regions) (non-nil-regions new-regions))
            (core/update-regions instances new-regions))))

      :component-will-unmount
      (fn []
        (core/destroy instances))

      :reagent-render
      (fn [{:keys [class-name]}]
        (let [handle-ref #(when (and (some? %)
                                     (nil? @element))
                            (reset! element %))]
          [:div {:class-name (get-class-name {"bbs--audio-wave" true
                                              class-name        (some? class-name)})
                 :ref        handle-ref}]))})))
