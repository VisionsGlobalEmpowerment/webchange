(ns webchange.sandbox)

(comment
  (def lessons {:concepts-single {:dataset-id 4 :item-ids []}})
  (-> lessons clj->js js/JSON.stringify js/btoa js/encodeURIComponent))
