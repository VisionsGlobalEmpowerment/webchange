(ns webchange.editor-v2.wizard.views
  (:require
    [webchange.editor-v2.wizard.views-game-changer :as game-changer]
    [webchange.editor-v2.wizard.views-wizard :as wizard]
    [webchange.editor-v2.wizard.views-wizard-configured :as wizard-configured]))

(def game-changer-panel game-changer/game-changer-panel)
(def wizard wizard/wizard)
(def wizard-configured wizard-configured/wizard)
