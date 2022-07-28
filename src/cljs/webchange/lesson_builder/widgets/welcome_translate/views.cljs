(ns webchange.lesson-builder.widgets.welcome-translate.views
  (:require
    [webchange.lesson-builder.components.instruction-cards.views :refer [instruction-cards]]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]))

(defn welcome-translate
  []
  [toolbox {:title "Voice & Translate Steps:"
            :icon  "translate"}
   [instruction-cards {:data               [{:content [:<>
                                                       "Select a Script Dialogue"]}
                                            {:content [:<>
                                                       "Upload an audio file or directly record your voice"]}
                                            {:content [:<>
                                                       "Select the audio file"]}
                                            {:content [:<>
                                                       "Click auto select for the system to recognize the words from the script and match them to the audio"]}
                                            {:content [:<>
                                                       "Play to verify the audio selection then click “apply“"]}]
                       :background-variant 2
                       :show-indexes?      true}]])
