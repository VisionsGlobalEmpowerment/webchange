(ns webchange.editor-v2.translator.translator-form.views-form-concepts
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn get-concept
  [concepts concept-id]
  (some
    (fn [{:keys [id] :as concept}]
      (and (= id concept-id)
           concept))
    concepts))

(defn concepts-block
  [{:keys [current-concept concepts-list on-change]}]
  [ui/form-control {:full-width true
                    :margin     "normal"}
   [ui/input-label "Concept"]
   [ui/select {:value     (or (:id current-concept) "")
               :on-change #(on-change (get-concept concepts-list (.. % -target -value)))}
    (for [concept concepts-list]
      ^{:key (:id concept)}
      [ui/menu-item {:value (:id concept)}
       (:name concept)])]])
