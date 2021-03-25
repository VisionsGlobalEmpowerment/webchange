(ns webchange.editor-v2.wizard.views-book-creator
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.components.page-layout.views :refer [layout]]
    [webchange.editor-v2.wizard.state.activity :as state-activity]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views :refer [template-form]]
    [webchange.editor-v2.wizard.validator :as validator]
    [webchange.editor-v2.components.breadcrumbs.views :refer [root-breadcrumbs]]))

(defn- book-activity-info
  []
  (r/with-let [book-template 24
               data (r/atom {:lang "English" :skills [] :name "Activity" :template-id book-template :authors [""] :illustrators [""]})
               {:keys [valid?] :as validator} (validator/init data)]
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
            (when (:options current-template)
              [template-form {:template  current-template
                              :data      data
                              :validator validator}])]]]
         [ui/card-actions
          [ui/button {:color    "secondary"
                      :style    {:margin-left "auto"}
                      :on-click #(if (valid?) (re-frame/dispatch [::state-activity/create-book (-> @data
                                                                                                   (assoc :course-name course-name :activity-name activity-name))]))}
           "Save"]]]]])))

(defn book-creator-panel
  []
  (re-frame/dispatch [::state-activity/load-templates])
  (re-frame/dispatch [::state-activity/load-skills])
  [layout {:breadcrumbs (root-breadcrumbs "Create Book")}
   [book-activity-info]])
