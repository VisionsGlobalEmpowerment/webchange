(ns webchange.editor-v2.fix-lodash
  (:require
    [lodash :refer [_] :as lodash]))

(defn fix-lodash
  []
  (doseq [field (js->clj (.keys js/Object _))]
    (aset lodash field (aget _ field))))

(fix-lodash)
