(ns webchange.student-dashboard.views-additional-content)

(def additional-content-styles
  {:background-color "#f7f7f7"
   :flex "0 0 auto"
   :width 400})

(defn- related-content-item
  [{:keys [id name type link]}]
  [:div name])

(defn- related-content-block
  [related-content]
  [:div
   [:h1 "Related / Additional content"]
   [:ul
    (for [item related-content]
      ^{:key (:id item)}
      [related-content-item item])]])

(defn- life-skills-item
  [{:keys [id name type link]}]
  [:div name])

(defn- life-skills-block
  [life-skills]
  [:div
   [:h1 "Life skills / Extra credit"]
   [:ul
    (for [item life-skills]
      ^{:key (:id item)}
      [life-skills-item item])]])


(defn additional-content
  [{:keys [related-content life-skills]}]
  [:div {:style additional-content-styles}
   [related-content-block related-content]
   [life-skills-block life-skills]])
