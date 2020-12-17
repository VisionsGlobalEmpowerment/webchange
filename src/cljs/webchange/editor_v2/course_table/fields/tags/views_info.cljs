(ns webchange.editor-v2.course-table.fields.tags.views-info
  (:require
    [cljs-react-material-ui.reagent :as ui]))

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
    (for [[name range] (sort-by first tags)]
      ^{:key name}
      [:li
       name
       [:span.appointment-range (str "[ " (first range) " : " (last range) " ]")]])]])

(defn info-from
  [{:keys [data]}]
  (let [{:keys [for-tags set-tags]} (:tags data)]
    [:div
     (when (some? for-tags)
       [tags-restriction {:tags for-tags}])
     (when (some? set-tags)
       [tags-appointment {:tags set-tags}])]))
