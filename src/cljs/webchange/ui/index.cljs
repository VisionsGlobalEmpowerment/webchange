(ns webchange.ui.index
  (:require
    [webchange.ui.components.button.views :as button]
    [webchange.ui.components.card.views :as card]
    [webchange.ui.components.icon.views :as icon]
    [webchange.ui.components.logo.views :as logo]
    [webchange.ui.components.panel.views :as panel]
    [webchange.ui.components.tab.views :as tab]
    [webchange.ui.utils.get-class-name :as gcn]))

(def button button/button)
(def card card/card)
(def flag icon/flag-icon)
(def icon icon/system-icon)
(def logo-with-name logo/logo-with-name)
(def navigation-icon icon/navigation-icon)
(def panel panel/panel)
(def tab tab/tab)

(def get-class-name gcn/get-class-name)
