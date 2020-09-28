(ns webchange.editor-v2.wizard.views-course
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.wizard.state.course :as state-course]
    [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:card               {:margin      "12px"
                        :flex-shrink "0"
                        :width       500}
   :form               {:padding-bottom "16px"}
   :text-input         {:margin-top "16px"}
   :control-container  {:margin-top "8px"}
   :validation-message {:position "absolute"
                        :bottom   "-12px"
                        :left     "20px"
                        :color    (get-in-theme [:palette :secondary :main])}
   :actions            {:padding "24px"}})

(def validation-map {:name            [(fn [value] (when (empty? value) "Name is required"))]
                     :lang            [(fn [value] (when (empty? value) "Language is required"))]
                     :concept-list-id [(fn [value] (when (= value "") "Concept list is required"))]})

(defn- validate
  [data validation-map]
  (doseq [[field validators] validation-map]
    (let [value (get @data field)
          result (->> validators
                      (map (fn [validator] (validator value)))
                      (remove empty?))]
      (if-not (empty? result)
        (swap! data assoc-in [:validation field] (clojure.string/join "; " result))
        (swap! data update :validation dissoc field)))))

(defn- get-error
  [data name]
  (get-in @data [:validation name]))

(defn- has-error
  [data name]
  (some? (get-error data name)))

(defn- valid?
  [data validation-map]
  (validate data validation-map)
  (empty? (get @data :validation)))

(defn- clean-data
  [data]
  (dissoc @data :validation))

(defn- course-info
  [{:keys [datasets]}]
  (r/with-let [data (r/atom {:name            ""
                             :lang            ""
                             :concept-list-id ""})]
              (let [handle-save (fn []
                                  (when (valid? data validation-map)
                                    (re-frame/dispatch [::state-course/create-course (clean-data data)])))
                    styles (get-styles)]
                [ui/card {:style (:card styles)}
                 [ui/dialog-title
                  "Create Course"]
                 [ui/card-content
                  [ui/grid {:container   true
                            :justify     "center"
                            :spacing     24
                            :align-items "center"
                            :style       (:form styles)}
                   [ui/grid {:item true :xs 10}
                    [ui/form-control {:full-width true
                                      :style      (:control-container styles)}
                     [ui/text-field {:label      "Name"
                                     :full-width true
                                     :variant    "outlined"
                                     :value      (:name @data)
                                     :on-change  #(swap! data assoc :name (-> % .-target .-value))
                                     :style      (:text-input styles)}]
                     (when (has-error data :name)
                       [ui/form-helper-text {:style (:validation-message styles)} (get-error data :name)])]
                    [ui/form-control {:full-width true
                                      :style      (:control-container styles)}
                     [ui/text-field {:label      "Language"
                                     :full-width true
                                     :variant    "outlined"
                                     :value      (:lang @data)
                                     :on-change  #(swap! data assoc :lang (-> % .-target .-value))
                                     :style      (:text-input styles)}]
                     (when (has-error data :lang)
                       [ui/form-helper-text {:style (:validation-message styles)} (get-error data :lang)])]
                    [ui/form-control {:full-width true
                                      :style      (:control-container styles)}
                     [ui/input-label "Concept list"]
                     [ui/select {:value     (:concept-list-id @data)
                                 :on-change #(swap! data assoc :concept-list-id (-> % .-target .-value))}
                      (for [dataset datasets]
                        ^{:key (:id dataset)}
                        [ui/menu-item {:value (:id dataset)} (:name dataset)])]
                     (when (has-error data :concept-list-id)
                       [ui/form-helper-text {:style (:validation-message styles)} (get-error data :concept-list-id)])]]]]
                 [ui/card-actions {:style (:actions styles)}
                  [ui/button {:color    "secondary"
                              :style    {:margin-left "auto"}
                              :on-click handle-save}
                   "Save"]]])))

(defn create-course-panel
  []
  (re-frame/dispatch [::state-course/load-datasets-library])
  (let [datasets @(re-frame/subscribe [::state-course/datasets-library])]
    [layout {:title "Course"
             :align "center"}
     (if (empty? datasets)
       [ui/circular-progress]
       [course-info {:datasets datasets}])]))
