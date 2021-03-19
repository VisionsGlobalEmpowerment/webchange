(ns webchange.sandbox)

(comment
  (let [course-slug "test"
        lesson-sets {:concepts-single ["a" "b" "c"]}

        course ()]
    )
  (def lessons {:concepts-single {:dataset-id 4 :item-ids []}})
  (-> lessons clj->js js/JSON.stringify js/btoa js/encodeURIComponent))
