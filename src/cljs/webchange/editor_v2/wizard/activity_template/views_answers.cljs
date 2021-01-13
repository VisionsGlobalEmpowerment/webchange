(ns webchange.editor-v2.wizard.activity-template.views-answers
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.file-input.views :as file-input]
    [webchange.editor-v2.concepts.events :as concepts-events]
    [webchange.editor-v2.wizard.activity-template.views-image :as image-field]
    [webchange.editor-v2.wizard.validator :as v :refer [connect-data]]))

(def answers-validation-map {:root [(fn [value] (when (= (count value) 0) "answers are required"))]})
(def page-validation-map {:text [(fn [value] (when (empty? value) "Text is required"))]
                          :img  [(fn [value] (when (empty? value) "Image is required"))]})
(def question-string-validation-map {:root [(fn [value] (when (= value "") "Required field"))]})



(defn- answer-option
  [{:keys [idx data validator on-remove last? with-image]}]
  (r/with-let [page-data (connect-data data [:answers idx])
               {:keys [error-message destroy]} (v/init page-data page-validation-map validator)]
              [ui/paper {:style {:padding "20px 10px"}}
               [ui/grid {:container true
                         :spacing   16
                         :style     {:margin-top "-16px"}}
                [ui/grid {:item true :xs 12}
                 [ui/form-control {:full-width true}
                  [ui/text-field {:label     "Text"
                                  :variant   "outlined"
                                  :value     (get @page-data :text "")
                                  :on-change #(swap! page-data assoc :text (-> % .-target .-value))
                                  :style     {:min-width "500px"}}]
                  [error-message {:field-name :text}]]]
                [ui/grid {:item true :xs 12}
                 [ui/form-control {}
                  [ui/typography {:variant "h6"
                                  :style   {:display      "inline-block"
                                            :margin-right "16px"}}
                   "Correct?"]
                  [ui/checkbox {:label     "Correct?"
                                  :variant   "outlined"
                                  :value     (get @page-data :checked "")
                                  :on-change #(swap! page-data assoc :checked (-> % .-target .-value))
                                }]
                  [error-message {:field-name :checked}]]]

                (if with-image
                  [ui/grid {:item true :xs 10}
                   [image-field/image-field (get @page-data :img "") #(swap! page-data assoc :img %)]
                   [error-message {:field-name :img}]]
                  )
                [ui/grid {:item  true :xs 2
                          :style {:display         "flex"
                                  :align-items     "flex-end"
                                  :justify-content "flex-end"}}
                 (when last?
                   [ui/icon-button {:on-click   #(on-remove idx)
                                    :aria-label "Delete"
                                    :style      {:padding "8px"}}
                    [ic/delete]])]]]
              (finally
                (destroy))))

(defn answers-option
  [{:keys [key option data validator]} with-image]
  (r/with-let [add-tooltip-open? (r/atom false)
               question-page (connect-data data ["question-page"] {})
               {:keys [error-message] :as validator} (v/init question-page answers-validation-map validator)
               ]
              (let [
                    handle-add-page (fn []
                                      (if (< (count (:answers @question-page)) (:max-answers option))
                                        (reset! question-page (update-in @question-page [:answers] conj {}))
                                        (reset! add-tooltip-open? true)))
                    handle-remove-page (fn [idx]
                                         (reset! question-page (assoc @question-page :answers (fn []
                                                               (let [item-to-remove (nth (:answers @question-page) idx)]
                                                                 (->> (:answers @question-page)
                                                                      (remove #(= item-to-remove %))
                                                                      (into [])))))

                                                 )
                                         )
                    ]
                (when (nil? (:answers @question-page)) (reset! question-page (assoc @question-page :answers [])))
                [ui/grid {:container   true
                          :justify     "center"
                          :spacing     16
                          :align-items "center"}
                 [ui/grid {:item true :xs 12}
                  [ui/typography {:variant "h6"
                                  :style   {:display      "inline-block"
                                            :margin-right "16px"}}
                   (:label option)]
                  [ui/text-field {:label     "Value"
                                  :variant   "outlined"
                                  :value     (get @question-page :question "")
                                  :style     {:min-width "300px"}
                                  :on-change #(reset! question-page (assoc @question-page :question (-> % .-target .-value)))}]
                  [error-message {:field-name :root}]]
                 [ui/grid {:item true :xs 10}
                  [image-field/image-field (get @question-page :img "") #(swap! question-page assoc :img %)]
                  [error-message {:field-name :img}]]

                 [ui/grid {:item true :xs 12}
                  [ui/typography {:variant "h6"
                                  :style   {:display      "inline-block"
                                            :margin-right "16px"}}
                   (:label option)]
                  [ui/click-away-listener {:onClickAway #(reset! add-tooltip-open? false)}
                   [ui/tooltip {:title                  (str "Max. " (:max-answers option) " items")
                                :open                   @add-tooltip-open?
                                :placement              "right"
                                :disable-focus-listener true
                                :disable-hover-listener true
                                :disable-touch-listener true}
                    [ui/button {:size     "small"
                                :on-click handle-add-page}
                     "Add"]]]
                  [error-message {:field-name :root}]]
                 (let [answers-list (map-indexed list (:answers @question-page))]
                   (for [[idx _] answers-list]
                     ^{:key idx}
                     [ui/grid {:item  true :xs 12
                               :style {:margin-bottom "12px"}}
                      [answer-option {:idx        idx
                                      :data       question-page
                                      :validator  validator
                                      :on-remove  handle-remove-page
                                      :with-image with-image
                                      :last?      (->> (count answers-list) (dec) (= idx))}]]))


                 ])))
