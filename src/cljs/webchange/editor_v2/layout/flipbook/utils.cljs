(ns webchange.editor-v2.layout.flipbook.utils)

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

(defn- page-data->text-object-data
  [page scene-data]
  (let [text-name (-> page (get :text) (keyword))]
    (when (some? text-name)
      [text-name (get-in scene-data [:objects text-name])])))

(defn scene-data->objects-list
  [scene-data stage-idx]
  (let [{:keys [left-page right-page]} (get-stage-data scene-data stage-idx)]
    (->> (cond-> []
                 (some? left-page) (conj (page-data->text-object-data left-page scene-data))
                 (some? right-page) (conj (page-data->text-object-data right-page scene-data)))
         (remove nil?))))
