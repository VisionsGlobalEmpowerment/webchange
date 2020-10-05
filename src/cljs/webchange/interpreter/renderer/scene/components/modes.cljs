(ns webchange.interpreter.renderer.scene.components.modes
  (:require
    [webchange.interpreter.renderer.scene.components.editor-mode :as editor-mode]))

(def available-modes [::game ::sandbox ::editor])

(defn enable-mode
  [mode props]
  (case mode
    ::editor (editor-mode/enable props)
    props))

(defn show-overlays?
  [mode]
  (some #{mode} [::game]))

(defn start-on-ready?
  [mode]
  (some #{mode} [::game ::sandbox]))

(defn fullscreen?
  [mode]
  (some #{mode} [::game]))
