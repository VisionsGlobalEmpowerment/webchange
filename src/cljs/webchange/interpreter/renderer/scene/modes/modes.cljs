(ns webchange.interpreter.renderer.scene.modes.modes)

(def available-modes [::game ::game-with-nav ::sandbox ::editor ::book-reader])

(defn get-mode
  [mode-name]
  (case mode-name
    "game-with-nav" ::game-with-nav
    nil))

(defn show-overlays?
  [mode]
  (some #{mode} [::game ::sandbox]))

(defn start-on-ready?
  [mode]
  (some #{mode} [::game ::game-with-nav ::sandbox]))

(defn fullscreen?
  [mode]
  (some #{mode} [::game]))
