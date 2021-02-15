(ns webchange.editor-v2.layout.flipbook.views-stage-text
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage]
    [webchange.editor-v2.layout.flipbook.page-form.views :refer [page-form]]))

(defn stage-text
  []
  (let [stage @(re-frame/subscribe [::stage/current-stage])]
    [ui/grid {:container true
              :spacing   16
              :justify   "space-between"}
     [ui/grid {:item true :xs 6}
      [page-form {:stage     stage
                  :page-side :left}]]
     [ui/grid {:item true :xs 6}
      [page-form {:stage     stage
                  :page-side :right}]]]))
