(ns webchange.templates.utils.image)

(defn create
  [data {:keys [name editable]}]
  {(keyword name) {:type      "image",
                   :x         500
                   :y         500
                   :scale-x   1,
                   :scale-y   1,
                   :editable? editable
                   :src       (:src data)}}
  )