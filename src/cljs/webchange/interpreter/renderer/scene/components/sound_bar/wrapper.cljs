(ns webchange.interpreter.renderer.scene.components.sound-bar.wrapper
  (:require
    [webchange.interpreter.renderer.scene.app :refer [add-ticker remove-ticker]]
    [webchange.interpreter.renderer.scene.components.sound-bar.utils-mask :refer [update-mask!]]
    [webchange.interpreter.renderer.scene.components.sound-bar.utils-value :refer [update-value!]]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn wrap
  [type name object state]
  (create-wrapper {:name       name
                   :type       type
                   :object     object
                   :activate   (fn [{:keys [callback]}]
                                 (->> (fn []
                                        (update-value! state)
                                        (update-mask! state))
                                      (swap! state assoc :timer-callback))
                                 (add-ticker (:timer-callback @state))
                                 (callback))
                   :deactivate (fn [{:keys [callback]}]
                                 (remove-ticker (:timer-callback @state))
                                 (callback))}))
