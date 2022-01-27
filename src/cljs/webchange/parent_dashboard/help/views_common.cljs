(ns webchange.parent-dashboard.help.views-common
  (:require
    [reagent.core :as r]))

(defn h1
  []
  (into [:h1]
        (-> (r/current-component)
            (r/children))))

(defn p
  []
  (into [:p.paragraph]
        (-> (r/current-component)
            (r/children))))

(defn link
  [{:keys [url]}]
  (into [:a.link {:href   url
                  :target "_blank"
                  :rel    "noopener noreferrer"}]
        (-> (r/current-component)
            (r/children))))

(defn qa
  [{:keys [q a]}]
  [:dl.qa
   [:dt "Q: " q]
   (if (some? a)
     [:dd [:span "A: "] [:div.dd-wrapper a]]
     [:dd [:span "A: "] (->> (r/current-component)
                             (r/children)
                             (into [:div.dd-wrapper]))])])

(defn block
  [{:keys [title]}]
  (into [:div.block
         (when (some? title)
           [h1 title])]
        (-> (r/current-component)
            (r/children))))

(defn list-items
  [{:keys [title]}]
  [:div.list
   [:h3 title]
   (->> (r/current-component)
        (r/children)
        (map (fn [item]
               [:li item]))
        (into [:ul]))])
