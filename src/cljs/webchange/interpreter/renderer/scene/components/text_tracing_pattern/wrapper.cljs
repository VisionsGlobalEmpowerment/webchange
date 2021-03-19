(ns webchange.interpreter.renderer.scene.components.text-tracing-pattern.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]
    [webchange.logger.index :as logger]
    [webchange.interpreter.core :as i]))

(defn tool-transition-id
  [name tool-name]
  (str name "-tool-" tool-name))

(defn wrap
  [type name container state]
  (create-wrapper {:name          name
                   :type          type
                   :object        container
                   :set-highlight (fn [value]
                                    (doall
                                      (for [[tool-name highlight] value]
                                        (let [tool-type (keyword tool-name)
                                              tool (get-in @state [:tools tool-type])
                                              id (tool-transition-id name tool-name)]
                                          (logger/trace "highlight: " tool-type highlight tool)
                                          (i/interpolate {:id        id
                                                          :component tool
                                                          :from      {:brightness 0},
                                                          :to        {:brightness 0.35}
                                                          :params    {:yoyo true :duration 0.5}})
                                          (js/setTimeout #(i/kill-transition! id) 3000)))))}))
