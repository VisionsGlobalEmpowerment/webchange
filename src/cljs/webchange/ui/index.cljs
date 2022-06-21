(ns webchange.ui.index
  (:require
    [webchange.ui.components.breadcrumbs.views :as breadcrumbs]
    [webchange.ui.components.button.views :as button]
    [webchange.ui.components.card.views :as card]
    [webchange.ui.components.chip.views :as chip]
    [webchange.ui.components.icon.views :as icon]
    [webchange.ui.components.logo.views :as logo]
    [webchange.ui.components.panel.views :as panel]
    [webchange.ui.components.tab.views :as tab]
    [webchange.ui.utils.get-class-name :as gcn]))

(def breadcrumbs breadcrumbs/breadcrumbs)
(def button button/button)
(def card card/card)
(def chip chip/chip)
(def flag icon/flag-icon)
(def icon icon/general-icon)
(def logo-with-name logo/logo-with-name)
(def navigation-icon icon/navigation-icon)
(def panel panel/panel)
(def system-icon icon/system-icon)
(def tab tab/tab)

(def get-class-name gcn/get-class-name)
