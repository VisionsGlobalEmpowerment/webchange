(ns webchange.editor-v2.graph-builder.graph.data.library.scene-translation)

(def data {:books         {:path        [:books]
                           :entity      :object
                           :children    []
                           :connections #{{:handler :welcome
                                           :name    "next"}}}
           :welcome       {:path        [:welcome]
                           :entity      :action
                           :children    []
                           :connections #{{:handler :vera-agree
                                           :name    "next"}}}
           :start-reading {:path        [:start-reading]
                           :entity      :action
                           :children    []
                           :connections #{}}
           :vera-agree    {:path        [:vera-agree]
                           :entity      :action
                           :children    []
                           :connections #{{:handler :start-reading
                                           :name    "next"}}}})
