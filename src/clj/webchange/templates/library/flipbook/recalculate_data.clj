(ns webchange.templates.library.flipbook.recalculate-data
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.library.flipbook.display-names :refer [update-display-names]]
    [webchange.templates.library.flipbook.stages :refer [update-stages]]))

(defn recalculate-data
  [activity-data book-name]
  (-> activity-data
      (update-stages {:book-name book-name})
      (update-display-names)))
