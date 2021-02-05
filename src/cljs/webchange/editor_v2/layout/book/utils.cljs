(ns webchange.editor-v2.layout.book.utils)

(defn- page-idx->data
  [scene-data page-idx flipbook-name]
  (-> scene-data
      (get-in [:objects (keyword flipbook-name) :pages])
      (nth page-idx)))

(defn get-stage-data
  [scene-data stage-idx]
  (when (some? stage-idx)
    (let [{:keys [stages flipbook-name]} (get scene-data :metadata {})
          stage (nth stages stage-idx)
          [left-page-idx right-page-idx] (:pages-idx stage)]
      (cond-> stage
              (some? left-page-idx) (assoc :left-page (page-idx->data scene-data left-page-idx flipbook-name))
              (some? right-page-idx) (assoc :right-page (page-idx->data scene-data right-page-idx flipbook-name))))))
