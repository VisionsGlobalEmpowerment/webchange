(ns webchange.editor-v2.lessons.views-lesson-form-scene-form
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]))

(defn- form-control
  [{:keys [text collapse?] :as props} control]
  [ui/grid {:xs   10
            :item true}
   (let [content [ui/grid {:container   true
                           :spacing     16
                           :align-items "center"
                           :justify     "center"}
                  [ui/grid {:xs   4
                            :item true}
                   [ui/typography {:variant "body1"
                                   :align   "right"}
                    text ":"]]
                  [ui/grid {:xs   7
                            :item true}
                   [ui/form-control {:full-width true
                                     :margin     "dense"}
                    control]]]]
     (if (contains? props :collapse?)
       [ui/collapse {:in              collapse?
                     :timeout         "auto"
                     :unmount-on-exit true
                     :style           {:width "100%"}}
        content]
       content))])

(defn- edit-scene-form
  [{:keys [data scenes-list]}]
  [ui/grid {:container   true
            :spacing     16
            :align-items "center"
            :justify     "center"}
   [form-control {:text "Activity"}
    [ui/select {:value     (or (:activity @data) "")
                :on-change #(swap! data assoc :activity (-> % .-target .-value))}
     (for [[item-key item] scenes-list]
       ^{:key (name item-key)}
       [ui/menu-item {:value (name item-key)} (:name item)])]]
   [form-control {:text "Time Expected"}
    [ui/text-field {:value      (or (:time-expected @data) "")
                    :on-change  #(swap! data assoc :time-expected (-> % .-target .-value js/parseInt))
                    :variant    "outlined"
                    :InputProps {:end-adornment (r/as-element [ui/input-adornment {:position "end"} "sec"])}}]]
   [form-control {:text "Scored"}
    [ui/switch {:checked   (or (:scored @data) false)
                :on-change #(swap! data assoc :scored (-> % .-target .-checked))
                :color     "primary"}]]
   [form-control {:text      "Expected Score"
                  :collapse? (:scored @data)}
    [ui/text-field {:value      (or (:expected-score-percentage @data) "")
                    :on-change  #(swap! data assoc :expected-score-percentage (-> % .-target .-value js/parseInt))
                    :variant    "outlined"
                    :InputProps {:end-adornment (r/as-element [ui/input-adornment {:position "end"} "%"])}}]]])

(defn edit-scene-modal
  [{:keys [open? scene-data scenes-list on-close on-save]}]
  (r/with-let [data (r/atom scene-data)]
              (let [handle-close #(on-close (empty? scene-data))]
                [ui/dialog
                 {:open     open?
                  :on-close handle-close}
                 [:form
                  [ui/dialog-title "Edit Scene"]
                  [ui/dialog-content
                   [edit-scene-form {:data        data
                                     :scenes-list scenes-list}]]
                  [ui/dialog-actions
                   [ui/button {:on-click handle-close} "Cancel"]
                   [ui/button {:type     "submit"
                               :variant  "contained"
                               :color    "secondary"
                               :on-click #(do (.preventDefault %)
                                              (on-save @data)
                                              (on-close))}
                    "Save"]]]])))
