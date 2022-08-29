(ns webchange.ui.components.audio-wave.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.audio-wave.constructor :refer [create-wavesurfer]]
    [webchange.ui.components.audio-wave.core :as core]
    [webchange.ui.components.audio-wave.controls :refer [get-controls]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- handle-wave-surfer-ready
  [wave-surfer-instance instances {:keys [on-ready region] :as props}]
  (print "instances" instances)
  (reset! (:wave-surfer instances) wave-surfer-instance)
  (js/console.log "handle wave-surfer ready")
  (print "instances updated" instances)
  (core/subscribe-to-events instances props)
  (print "region" region)
  (when (some? region)
    (core/add-region instances region))
  (when (fn? on-ready)
    (on-ready)))

(defn audio-wave
  []
  (let [wave-surfer (atom nil)
        region (atom nil)
        element (atom nil)
        instances {:wave-surfer wave-surfer
                   :region      region}]
    (r/create-class
      {:component-did-mount
       (fn [this]
         (let [{:keys [url ref] :as props} (r/props this)]
           (->> {:on-ready            #(handle-wave-surfer-ready % instances props)
                 :script-class-name   "bbs--audio-wave--script"
                 :timeline-class-name "bbs--audio-wave--timeline"
                 :wave-class-name     "bbs--audio-wave--wave"}
                (create-wavesurfer @element url))

           (when (fn? ref)
             (-> (get-controls instances)
                 (ref)))))

       :component-did-update
       (fn [this [_ old-props]]
         (let [{old-region      :region
                old-script-data :script-data} old-props
               {new-region      :region
                new-script-data :script-data} (r/props this)]
           (when-not (= old-script-data new-script-data)
             (core/update-audio-script instances new-script-data))
           (when-not (= old-region new-region)
             (core/update-region instances new-region))))

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
