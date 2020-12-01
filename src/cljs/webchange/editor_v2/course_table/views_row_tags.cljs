(ns webchange.editor-v2.course-table.views-row-tags
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.course-table.views-row-common :refer [field-cell]]))

(defn- tags-restriction
  [{:keys [tags]}]
  [:div.tags-block
   [:span.tags-title "Tags restriction:"]
   [:div
    (for [tag tags]
      ^{:key tag}
      [ui/chip {:label      tag
                :variant    "outlined"
                :class-name "tag-chip"}])]])

(defn- tags-appointment
  [{:keys [tags]}]
  [:div.tags-block
   [:span.tags-title "Tags appointment:"]
   [:ul.tags-appointments
    (for [[name range] tags]
      ^{:key name}
      [:li
       name
       [:span.appointment-range (str "[ " (first range) " : " (last range) " ]")]])]])

(defn tags
  [{:keys [data] :as props}]
  (let [{:keys [for-tags set-tags]} (:tags data)]
    [field-cell (merge props
                     {:cell-props {:class-name "tags"}})
     (when (some? for-tags)
       [tags-restriction {:tags for-tags}])
     (when (some? set-tags)
       [tags-appointment {:tags set-tags}])]))
