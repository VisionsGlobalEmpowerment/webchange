(ns webchange.editor-v2.diagram.custom-diagram-items.custom-widget.common-colors)

(def colors {:object        {:default "#FFC855"}
             :global-object {:default "#156874"}
             :action        {:audio   "#008A66"
                             :default "#CCCCCC"}
             :default       "#CCCCCC"})

(defn get-color
  [entity type]
  (let [entity-colors (get colors entity)]
    (if-not (nil? entity-colors)
      (or (get entity-colors type) (:default entity-colors))
      (:default colors))))

(defn get-node-color
  [props]
  (let [entity (->> props .-entity keyword)
        type (->> props .-type keyword)]
    (get-color entity type)))
