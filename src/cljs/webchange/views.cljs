(ns webchange.views
  (:require
   [re-frame.core :as re-frame]
   [webchange.subs :as subs]
   [webchange.interpreter.components :refer [course]]
   [webchange.editor.components :refer [editor]]
   ))

(defn main-panel []
  [course "test-course"])

(defn main-panel-editor []
  [editor])