(ns webchange.editor-v2.course-dashboard.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.course-dashboard.views-concepts :refer [concepts-list]]
    [webchange.editor-v2.course-dashboard.views-course-info :refer [course-info]]
    [webchange.editor-v2.course-dashboard.views-lessons :refer [lessons-list]]
    [webchange.editor-v2.course-dashboard.views-scenes :refer [scenes-list]]
    [webchange.editor-v2.components.page-layout.views :refer [layout]]
    [webchange.editor-v2.components.breadcrumbs.views :refer [root-breadcrumbs]]))

(defn- get-styles
  []
  {:content-wrapper {:height         "100%"
                     :display        "flex"
                     :flex-direction "column"}
   :main-content    {:height "100%"
                     :margin "0"
                     :width  "100%"}})

(defn course-dashboard
  []
  (let [styles (get-styles)]
    [layout {:breadcrumbs (root-breadcrumbs "Course")}
     [:div {:style (:content-wrapper styles)}
      [course-info {:title     "Step 2: Choose Your Topic"
                    :iconFlag? true}]
      [ui/grid {:container true
                :justify   "space-between"
                :spacing   24
                :style     (:main-content styles)}
       [ui/grid {:item true :xs 4}
        [scenes-list {:title "Step 1: Activities"}]]
       [ui/grid {:item true :xs 4}
        [concepts-list {:title "Step 3: Concepts"}]]
       [ui/grid {:item true :xs 4}
        [lessons-list {:title "Step 4: Game Sequencing"}]]]]]))
