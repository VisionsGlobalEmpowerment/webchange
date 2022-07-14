(ns webchange.lesson-builder.views
  (:require
    [camel-snake-kebab.core :refer [->kebab-case-keyword]]
    [camel-snake-kebab.extras :refer [transform-keys]]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.blocks.menu.views :refer [block-menu]]
    [webchange.lesson-builder.blocks.script.views :refer [block-script]]
    [webchange.lesson-builder.blocks.stage.views :refer [block-stage]]
    [webchange.lesson-builder.blocks.title.views :refer [block-title]]
    [webchange.lesson-builder.blocks.toolbox.views :refer [block-toolbox]]
    [webchange.lesson-builder.blocks.state :as blocks-state]
    [webchange.lesson-builder.state :as state]
    [webchange.ui.index :as ui]))

(defn index
  [props]
  (re-frame/dispatch [::state/init (transform-keys ->kebab-case-keyword props)])
  (fn []
    (let [loading? @(re-frame/subscribe [::state/loading?])
          focused-blocks @(re-frame/subscribe [::blocks-state/focused-blocks])]
      [:div#bbs-lesson-builder
       (if-not loading?
         [:<>
          [block-menu {:class-name (ui/get-class-name {"bbs-lesson-builder--menu"          true
                                                       "bbs-lesson-builder--focused-block" (contains? focused-blocks :menu)})}]
          [block-script {:class-name (ui/get-class-name {"bbs-lesson-builder--script" true
                                                         "bbs-lesson-builder--focused-block" (contains? focused-blocks :script)})}]
          [block-stage {:class-name (ui/get-class-name {"bbs-lesson-builder--stage" true
                                                        "bbs-lesson-builder--focused-block" (contains? focused-blocks :stage)})}]
          [block-title {:class-name (ui/get-class-name {"bbs-lesson-builder--title" true
                                                        "bbs-lesson-builder--focused-block" (contains? focused-blocks :title)})}]
          [block-toolbox {:class-name (ui/get-class-name {"bbs-lesson-builder--toolbox" true
                                                          "bbs-lesson-builder--focused-block" (contains? focused-blocks :toolbox)})}]
          (when-not (empty? focused-blocks)
            [ui/focus-overlay])]
         [ui/loading-overlay])])))
