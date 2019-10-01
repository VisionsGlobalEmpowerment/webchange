(ns webchange.editor-v2.diagram-builder.colors)

(def colors {:objects {:global  "#8063ff"
                       :default "#FFC855"}
             :actions {:audio   "#008A66"
                       :default "#CCCCCC"}
             :default "#FF0000"})

(defn get-color
  [asset type]
  (let [asset-colors (get colors asset)]
    (if-not (nil? asset-colors)
      (or (get asset-colors type) (:default asset-colors))
      (:default colors))))

(defn get-object-color
  ([]
   (get-color :objects nil))
  ([object-type]
   (get-color :objects object-type)))

(defn get-action-color
  ([]
   (get-color :actions nil))
  ([action-type]
   (get-color :actions action-type)))
