(ns webchange.templates.library.flipbook.reorder-page
  (:require
    [webchange.templates.library.flipbook.stages :refer [update-stages]]
    [webchange.templates.library.flipbook.utils :refer [get-book-object-name get-pages-count stage-number->page-number]]
    [webchange.utils.list :refer [move-item]]))

(defn- available-target-position?
  [target-position pages-count]
  (and (number? target-position)
       (>= target-position 0)
       (< target-position pages-count)))

(defn move-page
  [activity-data {:keys [page-idx-from page-idx-to]} page-params]
  (let [book-name (get-book-object-name activity-data)
        pages-count (get-pages-count activity-data)]
    (if (available-target-position? page-idx-to pages-count)
      (-> activity-data
          (update-in [:objects (keyword book-name) :pages] move-item page-idx-from page-idx-to)
          (update-stages {:book-name book-name}))
      activity-data)))
