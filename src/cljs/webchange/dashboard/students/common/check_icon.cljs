(ns webchange.dashboard.students.common.check-icon
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.icons :as ic]))

(def check-colors
  {:completed "#3d9a00"
   :failed    "#fd4142"
   :undefined "#c1c1c1"})

(def icon-styles
  {:display  "inline-block"
   :height   17
   :position "relative"
   :top      3})

(defn get-check-style
  [{:keys [style] :or {style {}}} color-key]
  (merge icon-styles {:color (get check-colors color-key)} style))

(defn check-icon
  [{:keys [value icon style]}]
  (let [get-style (partial get-check-style {:style style})]
    (case value
      true [(or icon ic/check-circle) {:style (get-style :completed)}]
      false [(or icon ic/remove-circle) {:style (get-style :failed)}]
      [(or icon ic/remove-circle-outline) {:style (get-style :undefined)}])))
