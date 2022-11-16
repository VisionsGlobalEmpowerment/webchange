(ns webchange.interpreter.renderer.scene.modes.editor-mode
  (:require
    [webchange.interpreter.renderer.scene.modes.editor-frame :refer [add-editor-frame]]))

(defn- modify-common-props
  [{:keys [editable?] :as props}]
  (cond-> props
          editable? (assoc :draggable false)
          editable? (dissoc :on-drag-start)
          editable? (dissoc :on-drag-end)
          true (dissoc :filters)
          true (dissoc :on-click)))

(defn enable-mode-props
  [{:keys [type] :as props}]
  (-> (case type
        "animation" (-> props (assoc :start false))
        "carousel" (-> props (assoc :start false))
        "text" (-> props (dissoc :chunks))
        props)
      (modify-common-props)))

(defn enable-mode-helpers!
  [wrapper object-props params]
  (add-editor-frame wrapper object-props params))
