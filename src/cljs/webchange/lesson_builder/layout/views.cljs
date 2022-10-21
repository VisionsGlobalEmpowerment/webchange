(ns webchange.lesson-builder.layout.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.layout.state :as state]
    [webchange.lesson-builder.layout.menu.views :refer [block-menu]]
    [webchange.lesson-builder.layout.stage.views :refer [block-stage]]
    [webchange.lesson-builder.layout.script.views :refer [block-script]]
    [webchange.lesson-builder.layout.title.views :refer [block-title]]
    [webchange.lesson-builder.layout.toolbox.views :refer [block-toolbox]]
    [webchange.lesson-builder.widgets.confirm.views :refer [block-confirm]]
    [webchange.ui.index :as ui]))

(defn layout
  []
  (let [focused-blocks @(re-frame/subscribe [::state/focused-blocks])
        loading? @(re-frame/subscribe [::state/activity-loading?])]
    [:div#bbs-lesson-builder
     (if-not loading?
       [:<>
        [block-menu {:tabs-disabled? (contains? focused-blocks :menu)
                     :class-name     (ui/get-class-name {"bbs-lesson-builder--menu"          true
                                                         "bbs-lesson-builder--focused-block" (contains? focused-blocks :menu)})}]
        [block-script {:class-name (ui/get-class-name {"bbs-lesson-builder--script"        true
                                                       "bbs-lesson-builder--focused-block" (contains? focused-blocks :script)})}]
        [block-stage {:class-name (ui/get-class-name {"bbs-lesson-builder--stage"         true
                                                      "bbs-lesson-builder--focused-block" (contains? focused-blocks :stage)})}]
        [block-title {:class-name (ui/get-class-name {"bbs-lesson-builder--title"         true
                                                      "bbs-lesson-builder--focused-block" (contains? focused-blocks :title)})}]
        [block-toolbox {:class-name (ui/get-class-name {"bbs-lesson-builder--toolbox"       true
                                                        "bbs-lesson-builder--focused-block" (contains? focused-blocks :toolbox)})}]
        [block-confirm]
        (when-not (empty? focused-blocks)
          [ui/focus-overlay])]
       [ui/loading-overlay])]))
