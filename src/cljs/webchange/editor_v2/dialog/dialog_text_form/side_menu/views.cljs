(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.views
  (:require
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.views :refer [main]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.sections.views :refer [sections]]))

(defn side-menu
  []
  [:div.side-menu
   [sections]
   [main]])
