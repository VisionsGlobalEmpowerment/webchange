(ns webchange.editor-v2.layout.flipbook.views-stage-text
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.flipbook.state :as flipbook-state]
    [webchange.editor-v2.layout.flipbook.page-text.views :refer [page-text-form]]))

(defn- form-placeholder
  []
  [ui/typography {:style {:font-size  "24px"
                          :text-align "center"
                          :width      "100%"
                          :padding    "64px"
                          :color      "#757575"}}
   "Nothing to edit on this stage"])

(defn stage-text
  []
  (let [text-objects @(re-frame/subscribe [::flipbook-state/stage-text-data])]
    (if (empty? text-objects)
      [form-placeholder]
      [ui/grid {:container true
                :spacing   16
                :justify   "space-between"}
       (for [{:keys [text] :as text-object} text-objects]
         ^{:key (:name text)}
         [ui/grid {:item true :xs 6}
          [page-text-form text-object]])])))
