(ns cljsjs.material-ui
  (:require ["@material-ui/core" :as material-ui]
            ["@material-ui/core/styles" :as material-ui-styles]
            ["@material-ui/icons" :as material-ui-icons]))

(js/goog.exportSymbol "MaterialUI" material-ui)
(js/goog.exportSymbol "MaterialUIIcons" material-ui-icons)
(js/goog.exportSymbol "MaterialUIStyles" material-ui-styles)
