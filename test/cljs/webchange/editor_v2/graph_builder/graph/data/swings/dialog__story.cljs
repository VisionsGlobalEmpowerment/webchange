(ns webchange.editor-v2.graph-builder.graph.data.swings.dialog--story)

(def data {:swings-dialog-action-3 {:data        {:phrase-text "Ah, es una ardilla!"}
                                    :path        [:swings-dialog-action 3]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:previous :swings-dialog-action-2
                                                    :name     "next"
                                                    :sequence :swings-dialog-action
                                                    :handler  :swings-dialog-action-4}}}
           :swings-dialog-action-2 {:data        {:phrase-text "Si! Cuál es el nombre del animal con ojos grandes y una cola tupida?"}
                                    :path        [:swings-dialog-action 2]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:previous :swings-dialog-action-1
                                                    :name     "next"
                                                    :sequence :swings-dialog-action
                                                    :handler  :swings-dialog-action-3}}}
           :swings-dialog-action-4 {:data        {:phrase-text "Una ardilla! Eso pensé! Has visto antes una ardilla?"}
                                    :path        [:swings-dialog-action 4]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:previous :swings-dialog-action-3
                                                    :name     "next"
                                                    :sequence :swings-dialog-action
                                                    :handler  :swings-dialog-action-5}}}
           :swings-dialog-action-7 {:data        {:phrase-text "Si? Nueces, frutas y semillas? A mi me gusta las nueces y las frutas. Talvez pueda probar las semillas tambien"}
                                    :path        [:swings-dialog-action 7]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:previous :swings-dialog-action-6
                                                    :name     "next"
                                                    :sequence :swings-dialog-action
                                                    :handler  :swings-dialog-action-8}}}
           :swings-dialog-action-6 {:data        {:phrase-text "Ahhhh. A las ardillas les gusta treparse a los árboles. También les gusta comer nueces, frutas y semillas."}
                                    :path        [:swings-dialog-action 6]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:previous :swings-dialog-action-5
                                                    :name     "next"
                                                    :sequence :swings-dialog-action
                                                    :handler  :swings-dialog-action-7}}}
           :swings-dialog-action-8 {:data        {:phrase-text "Jajaja. Diviértete jugando con la a, a, a, ardilla!"}
                                    :path        [:swings-dialog-action 8]
                                    :entity      :action
                                    :children    []
                                    :connections #{}}
           :swings-dialog-action-5 {:data        {:phrase-text "Mmmm, si! He visto una ardilla. Una vez vi una ardilla treparse en un arbol."}
                                    :path        [:swings-dialog-action 5]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:previous :swings-dialog-action-4
                                                    :name     "next"
                                                    :sequence :swings-dialog-action
                                                    :handler  :swings-dialog-action-6}}}
           :swings-dialog-action-0 {:data        {:phrase-text "Eso se ve divertido! A quién estás empujando en el columpio?"}
                                    :path        [:swings-dialog-action 0]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:previous :root
                                                    :name     "next"
                                                    :sequence :swings-dialog-action
                                                    :handler  :swings-dialog-action-1}}}
           :swings-dialog-action-1 {:data        {:phrase-text "A quien estoy empujando?"}
                                    :path        [:swings-dialog-action 1]
                                    :entity      :action
                                    :children    []
                                    :connections #{{:previous :swings-dialog-action-0
                                                    :name     "next"
                                                    :sequence :swings-dialog-action
                                                    :handler  :swings-dialog-action-2}}}})
