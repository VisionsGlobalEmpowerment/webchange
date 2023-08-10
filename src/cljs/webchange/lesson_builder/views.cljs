(ns webchange.lesson-builder.views
  (:require
    [camel-snake-kebab.core :refer [->kebab-case-keyword]]
    [camel-snake-kebab.extras :refer [transform-keys]]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.layout.views :refer [layout]]
    [webchange.lesson-builder.state :as state]))

(defn index
  [props]
  (re-frame/dispatch [::state/init (transform-keys ->kebab-case-keyword props)])
  (fn []
    [layout]))
