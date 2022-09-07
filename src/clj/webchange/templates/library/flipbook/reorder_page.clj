(ns webchange.templates.library.flipbook.reorder-page
  (:require
    [webchange.templates.library.flipbook.recalculate-data :refer [recalculate-data]]
    [webchange.templates.library.flipbook.utils :refer [get-book-object-name get-pages-count stage-number->page-number]]
    [webchange.utils.list :refer [move-item]]))

(defn- available-target-position?
  [target-position pages-count]
  (and (number? target-position)
       (>= target-position 0)
       (< target-position pages-count)))

(defn move-page
  [activity-data {:keys [page-idx-from page-idx-to]} _]
  (let [book-name (-> activity-data get-book-object-name keyword)
        pages-count (get-pages-count activity-data)]
    (if (available-target-position? page-idx-to pages-count)
      (-> activity-data
          (update-in [:objects book-name :pages] move-item page-idx-from page-idx-to)
          (recalculate-data book-name))
      activity-data)))
