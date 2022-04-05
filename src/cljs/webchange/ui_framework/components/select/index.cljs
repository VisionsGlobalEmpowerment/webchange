(ns webchange.ui-framework.components.select.index
  (:require
    [webchange.ui-framework.components.select.icon-down :as icon-down]
    [webchange.ui-framework.components.select.icon-up :as icon-up]
    [webchange.ui-framework.components.select.utils :refer [empty-value? fix-options parse-value]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- get-selected-options
  [event]
  (->> (.. event -target -options)
       (.apply js/Array nil)
       (filter (fn [option] (.-selected option)))
       (map (fn [option] (.-value option)))))

(defn- apply-max-values-number
  [max-values-number values]
  (if (number? max-values-number)
    (take max-values-number values)
    values))

(defn component
  [{:keys [class-name
           error
           max-values-number
           multiple?
           on-arrow-down-click
           on-arrow-up-click
           on-change
           options
           placeholder
           required?
           show-buttons?
           type
           value
           variant
           width
           with-arrow?]
    :or   {multiple?           false
           options             []
           on-change           #()
           on-arrow-down-click #()
           on-arrow-up-click   #()
           placeholder         ""
           required?           false
           show-buttons?       false
           type                "str"
           with-arrow?         true}
    :as   props}]
  "Props:
   :options - items list
       .text
       .value
       .disabled?
       .title
   :variant - 'outlined' or none."
  (let [handle-change (fn [event]
                        (let [selected-options (->> (get-selected-options event)
                                                    (map #(parse-value % type))
                                                    (doall)
                                                    (apply-max-values-number max-values-number))]
                          (-> (if multiple?
                                selected-options
                                (first selected-options))
                              (on-change))))
        options (fix-options options props)]
    [:div {:style      (if (some? width) {:width width} {})
           :class-name (get-class-name (cond-> (-> {"wc-select"       true
                                                    "wc-select-error" (some? error)
                                                    "with-arrow"      (and with-arrow? (not multiple?))}
                                                   (assoc class-name (some? class-name)))
                                               (some? variant) (assoc (str "variant-" variant) true)))}
     [:select (cond-> {:value     (or value "")
                       :on-change handle-change}
                      multiple? (assoc :multiple true))
      (for [{:keys [text value disabled? title]} options]
        ^{:key value}
        [:option (cond-> {:value    value
                          :disabled disabled?}
                         (some? title) (assoc :title title))
         text])]
     (when show-buttons?
       [:div.controls
        [:button {:on-click on-arrow-up-click}
         [icon-up/data]]
        [:button {:on-click on-arrow-down-click}
         [icon-down/data]]])
     (when (some? error)
       [:label.wc-error error])]))
