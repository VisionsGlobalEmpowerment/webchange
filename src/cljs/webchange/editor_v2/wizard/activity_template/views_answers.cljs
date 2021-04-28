(ns webchange.editor-v2.wizard.activity-template.views-answers
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.activity-stage.views :refer [select-stage]]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.ui-framework.components.index :refer [select]]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.activity-template.views-image :as image-field]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def question-validation-map {:answers [(fn [value] (when (= (count value) 0) "answers are required"))]
                              :question-type [(fn [value] (when (empty? value) "select layout type"))]})
(def answer-option-validation-map {:text [(fn [value] (when (empty? value) "Text is required"))]})

(def types-options [{:text "--Select question type--" :value ""}
                    {:text "Answers without images" :value "type-1"}
                    {:text "Answers with images" :value "type-2"}])

(defn- image-enabled
  [type with-image]
  (if with-image
    with-image
    (case type
      "" false
      "type-1" false
      "type-2" true)))

(defn- form-block
  [{:keys [title]}]
  (let [this (r/current-component)]
    [ui/grid {:container   true
              :justify     "center"
              :spacing     16
              :align-items "flex-start"
              :style       {:margin-top    "8px"
                            :margin-bottom "16px"}}
     [ui/grid {:item true :xs 2}
      [ui/typography {:variant "h6"}
       title ":"]]
     (into [ui/grid {:item true :xs 10}]
           (r/children this))]))

(defn- question-block
  [{:keys [question-page option error-message with-image]}]
  [form-block {:title (:label option)}
   [ui/grid {:container true
             :spacing   16}
    [ui/grid {:item true :xs 12}
     [ui/checkbox {:label     "Screenshot"
                     :variant   "outlined"
                     :checked  (get @question-page :question-screenshot false)
                     :on-change #(do
                                   (reset! question-page (assoc @question-page :question-screenshot (-> % .-target .-checked))))}]
     [ui/typography {:style {:display "inline-block"}
                     :variant "h6"}
       "Use screenshot instead image"]
     [error-message {:field-name :root}]]

      [ui/grid {:item true :xs 12}
       [ui/typography {:style   {:display "inline-block"}
                       :variant "h6"}
        "Select question layout type"]
       [select {:value     (get @question-page :question-type "")
                :options   types-options
                :variant   "outlined"
                :on-change #(reset! question-page (assoc @question-page :question-type %))
                :width     160}]
       [error-message {:field-name :question-type}]
       ]

    [ui/grid {:item true :xs 12}
     [ui/text-field {:label     "Question text"
                     :variant   "outlined"
                     :value     (get @question-page :question "")
                     :style     {:min-width "300px"}
                     :on-change #(reset! question-page (assoc @question-page :question (-> % .-target .-value)))}]
     [error-message {:field-name :root}]]
    [ui/grid {:item true :xs 12}
     [image-field/image-field (get @question-page :img "") #(swap! question-page assoc :img %) {:max-width 600
                                                                                                :max-height 400
                                                                                                :min-height 100
                                                                                                :min-width 100}]
     [select-stage {:on-change #(re-frame/dispatch [::state-flipbook/upload-stage-screenshot (.. % -target -value)
                                                    (fn [result] (swap! question-page assoc :img (:url result)))])}]
     [error-message {:field-name :img}]]]])

(defn- answer-option
  [{:keys [idx data validator on-remove last? with-image]}]
  (r/with-let [page-data (connect-data data [:answers idx])
               {:keys [error-message destroy]} (v/init
                                                 page-data
                                                 answer-option-validation-map
                                                 validator)]
    [ui/grid {:container   true
              :spacing     16
              :align-items "center"}

     [ui/grid {:item true :xs 9}
      [ui/form-control {:full-width true}
       [ui/text-field {:label     "Text"
                       :variant   "outlined"
                       :value     (get @page-data :text "")
                       :on-change #(swap! page-data assoc :text (-> % .-target .-value))
                       :style     {:min-width "500px"}}]
       [error-message {:field-name :text}]]]


     [ui/grid {:item  true :xs 2
               :style {:text-align "end"}}
      [ui/form-control-label
       {:label   "Correct?"
        :control (r/as-element
                   [ui/switch {:checked   (get @page-data :checked false)
                               :on-change #(swap! page-data assoc :checked (-> % .-target .-checked))}])}]
      [error-message {:field-name :checked}]]

     [ui/grid {:item true :xs 1}
      (when last?
        [ui/icon-button {:on-click   on-remove
                         :aria-label "Delete"
                         :style      {:padding "8px"}}
         [ic/delete]])]

     (when with-image
       [ui/grid {:item true :xs 9}
        [image-field/image-field (get @page-data :img "") #(swap! page-data assoc :img %)]
        [select-stage {:on-change #(re-frame/dispatch [::state-flipbook/upload-stage-screenshot (.. % -target -value)
                                                       (fn [result] (swap! page-data assoc :img (:url result)))])}]
        [error-message {:field-name :img}]]
       )]

    (finally
      (destroy))))

(defn- answers-block
  [{:keys [question-page option error-message validator with-image]}]
  (r/with-let [add-tooltip-open? (r/atom false)
               handle-add-page (fn []
                                 (if (< (count (:answers @question-page)) (:max-answers option))
                                   (reset! question-page (update-in @question-page [:answers] conj {}))
                                   (reset! add-tooltip-open? true)))
               handle-remove-page (fn []
                                    (->> (get @question-page :answers)
                                         (drop-last)
                                         (into [])
                                         (swap! question-page assoc :answers)))]
    [form-block {:title (:answers-label option)}
     [ui/grid {:container   true
               :justify     "center"
               :spacing     16
               :align-items "flex-start"}
      [ui/grid {:item true :xs 12}
       [ui/click-away-listener {:onClickAway #(reset! add-tooltip-open? false)}
        [ui/tooltip {:title                  (str "Max. " (:max-answers option) " items")
                     :open                   @add-tooltip-open?
                     :placement              "right"
                     :disable-focus-listener true
                     :disable-hover-listener true
                     :disable-touch-listener true}
         [ui/button {:on-click handle-add-page}
          "Add Answer Option"]]]
       [error-message {:field-name :answers}]]

      (when (->> (:answers @question-page)
                 (count) (< 0))
        [ui/grid {:item true :xs 12}
         [ui/paper {:style {:padding "20px 10px"}}
          (let [answers-list (map-indexed list (:answers @question-page))]
            (for [[idx _] answers-list]
              ^{:key idx}
              [answer-option {:idx        idx
                              :data       question-page
                              :validator  validator
                              :on-remove  handle-remove-page
                              :with-image with-image
                              :last?      (->> (count answers-list) (dec) (= idx))}]))]])]]))

(defn answers-option
  [{:keys [key option data validator]} with-image]
  (r/with-let [question-page (connect-data data ["question-page"] {})
               {:keys [error-message destroy] :as validator} (v/init question-page question-validation-map validator)
               ]
    (when (nil? (:answers @question-page)) (reset! question-page (assoc @question-page :answers [])))
    [:div
     [ui/divider]
     [question-block {:question-page question-page
                      :option        option
                      :error-message error-message
                      :with-image    with-image}]
     [answers-block {:question-page question-page
                     :option        option
                     :error-message error-message
                     :validator     validator
                     :with-image    (image-enabled (get @question-page :question-type "") with-image)}]]
    (finally
      (destroy))))
