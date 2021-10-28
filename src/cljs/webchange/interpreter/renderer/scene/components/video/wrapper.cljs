(ns webchange.interpreter.renderer.scene.components.video.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.video.utils :as v-utils]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.resources.manager :as resources]))

(defn wrap
  [type name sprite-object state]
  (create-wrapper {:name    name
                   :type    type
                   :object  sprite-object
                   :set-src (fn [src options]
                              (let [resource (resources/get-resource src)]
                                (when (nil? resource)
                                  (throw (js/Error. (str "Resources for '" src "' were not loaded"))))
                                (v-utils/set-src sprite-object resource (merge options
                                                                               {:volume (get-in @state [:props :volume])}))))
                   :play    (fn []
                              (v-utils/play-video sprite-object (get-in @state [:props :volume])))
                   :stop    (fn []
                              (v-utils/stop-video sprite-object))}))
