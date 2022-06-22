(ns webchange.ui.index
  (:require
    [webchange.ui.components.breadcrumbs.views :as breadcrumbs]
    [webchange.ui.components.button.views :as button]
    [webchange.ui.components.card.views :as card]
    [webchange.ui.components.chip.views :as chip]
    [webchange.ui.components.copy-link.views :as copy-link]
    [webchange.ui.components.form.views :as form]
    [webchange.ui.components.icon.views :as icon]
    [webchange.ui.components.input.views :as input]
    [webchange.ui.components.logo.views :as logo]
    [webchange.ui.components.overlay.views :as overlay]
    [webchange.ui.components.panel.views :as panel]
    [webchange.ui.components.password.views :as password]
    [webchange.ui.components.select.views :as select]
    [webchange.ui.components.tab.views :as tab]
    [webchange.ui.components.text-area.views :as text-area]
    [webchange.ui.utils.get-class-name :as gcn]))

(def breadcrumbs breadcrumbs/breadcrumbs)
(def button button/button)
(def card card/card)
(def chip chip/chip)
(def copy-link copy-link/copy-link)
(def flag icon/flag-icon)
(def form form/form)
(def icon icon/general-icon)
(def input input/input)
(def logo-with-name logo/logo-with-name)
(def navigation-icon icon/navigation-icon)
(def focus-overlay overlay/focus-overlay)
(def panel panel/panel)
(def password password/password)
(def select select/select)
(def system-icon icon/system-icon)
(def tab tab/tab)
(def text-area text-area/text-area)

(def get-class-name gcn/get-class-name)
