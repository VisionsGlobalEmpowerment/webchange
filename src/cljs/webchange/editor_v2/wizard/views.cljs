(ns webchange.editor-v2.wizard.views
  (:require
    [webchange.editor-v2.wizard.views-book-creator :as book-creator]
    [webchange.editor-v2.wizard.views-wizard :as wizard]
    [webchange.editor-v2.wizard.views-wizard-configured :as wizard-configured]))

(def book-creator-panel book-creator/book-creator-panel)
(def wizard wizard/wizard)
(def wizard-configured wizard-configured/wizard)
