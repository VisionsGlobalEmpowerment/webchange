(ns webchange.lesson-builder.views
  (:require
    [camel-snake-kebab.core :refer [->kebab-case-keyword]]
    [camel-snake-kebab.extras :refer [transform-keys]]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.layout.views :refer [layout]]
    [webchange.lesson-builder.widgets.confirm.views :refer [block-confirm]]
    [webchange.lesson-builder.layout.menu.views :refer [block-menu]]
    [webchange.lesson-builder.layout.script.views :refer [block-script]]
    [webchange.lesson-builder.layout.stage.views :refer [block-stage]]
    [webchange.lesson-builder.layout.title.views :refer [block-title]]
    [webchange.lesson-builder.layout.toolbox.views :refer [block-toolbox]]
    [webchange.lesson-builder.layout.state :as blocks-state]
    [webchange.lesson-builder.state :as state]
    [webchange.ui.index :as ui]))

(defn index
  [props]
  (re-frame/dispatch [::state/init (transform-keys ->kebab-case-keyword props)])
  (fn []
    [layout]))
