(ns webchange.templates.common-actions-guide
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.utils.dialog :as dialog]))

(def init-guide-action-name :init-guide)
(def init-guide-trigger-name :guide)
(def tap-instructions-action-name :tap-instructions)
(def tap-instructions-dialog-action-name :dialog-tap-instructions)
(def tap-instructions-var-name "tap-instructions-action")
(def timeout-instructions-action-name :timeout-instructions)
(def timeout-instructions-dialog-action-name :dialog-timeout-instructions)
(def timeout-instructions-var-name "timeout-instructions-action")

(defn- add-action-safely
  [scene-data action-name action-data]
  (if (-> (:actions scene-data) (contains? action-name) (not))
    (assoc-in scene-data [:actions action-name] action-data)
    scene-data))

(defn- add-trigger-safely
  [scene-data trigger-name event handler]
  (if (-> (:triggers scene-data) (contains? trigger-name) (not))
    (assoc-in scene-data [:triggers trigger-name] {:on event :action handler})
    scene-data))

(defn with-init-guide
  [scene-data]
  (add-action-safely scene-data
                     init-guide-action-name
                     {:type "parallel"
                      :data [{:type      "set-variable"
                              :var-name  tap-instructions-var-name
                              :var-value (clojure.core/name tap-instructions-dialog-action-name)}
                             {:type      "set-variable"
                              :var-name  timeout-instructions-var-name
                              :var-value (clojure.core/name timeout-instructions-dialog-action-name)}]}))

(defn with-init-trigger
  [scene-data]
  (add-trigger-safely scene-data
                      init-guide-trigger-name
                      "start"
                      (clojure.core/name init-guide-action-name)))

(defn with-tap-instructions
  [scene-data]
  (add-action-safely scene-data
                     tap-instructions-action-name
                     {:type     "action"
                      :from-var [{:action-property "id"
                                  :var-name        tap-instructions-var-name}]}))

(defn with-tap-instructions-dialog
  [scene-data]
  (add-action-safely scene-data
                     tap-instructions-dialog-action-name
                     (dialog/default "Tap instructions")))

(defn with-timeout-instructions
  [scene-data]
  (add-action-safely scene-data
                     timeout-instructions-action-name
                     {:type     "action"
                      :from-var [{:action-property "id"
                                  :var-name        timeout-instructions-var-name}]}))

(defn with-timeout-instructions-dialog
  [scene-data]
  (add-action-safely scene-data
                     timeout-instructions-dialog-action-name
                     (dialog/default "Timeout instructions")))

(defn with-guide-actions
  [scene-data]
  (-> scene-data
      (with-init-guide)
      (with-init-trigger)
      (with-tap-instructions)
      (with-tap-instructions-dialog)
      (with-timeout-instructions)
      (with-timeout-instructions-dialog)))

(defn update-settings
  [scene-data guide-settings-patch]
  (update-in scene-data [:metadata :guide-settings] merge guide-settings-patch))
