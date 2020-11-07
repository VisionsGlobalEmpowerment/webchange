(ns webchange.editor-v2.lessons.views-lesson-form-main-data
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn lesson-main-data
  [{:keys [data level-scheme]}]
  [ui/grid {:container true
            :spacing   32}
   [ui/grid {:item true :xs 3}
    [ui/typography {:variant "body1"}
     "Name:"]
    [ui/form-control {:full-width true
                      :margin     "dense"}
     [ui/text-field {:variant       "outlined"
                     :full-width    true
                     :default-value (:name @data)
                     :on-change     #(swap! data assoc :name (-> % .-target .-value))}]]]
   [ui/grid {:item true :xs 3}
    [ui/typography {:variant "body1"}
     "Type:"]
    [ui/form-control {:full-width true
                      :margin     "dense"}
     [ui/select {:value     (:type @data)
                 :on-change #(swap! data assoc :type (-> % .-target .-value))
                 :style     {:min-width "150px"}}
      (for [[lesson-type lesson-type-data] level-scheme]
        ^{:key lesson-type}
        [ui/menu-item {:value lesson-type}
         (:name lesson-type-data)])]]]])
