(ns webchange.editor-v2.wizard.views-book-creator
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.wizard.state.activity :as state-activity]
    [webchange.editor-v2.wizard.activity-template.views :refer [template]]
    [webchange.editor-v2.layout.breadcrumbs :refer [root-breadcrumbs]]))

(defn- book-activity-info
  []
  (r/with-let [book-template 24
               data (r/atom {:lang "English" :skills [] :name "Activity" :template-id book-template})]
    (let [current-template (->> @(re-frame/subscribe [::state-activity/templates])
                                (filter #(= (:template-id @data) (:id %)))
                                first)
          course-name (get @data :cover-title)
          activity-name "Book"]
      [ui/grid {:container   true
                :justify     "center"
                :spacing     24
                :align-items "center"}
       [ui/grid {:item true :xs 8}
        [ui/card {:style {:margin      "12px"
                          :flex-shrink "0"}}
         [ui/card-content
          [ui/grid {:container   true
                    :justify     "center"
                    :spacing     24
                    :align-items "center"}
           [ui/grid {:item true :xs 12}
            [ui/typography {:variant "h4"} "Create Book"]]
           [ui/grid {:item true :xs 12}
            [template {:template current-template
                       :data     data}]]]]
         [ui/card-actions
          [ui/button {:color    "secondary"
                      :style    {:margin-left "auto"}
                      :on-click #(re-frame/dispatch [::state-activity/create-book (-> @data
                                                                                      (assoc :course-name course-name :activity-name activity-name))])}
           "Save"]]]]])))

(defn book-creator-panel
  []
  (re-frame/dispatch [::state-activity/load-templates])
  (re-frame/dispatch [::state-activity/load-skills])
  [layout {:breadcrumbs (root-breadcrumbs "Create Book")}
   [book-activity-info]])
