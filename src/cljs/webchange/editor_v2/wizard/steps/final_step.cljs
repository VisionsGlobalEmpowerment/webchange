(ns webchange.editor-v2.wizard.steps.final-step
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.state.activity :as state-activity]
    [webchange.editor-v2.wizard.state.course :as state-course]
    [webchange.editor-v2.wizard.steps.common :refer [progress-block]]))

(defn- get-styles
  []
  {:icon-container    {:position   "relative"
                       :text-align "center"}
   :icon              {:color     "#5f5f5f"
                       :font-size "170px"
                       :opacity   0.1}
   :content-container {:position        "absolute"
                       :top             0
                       :left            0
                       :width           "100%"
                       :height          "168px"
                       :display         "flex"
                       :flex-direction  "column"
                       :align-items     "center"
                       :justify-content "center"
                       :padding-top     "20px"}
   :button            {:margin "16px"}})

(defn- get-course-data
  [data]
  {:name            (get-in data [:activity-data :course])
   :lang            (get-in data [:activity-data :language])
   :concept-list-id (get-in data [:skills :concept])})

(defn- get-activity-data
  [data]
  (merge {:name        (get-in data [:activity-data :name])
          :skills      (get-in data [:skills :skills])
          :template-id (get-in data [:template-id])}
         (get-in data [:template-data])))

(defn- save-course
  [course-data]
  (js/Promise. (fn [resolve]
                 (re-frame/dispatch [::state-course/create-course course-data resolve]))))

(defn- save-activity
  [activity-data course-slug]
  (js/Promise. (fn [resolve]
                 (re-frame/dispatch [::state-activity/create-activity course-slug activity-data resolve]))))

(defn- save-data
  [data callback]
  (let [course-data (get-course-data data)
        activity-data (get-activity-data data)]
    (-> (save-course course-data)
        (.then (fn [new-course]
                 (save-activity activity-data (:slug new-course))))
        (.then callback))))

(defn final-form
  [{:keys [data]}]
  (r/with-let [state (r/atom {:saving? true})
               saved-activity (atom nil)
               _ (save-data @data (fn [activity]
                                    (swap! state assoc :saving? false)
                                    (reset! saved-activity activity)))
               handle-edit-click (fn []
                                   (let [course-slug (:course-slug @saved-activity)
                                         scene-slug (:scene-slug @saved-activity)]
                                     (re-frame/dispatch [::state-course/redirect-to-editor course-slug scene-slug])))]
    (let [styles (get-styles)]
      (if (:saving? @state)
        [progress-block {:title "Saving your data..."}]
        [:div {:style (:icon-container styles)}
         [ic/check-circle {:style (:icon styles)}]
         [:div {:style (:content-container styles)}
          [ui/typography {:variant "h4"} "Activity Saved"]
          [ui/button {:on-click handle-edit-click
                      :color    "primary"
                      :variant  "contained"
                      :style    (:button styles)}
           "Edit dialogs"]]]))))

(def data
  {:header     "Finish"
   :sub-header "Activity creation completed"
   :component  final-form})
