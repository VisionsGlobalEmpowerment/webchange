(ns webchange.dashboard.dashboard-layout.utils)

(defn get-shift-styles
  [drawer-open? drawer-width]
  (if drawer-open?
    {:margin-left drawer-width
     :width       (str "calc(100% - " drawer-width "px)")}
    {:margin-left 0
     :width       "100%"}))
