(ns webchange.editor-v2.translator.translator-form.subs
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.db :refer [path-to-db]]
    [webchange.editor-v2.translator.translator-form.utils :refer [get-current-action-data]]
    [webchange.editor-v2.translator.subs :as translator-subs]))

(re-frame/reg-sub
  ::current-action-data
  (fn []
    [(re-frame/subscribe [::translator-subs/selected-action])
     (re-frame/subscribe [::translator-subs/current-concept])
     (re-frame/subscribe [::translator-subs/phrase-translation-actions-data])])
  (fn [[selected-action-node current-concept data-store]]
    (get-current-action-data selected-action-node current-concept data-store)))
