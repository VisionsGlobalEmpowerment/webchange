(ns webchange.editor-v2.graph-builder.graph.data.book.dialog--read-title)

(def data {:read-title-4 {:data        {:phrase-text "My first words book."}
                          :path        [:read-title 4]
                          :entity      :action
                          :children    []
                          :connections #{{:previous :read-title-3
                                          :name     "next"
                                          :sequence :read-title
                                          :handler  :read-title-5}}}
           :read-title-3 {:data        {:phrase-text "“My first words book”"}
                          :path        [:read-title 3]
                          :entity      :action
                          :children    []
                          :connections #{{:previous :read-title-2
                                          :name     "next"
                                          :sequence :read-title
                                          :handler  :read-title-4}}}
           :read-title-6 {:data        {:phrase-text "Touch the arrow to turn the page."}
                          :path        [:read-title 6]
                          :entity      :action
                          :children    []
                          :connections #{}}
           :read-title-5 {:data        {:phrase-text "Vamos a leerlo juntos!"}
                          :path        [:read-title 5]
                          :entity      :action
                          :children    []
                          :connections #{{:previous :read-title-4
                                          :name     "next"
                                          :sequence :read-title
                                          :handler  :read-title-6}}}
           :read-title-0 {:data        {:phrase-text "Do you see this title?"}
                          :path        [:read-title 0]
                          :entity      :action
                          :children    []
                          :connections #{{:previous :root
                                          :name     "next"
                                          :sequence :read-title
                                          :handler  :read-title-1}}}
           :read-title-1 {:data        {:phrase-text "“My first words book”"},
                          :path        [:read-title 1]
                          :entity      :action
                          :children    []
                          :connections #{{:previous :read-title-0
                                          :name     "next"
                                          :sequence :read-title
                                          :handler  :read-title-2}}}
           :read-title-2 {:data        {:phrase-text "The title of this book is"}
                          :path        [:read-title 2]
                          :entity      :action
                          :children    []
                          :connections #{{:previous :read-title-1
                                          :name     "next"
                                          :sequence :read-title
                                          :handler  :read-title-3}}}})
