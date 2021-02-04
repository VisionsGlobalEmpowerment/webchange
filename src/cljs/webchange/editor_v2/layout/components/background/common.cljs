(ns webchange.editor-v2.layout.components.background.common)

(def available-layers {:single  [{:type  :single-background
                                  :title "Background"}]
                       :layered [{:type  :background
                                  :title "Background"}
                                 {:type  :surface
                                  :title "Surface"}
                                 {:type  :decoration
                                  :title "Decorations"}]})