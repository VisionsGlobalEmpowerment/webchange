(ns webchange.interpreter.renderer.scene.modes.enable
  (:require
    [webchange.interpreter.renderer.scene.modes.editor-mode :as editor-mode]
    [webchange.interpreter.renderer.scene.modes.modes :as modes]))

(defn enable-mode-props
  [mode props]
  (case mode
    ::modes/editor (editor-mode/enable-mode-props props)
    props))

(defn enable-mode-helpers!
  [mode object object-props params]
  (case mode
    ::modes/editor (editor-mode/enable-mode-helpers! object object-props params)
    nil))
