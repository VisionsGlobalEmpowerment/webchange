(ns webchange.admin.widgets.search.views
  (:require
    [webchange.ui.index :as ui]))

(defn- search
  [{:keys [class-name on-change value]}]
  (let [handle-change #(when (fn? on-change) (on-change %))
        handle-reset #(handle-change "")]
    [ui/input {:value         value
               :icon          (if (empty? value) "search" "close")
               :placeholder   "search"
               :class-name    (ui/get-class-name {"widget--search" true
                                                  class-name       (some? class-name)})
               :on-change     handle-change
               :on-esc-press  handle-reset
               :on-icon-click handle-reset}]))