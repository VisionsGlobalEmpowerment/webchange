(ns webchange.lesson-builder.widgets.welcome.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.components.instruction-cards.views :refer [instruction-cards]]
    [webchange.lesson-builder.components.toolbox.views :refer [toolbox]]
    [webchange.lesson-builder.state :as state]
    [webchange.lesson-builder.widgets.pages.views :refer [activity-pages]]))

(defn welcome
  []
  (let [flipbook? @(re-frame/subscribe [::state/flipbook?])]
    (cond
      flipbook? [activity-pages]
      :default [toolbox {:title "Welcome To The Lesson Builder!"
                         :icon  "create"}
                [instruction-cards {:data [{:icon    "build"
                                            :content [:<>
                                                      "In the right sidebar, use the "
                                                      [:b "Build"]
                                                      " section to add any new elements to the activity"]}
                                           {:icon    "levels"
                                            :content [:<>
                                                      "Many components have already been filled in for you. To make change, go to "
                                                      [:b "Scene Layers"]
                                                      " and click edit"]}
                                           {:icon    "translate"
                                            :content [:<>
                                                      "Within the script you can add dialogue and then use the "
                                                      [:b "Voice & Translate"]
                                                      " section to add the audio"]}
                                           {:icon    "navigation/play"
                                            :content [:<>
                                                      "For more tips on building activities check out our help videos"]
                                            :action  {:text "See Videos"
                                                      :href nil}}]}]])))
