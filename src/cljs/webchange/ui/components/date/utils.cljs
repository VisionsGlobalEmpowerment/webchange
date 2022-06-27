(ns webchange.ui.components.date.utils)

(defn now
  []
  (js/Date.))

(defn yyyy-mm-dd
  [date]
  (-> (.toISOString date)
      (subs 0 10)))
