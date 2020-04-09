(ns webchange.editor-v2.lessons.views-lesson-form
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.lessons.views-lesson-form-datasets :refer [lesson-datasets]]
    [webchange.editor-v2.lessons.views-lesson-form-main-data :refer [lesson-main-data]]
    [webchange.editor-v2.lessons.views-lesson-form-scenes :refer [lesson-scenes]]))

(defn- get-styles
  []
  {:block       {:padding "16px"}
   :block-title {:margin-bottom "8px"}})

(defn lesson-form
  [{:keys [data dataset-items lesson-scheme level-scheme scenes-list]}]
  (let [styles (get-styles)]
    [ui/grid {:container true
              :spacing   40}
     [ui/grid {:item true :xs 12}
      [ui/paper {:style (:block styles)}
       [lesson-main-data {:data         data
                          :level-scheme level-scheme}]]]
     [ui/grid {:item true :xs 3}
      [ui/paper {:style (:block styles)}
       [ui/typography {:variant "h5"
                       :style   (:block-title styles)}
        "Datasets"]
       [lesson-datasets {:data          data
                         :dataset-items dataset-items
                         :lesson-scheme lesson-scheme}]]]
     [ui/grid {:item true :xs 9}
      [ui/paper {:style (:block styles)}
       [ui/typography {:variant "h5"
                       :style   (:block-title styles)}
        "Scenes"]
       [lesson-scenes {:data        data
                       :scenes-list scenes-list}]]]]))
