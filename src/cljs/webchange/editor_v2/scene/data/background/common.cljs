(ns webchange.editor-v2.scene.data.background.common)

(def available-layers {:single  [{:type  :single-background
                                  :title "Background"}]
                       :layered [{:type  :background
                                  :title "Background"}
                                 {:type  :surface
                                  :title "Surface"}
                                 {:type  :decoration
                                  :title "Decorations"}]})