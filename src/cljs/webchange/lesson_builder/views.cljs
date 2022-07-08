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
    [webchange.lesson-builder.state :as state]
    [webchange.ui.index :as ui]))

(defn index
  [props]
  (re-frame/dispatch [::state/init (transform-keys ->kebab-case-keyword props)])
  (fn []
    (let [loading? @(re-frame/subscribe [::state/loading?])]
      [:div#bbs-lesson-builder
       (if-not loading?
         [:<>
          [block-menu {:class-name "bbs-lesson-builder--menu"}]
          [block-script {:class-name "bbs-lesson-builder--script"}]
          [block-stage {:class-name "bbs-lesson-builder--stage"}]
          [block-title {:class-name "bbs-lesson-builder--title"}]
          [block-toolbox {:class-name "bbs-lesson-builder--toolbox"}]]
         [ui/loading-overlay])])))
