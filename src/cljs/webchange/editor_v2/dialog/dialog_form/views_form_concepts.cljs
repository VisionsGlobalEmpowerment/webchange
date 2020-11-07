(ns webchange.editor-v2.dialog.dialog-form.views-form-concepts
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.translator.translator-form.state.concepts :as translator-form.concepts]
    [webchange.editor-v2.dialog.dialog-form.state.concepts :as dialog-form.concepts]
    ))

(defn- get-styles
  []
  {:wrapper {:display     "flex"
             :align-items "baseline"}
   :label   {:margin-right "20px"}})

(defn- get-concept
  [concepts concept-id]
  (some
    (fn [{:keys [id] :as concept}]
      (and (= id concept-id)
           concept))
    concepts))

(defn concepts-block
  []
  (let [current-concept @(re-frame/subscribe [::translator-form.concepts/current-concept])
        concepts-list (->> @(re-frame/subscribe [::translator-form.concepts/concepts-list]))
        incomplete-concepts @(re-frame/subscribe [::dialog-form.concepts/incomplete-concepts])
        handle-concept-changed (fn [id]
                                 (re-frame/dispatch [::dialog-form.concepts/prepare-concept (:id id)])
                                 (re-frame/dispatch [::translator-form.concepts/set-current-concept (:id id)]))
        styles (get-styles)]
    [:div
    [:div [ui/typography {:variant "body1"
                          :style   (:label styles)}
           "Incomplete concepts: " (clojure.string/join ", " incomplete-concepts)]
     ]
    [:div {:style (:wrapper styles)}
     [ui/typography {:variant "body1"
                     :style   (:label styles)}
      "Concept:"]
     [ui/form-control {:full-width true
                       :margin     "normal"}
      [ui/select {:value     (or (:id current-concept) "")
                  :on-change #(handle-concept-changed (get-concept concepts-list (.. % -target -value)))}
       (for [concept concepts-list]
         ^{:key (:id concept)}
         [ui/menu-item {:value (:id concept)}
          (:name concept)])]]]]))
