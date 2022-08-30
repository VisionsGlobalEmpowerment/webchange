(ns webchange.ui.components.switch.views
  (:require
    [webchange.ui.utils.get-class-name :refer [get-class-name]]))

(defn switch
  [{:keys [checked? class-name color disabled? id indeterminate? label label-side name on-change state title value]
    :or   {checked?       true
           disabled?      false
           id             (->> (str (random-uuid))
                               (take 8)
                               (clojure.string/join "")
                               (str "switcher-"))
           label          ""
           label-side     "left"
           indeterminate? false
           on-change      #()}}]
  (let [handle-change #(on-change (not checked?) value)]
    [:div (cond-> {:class-name (get-class-name {"bbs--switch"                               true
                                                "bbs--switch--indeterminate"                indeterminate?
                                                "bbs--switch--disabled"                     disabled?
                                                (str "bbs--switch--color-" color)           (some? color)
                                                (str "bbs--switch--label-side-" label-side) true
                                                (str "bbs--switch--state-" state)           (some? state)
                                                class-name                                  (some? class-name)})}
                  (some? title) (assoc :title title))
     [:input {:type      "checkbox"
              :name      name
              :value     value
              :checked   checked?
              :on-change handle-change
              :id        id
              :disabled  disabled?}]
     [:label {:for id} label]]))
