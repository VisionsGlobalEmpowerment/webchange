(ns webchange.editor-v2.wizard.activity-template.question.views-preview
  (:require
    [webchange.ui-framework.components.index :refer [label]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def default-option [{:img  "/images/questions/option1.png"
                      :text "cow"}
                     {:img  "/images/questions/option2.png"
                      :text "deer"}
                     {:img  "/images/questions/option3.png"
                      :text "fox"}
                     {:img  "/images/questions/option4.png"
                      :text "skunk"}])

(defn- play-audio
  []
  [:div.play-audio
   [:svg {:width "41" :height "32" :viewBox "0 0 41 32" :fill "none" :xmlns "http://www.w3.org/2000/svg"}
    [:path {:d "M20.86 0.199576C20.5352 0.0590597 20.1789 0.00723956 19.8276 0.0494034C19.4762 0.0915673 19.1423 0.226205 18.86 0.439575L9.3 7.99958H2C1.46957 7.99958 0.960859 8.21029 0.585786 8.58536C0.210714 8.96043 0 9.46914 0 9.99957V21.9996C0 22.53 0.210714 23.0387 0.585786 23.4138C0.960859 23.7889 1.46957 23.9996 2 23.9996H9.3L18.76 31.5596C19.1119 31.8419 19.5489 31.997 20 31.9996C20.2987 32.0045 20.5941 31.9358 20.86 31.7996C21.2003 31.6375 21.4879 31.3825 21.6897 31.064C21.8914 30.7455 21.9989 30.3765 22 29.9996V1.99958C21.9989 1.6226 21.8914 1.25361 21.6897 0.935133C21.4879 0.616658 21.2003 0.361673 20.86 0.199576ZM18 25.8396L11.24 20.4396C10.8881 20.1573 10.4511 20.0022 10 19.9996H4V11.9996H10C10.4511 11.997 10.8881 11.8419 11.24 11.5596L18 6.15957V25.8396ZM35.32 4.67958C34.9434 4.30297 34.4326 4.09139 33.9 4.09139C33.3674 4.09139 32.8566 4.30297 32.48 4.67958C32.1034 5.05618 31.8918 5.56697 31.8918 6.09958C31.8918 6.63218 32.1034 7.14297 32.48 7.51958C33.6577 8.69545 34.5779 10.1034 35.1823 11.6541C35.7866 13.2047 36.0617 14.8641 35.9902 16.5268C35.9188 18.1895 35.5022 19.8192 34.7671 21.3122C34.0319 22.8053 32.9943 24.1291 31.72 25.1996C31.4105 25.4642 31.1892 25.8169 31.0856 26.2107C30.982 26.6045 31.001 27.0205 31.1402 27.4032C31.2793 27.7858 31.5319 28.1169 31.8643 28.3521C32.1966 28.5874 32.5929 28.7156 33 28.7196C33.4673 28.7205 33.9202 28.5577 34.28 28.2596C35.9819 26.8342 37.3685 25.0702 38.3517 23.0798C39.3349 21.0895 39.8932 18.9163 39.991 16.6985C40.0888 14.4807 39.7241 12.2668 38.9199 10.1977C38.1157 8.12848 36.8898 6.24928 35.32 4.67958ZM29.66 10.3396C29.4735 10.1531 29.2521 10.0052 29.0085 9.90425C28.7649 9.80333 28.5037 9.75139 28.24 9.75139C27.9763 9.75139 27.7152 9.80333 27.4715 9.90425C27.2279 10.0052 27.0065 10.1531 26.82 10.3396C26.6335 10.5261 26.4856 10.7474 26.3847 10.9911C26.2838 11.2347 26.2318 11.4959 26.2318 11.7596C26.2318 12.0233 26.2838 12.2844 26.3847 12.5281C26.4856 12.7717 26.6335 12.9931 26.82 13.1796C27.5712 13.9263 27.9955 14.9404 28 15.9996C28.0005 16.5823 27.8736 17.158 27.6284 17.6866C27.3831 18.2152 27.0253 18.6837 26.58 19.0596C26.3775 19.2275 26.2101 19.4337 26.0874 19.6664C25.9647 19.8991 25.8891 20.1537 25.8649 20.4156C25.8408 20.6776 25.8685 20.9417 25.9466 21.1929C26.0247 21.4441 26.1516 21.6775 26.32 21.8796C26.4893 22.0806 26.6966 22.2464 26.93 22.3672C27.1634 22.4881 27.4184 22.5618 27.6803 22.5841C27.9422 22.6064 28.2059 22.5769 28.4564 22.4972C28.7069 22.4174 28.9392 22.2891 29.14 22.1196C30.0342 21.3698 30.7536 20.4335 31.2477 19.3763C31.7417 18.319 31.9985 17.1665 32 15.9996C31.9887 13.8798 31.1489 11.8485 29.66 10.3396Z" :fill "black"}]]])

(defn- task
  []
  [:div.task
   [play-audio]
   [:span "Who do you think the main character, or most important character is going to be in this book?"]])

(defn- done-button
  []
  [:div.done-button
   [:img {:src "/images/questions/done.png"}]])

(defn- option
  [{:keys [idx option-label]}]
  (let [{:keys [img text]} (nth default-option (dec idx))]
    (into [:div {:class-name (get-class-name (-> {"option" true}
                                                 (assoc (str "label--" option-label) true)))}]
          (cond-> [[:img {:src img}]]
                  (= option-label "audio") (conj [:div.option-label [play-audio]])
                  (= option-label "audio-text") (conj [:div.option-label [play-audio] [:div.option-text text]])))))

(defn- options
  [{:keys [options-number] :as data}]
  [:div.options
   (for [idx (->> (inc options-number)
                  (range 1)
                  (into []))]
     ^{:key idx}
     [option (merge {:idx idx}
                    data)])])

(defn- primary-block
  [{:keys [correct-answers-count layout task-type] :as data}]
  (into [:div {:class-name (get-class-name {"primary-block" true
                                            "done-in-row"   (and (= task-type "text-image")
                                                                 (= layout "vertical")
                                                                 (= correct-answers-count "multiple"))})}]
        (cond-> (cond
                  (= task-type "text") [[task] [options data]]
                  (and (= task-type "text-image")
                       (= layout "horizontal")) [[task] [options data]]
                  (and (= task-type "text-image")
                       (= layout "vertical")) [[options data]]
                  :else [])
                (= correct-answers-count "multiple") (conj [done-button]))))

(defn- task-image
  []
  [:div.task-image
   [:img {:src (str "/images/questions/question.png")}]])

(defn- secondary-block
  [{:keys [layout]}]
  (into [:div.secondary-block]
        (case layout
          "horizontal" [[task-image]]
          "vertical" [[task-image]
                      [task]]
          [])))

(defn- template
  [{:keys [layout task-type] :as data}]
  (into [:div {:class-name (get-class-name (-> {"template" true}
                                               (assoc (str "type--" task-type) true)
                                               (assoc (str "layout--" layout) true)))}]
        (case task-type
          "text" [[primary-block data]]
          "text-image" [[secondary-block data]
                        [primary-block data]]
          [])))

(defn question-preview
  [{:keys [data]}]
  (print "data" data)
  [:div.preview-wrapper
   [template data]])
