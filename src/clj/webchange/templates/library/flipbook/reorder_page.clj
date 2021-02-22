(ns webchange.templates.library.flipbook.reorder-page
  (:require
    [clojure.tools.logging :as log]
    [webchange.templates.library.flipbook.page-number :refer [update-pages-numbers]]
    [webchange.templates.library.flipbook.stages :refer [update-stages]]
    [webchange.templates.library.flipbook.utils :refer [get-book-object-name get-pages-count stage-number->page-number]]
    [webchange.utils.list :refer [move-item]]))

(defn- available-target-position?
  [target-position pages-count]
  (and (number? target-position)
       (>= target-position 0)
       (< target-position pages-count)))

(defn move-page
  [activity-data {:keys [page-side stage target]} page-params]
  (let [book-name (get-book-object-name activity-data)
        page-number (stage-number->page-number activity-data stage page-side)
        pages-count (get-pages-count activity-data)
        target-position (case target
                          "forward" (inc page-number)
                          "backward" (dec page-number)
                          nil)]
    (if (available-target-position? target-position pages-count)
      (-> activity-data
          (update-in [:objects (keyword book-name) :pages] move-item page-number target-position)
          (update-stages {:book-name book-name})
          (update-pages-numbers page-params))
      activity-data)))
