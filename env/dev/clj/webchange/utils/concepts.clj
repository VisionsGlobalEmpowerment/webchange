(ns webchange.utils.concepts
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
  "Get course dataset items"
  (->> (get-dataset-items "spanish")
       (map #(select-keys % [:id :name]))))

(comment
  "Get concept fields for test"

  (-> (get-dataset-items "spanish")
      (find-dataset-item {:id 5358})
      (get-item-fields [
                        :dialog-field-e3a767e4-93b8-4314-8724-768b8a5076d7
                        ;:dialog-field-8d4ef5e3-6e37-4a69-988f-de8cf45903f9
                        ;:dialog-field-136fa9c0-4bde-45a0-a7aa-f933ab1cbdeb
                        ;:dialog-field-60ec6231-da8f-4938-a5ab-169949319aba
                        ;:dialog-field-e51a7576-061f-4e94-bbd6-936fc7df466c
                        ;:dialog-field-5dcb1877-2384-4bc5-aba6-3ab0bacf53ed
                             ;:dialog-field-696fbc25-ff33-4a12-afcb-0b3dac637881
                        ;:dialog-field-02c7dada-5129-47cd-9f16-31298a67dca9
                        ;:dialog-field-a4bc9468-283e-4ab1-bdf4-baa58661a315
                        ])))