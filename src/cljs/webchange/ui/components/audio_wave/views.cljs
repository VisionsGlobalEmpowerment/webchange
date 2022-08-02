(ns webchange.ui.components.audio-wave.views
  (:require
    [reagent.core :as r]
    [webchange.ui.components.audio-wave.constructor :refer [create-wavesurfer]]
    [webchange.ui.components.audio-wave.core :as core]
    [webchange.ui.components.audio-wave.controls :refer [get-controls]]
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn- handle-wave-surfer-ready
  [instances {:keys [on-ready region] :as props}]
  (core/subscribe-to-events instances props)
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
           (->> {:on-ready #(handle-wave-surfer-ready instances props)}
                (create-wavesurfer @element url)
                (reset! wave-surfer))

           (when (fn? ref)
             (-> (get-controls instances)
                 (ref)))))

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
