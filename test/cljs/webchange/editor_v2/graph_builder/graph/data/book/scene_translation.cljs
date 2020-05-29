(ns webchange.editor-v2.graph-builder.graph.data.book.scene-translation)

(def data {:read-page-current-concept {:path        [:read-page-current-concept]
                                       :entity      :action
                                       :children    []
                                       :connections #{}}
           :start                     {:path        [:start]
                                       :entity      :trigger
                                       :children    []
                                       :connections #{{:handler :read-title
                                                       :name    "next"}}}
           :read-title                {:path        [:read-title]
                                       :entity      :action
                                       :children    [:read-title-0
                                                     :read-title-1
                                                     :read-title-2
                                                     :read-title-3
                                                     :read-title-4
                                                     :read-title-5
                                                     :read-title-6]
                                       :connections #{}}
           :next-page-arrow           {:path        [:next-page-arrow]
                                       :entity      :object
                                       :children    []
                                       :connections #{{:handler :read-page-current-concept
                                                       :name    "next"}}}})
