(ns webchange.interpreter.renderer.scene.modes.modes)

(def available-modes [::game ::sandbox ::editor])

(defn show-overlays?
  [mode]
  (some #{mode} [::game]))

(defn start-on-ready?
  [mode]
  (some #{mode} [::game ::sandbox]))

(defn fullscreen?
  [mode]
  (some #{mode} [::game]))
