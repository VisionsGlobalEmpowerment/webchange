(ns webchange.editor-v2.course-dashboard.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.course-dashboard.views-concepts :refer [concepts-list]]
    [webchange.editor-v2.course-dashboard.views-course-info :refer [course-info]]
    [webchange.editor-v2.course-dashboard.views-lessons :refer [lessons-list]]
    [webchange.editor-v2.course-dashboard.views-scenes :refer [scenes-list]]
    [webchange.editor-v2.layout.views :refer [layout]]))

(defn- get-styles
  []
  {:main-content {:height "100%"
                  :margin "0"
                  :width  "100%"}})

(defn course-dashboard
  []
  (let [styles (get-styles)]
    [layout {:title "Course"}
     [:div {:style {:height         "100%"
                    :display        "flex"
                    :flex-direction "column"}}
      [course-info]
      [ui/grid {:container true
                :justify   "space-between"
                :spacing   24
                :style     (:main-content styles)}
       [ui/grid {:item true
                 :xs   4}
        [concepts-list]]
       [ui/grid {:item true
                 :xs   4}
        [scenes-list]]
       [ui/grid {:item true
                 :xs   4}
        [lessons-list]]]]]))
