(ns webchange.editor-v2.translator.translator-form.views-form-concepts
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.subs :as editor-subs]
    [webchange.editor-v2.translator.translator-form.subs :as translator-form-subs]
    [webchange.editor-v2.translator.translator-form.events :as translator-form-events]))

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
  (let [current-concept @(re-frame/subscribe [::translator-form-subs/current-concept])
        concepts-list (->> @(re-frame/subscribe [::editor-subs/course-dataset-items]) (vals) (sort-by :name))
        handle-concept-changed #(re-frame/dispatch [::translator-form-events/set-current-concept %])
        styles (get-styles)]
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
          (:name concept)])]]]))
