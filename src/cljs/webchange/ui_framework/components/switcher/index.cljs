(ns webchange.ui-framework.components.switcher.index
  (:require
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn component
  [{:keys [checked? class-name id label name on-change value]
    :or   {checked?  true
           id        (->> (str (random-uuid))
                          (take 8)
                          (clojure.string/join "")
                          (str "switcher-"))
           label     ""
           on-change #()}}]
  (let [handle-change #(on-change (not checked?) value)]
    [:div {:class-name (get-class-name (-> {"wc-switch" true}
                                           (assoc class-name (some? class-name))))}
     [:input {:type      "checkbox"
              :name      name
              :value     value
              :checked   checked?
              :on-change handle-change
              :id        id}]
     [:label {:for id} label]]))
