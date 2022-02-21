(ns webchange.concept
  (:require
    [webchange.dataset.core :as dataset]))

(defn get-dataset-items
  [course-slug]
  (->> (dataset/get-course-lessons course-slug)
       (:items)))

(defn find-dataset-item
  [dataset-items {:keys [id]}]
  (some #(and (= (:id %) id) %) dataset-items))

(defn get-item-fields
  [dataset-item fields]
  (->> (get dataset-item :data [])
       (filter (fn [[field-name _]]
                 (some #{field-name} fields)))
       (into {})))

(comment
  "Get concept fields for test"

  (-> (get-dataset-items "english")
      (find-dataset-item {:id 78})
      (get-item-fields [:dialog-field-8d4ef5e3-6e37-4a69-988f-de8cf45903f9
                        :dialog-field-136fa9c0-4bde-45a0-a7aa-f933ab1cbdeb
                        :dialog-field-45d4b490-d167-4992-bf64-3487e8dbd862
                        :dialog-field-e51a7576-061f-4e94-bbd6-936fc7df466c
                        :dialog-field-7dcb4949-4ae7-4947-bdb9-e744399aef73
                        :dialog-field-fbc892e9-258e-4a27-be9e-eb4d7011d394
                        :dialog-field-2b1330ce-7745-4a8e-87cc-7e77398939b2
                        :dialog-field-02c7dada-5129-47cd-9f16-31298a67dca9])))