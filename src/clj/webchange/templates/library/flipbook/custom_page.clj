(ns webchange.templates.library.flipbook.custom-page)

(defn- get-page-name
  []
  (->> (java.util.UUID/randomUUID) (.toString) (str "page-")))

(defn- page-name->image-name
  [page-name]
  (-> page-name (str "-image")))

(defn- page-name->text-name
  [page-name]
  (-> page-name (str "-text")))

(defn create
  [page-params {:keys [page-type image-src text]}]
  (let [page-name (get-page-name)
        image-name (page-name->image-name page-name)
        text-name (page-name->text-name page-name)
        default-props {:group {:type       "group"
                               :transition ""
                               :children   [""]}
                       :text  {:type           "text"
                               :word-wrap      true
                               :vertical-align "top"
                               :fill           "white"
                               :text           text}
                       :image {:type   "image"
                               :x      0
                               :y      0
                               :width  (:width page-params)
                               :height (:height page-params)
                               :src    image-src}}
        current-props (case page-type
                        :text-big-at-bottom (-> default-props
                                                (update :group merge {:transition page-name
                                                                      :children   [image-name text-name]})
                                                (update :text merge {:x     (:padding page-params)
                                                                     :y     (* (:height page-params) 0.6)
                                                                     :width (- (:width page-params) (* (:padding page-params) 2))}))
                        :text-small-at-bottom (-> default-props
                                                  (update :group merge {:transition page-name
                                                                        :children   [image-name text-name]})
                                                  (update :text merge {:x     (:padding page-params)
                                                                       :y     (* (:height page-params) 0.8)
                                                                       :width (- (:width page-params) (* (:padding page-params) 2))}))
                        :only-image (-> default-props
                                        (update :group merge {:transition page-name
                                                              :children   [image-name]})
                                        (dissoc :text))
                        :text-at-top (-> default-props
                                         (update :group merge {:transition page-name
                                                               :children   [image-name text-name]})
                                         (update :text merge {:x     (:padding page-params)
                                                              :y     (:padding page-params)
                                                              :width (- (:width page-params) (* (:padding page-params) 2))}))
                        :only-text (-> default-props
                                       (update :group merge {:transition page-name
                                                             :children   [text-name]})
                                       (update :text merge {:x     (:padding page-params)
                                                            :y     (:padding page-params)
                                                            :width (- (:width page-params) (* (:padding page-params) 2))})
                                       (dissoc :image)))]
    {:name      page-name
     :resources []
     :objects   (cond-> (assoc {} (keyword page-name) (:group current-props))
                        (some? (:image current-props)) (assoc (keyword image-name) (:image current-props))
                        (some? (:text current-props)) (assoc (keyword text-name) (:text current-props)))}))
