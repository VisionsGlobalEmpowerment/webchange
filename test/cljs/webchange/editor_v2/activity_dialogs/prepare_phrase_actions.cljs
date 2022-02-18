(ns webchange.editor-v2.activity-dialogs.prepare-phrase-actions
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.activity-dialogs.stub-concept-data :refer [concept-data]]
    [webchange.editor-v2.activity-dialogs.stub-scene-data :refer [scene-data]]
    [webchange.editor-v2.activity-dialogs.form.utils :refer [prepare-phrase-actions]]))

(defn- get-scene-action-data
  [action-path]
  (get-in scene-data (concat [:actions] action-path)))

(defn- get-concept-action-data
  [action-path]
  (get-in concept-data (concat [:data] action-path)))

(deftest test-prepare-phrase-actions
  (let [params {:dialog-action-path  [:introduce-big-small]
                :current-action-path [:introduce-big-small :data 2]
                :available-effects   [{:name "glow big", :action "glow-big"}
                                      {:name "stop glow big", :action "stop-glow-big"}
                                      {:name "glow small", :action "glow-small"}
                                      {:name "stop glow small", :action "stop-glow-small"}
                                      {:name "highlight big letter", :action "highlight-big-letter"}
                                      {:name "highlight small letter", :action "highlight-small-letter"}
                                      {:name "Show Preview image", :action "show-uploaded-image-3"}
                                      {:name "Hide Preview image", :action "hide-uploaded-image-3"}]
                :scene-data          scene-data
                :concept-data        concept-data}
        actual-result (prepare-phrase-actions params)
        expected-result [#_0 {:type          :guide
                              :source        :scene
                              :delay         0
                              :path          [:introduce-big-small :data 0]
                              :node-data     {:data (get-scene-action-data [:introduce-big-small :data 0])
                                              :path [:introduce-big-small 0]}
                              :parallel-mark :none
                              :effect-name   "Hide guide"
                              :selected?     false}
                         #_1 {:path          [:introduce-big-small :data 1]
                              :node-data     {:data (get-scene-action-data [:introduce-big-small :data 1])
                                              :path [:introduce-big-small 1]}
                              :placeholder   nil
                              :type          :phrase
                              :source        :scene
                              :parallel-mark :none
                              :selected?     false
                              :delay         1000
                              :character     "senora-vaca"
                              :text          ""}
                         #_2 {:path          [:introduce-big-small :data 2]
                              :node-data     {:data (get-scene-action-data [:introduce-big-small :data 2])
                                              :path [:introduce-big-small 2]}
                              :placeholder   nil
                              :type          :phrase
                              :source        :scene
                              :parallel-mark :none
                              :selected?     true
                              :delay         0
                              :character     "senora-vaca"
                              :text          "This is the letter"}
                         #_3 {:path          [:dialog-field-ef56a7df-3b1d-4abb-a43d-e06bad56bba5 :data 0]
                              :node-data     {:data             (get-concept-action-data [:dialog-field-ef56a7df-3b1d-4abb-a43d-e06bad56bba5 :data 0])
                                              :entity           :action
                                              :path             [:dialog-field-ef56a7df-3b1d-4abb-a43d-e06bad56bba5 0]
                                              :action-node-data {:data {:type     "action"
                                                                        :from-var [{:var-name     "current-concept"
                                                                                    :var-property "dialog-field-ef56a7df-3b1d-4abb-a43d-e06bad56bba5"}]}
                                                                 :path [:introduce-big-small 3]}}
                              :placeholder   nil
                              :type          :phrase
                              :source        :concept
                              :concept-name  "(a) apple"
                              :parallel-mark :none
                              :selected?     false
                              :delay         0
                              :character     "senora-vaca"
                              :text          "a"}
                         #_4 {:path          [:introduce-big-small :data 4]
                              :node-data     {:data (get-scene-action-data [:introduce-big-small :data 4])
                                              :path [:introduce-big-small 4]}
                              :placeholder   nil
                              :type          :phrase
                              :source        :scene
                              :parallel-mark :none
                              :selected?     false
                              :delay         500
                              :character     "senora-vaca"
                              :text          "There are 2 ways to write it "}
                         #_5 {:path          [:introduce-big-small :data 5 :data 0]
                              :node-data     {:data (get-scene-action-data [:introduce-big-small :data 5 :data 0])
                                              :path [:introduce-big-small 5 0]}
                              :placeholder   nil
                              :type          :phrase
                              :source        :scene
                              :parallel-mark :start
                              :selected?     false
                              :delay         0
                              :character     "senora-vaca"
                              :text          "the lowercase"}
                         #_6 {:path          [:introduce-big-small :data 5 :data 1]
                              :node-data     {:data (get-scene-action-data [:introduce-big-small :data 5 :data 1])
                                              :path [:introduce-big-small 5 1]}
                              :type          :effect
                              :source        :scene
                              :effect-name   "glow small"
                              :parallel-mark :end
                              :effect        "glow-small"
                              :selected?     false
                              :delay         0}
                         #_7 {:path          [:introduce-big-small :data 6]
                              :node-data     {:data (get-scene-action-data [:introduce-big-small :data 6])
                                              :path [:introduce-big-small 6]}
                              :type          :effect
                              :source        :scene
                              :effect-name   "stop glow small"
                              :parallel-mark :none
                              :effect        "stop-glow-small"
                              :selected?     false
                              :delay         0}
                         #_8 {:path          [:introduce-big-small :data 7 :data 0]
                              :node-data     {:data (get-scene-action-data [:introduce-big-small :data 7 :data 0])
                                              :path [:introduce-big-small 7 0]}
                              :placeholder   nil
                              :type          :phrase
                              :source        :scene
                              :parallel-mark :start
                              :selected?     false
                              :delay         500
                              :character     "senora-vaca"
                              :text          "and the uppercase"}
                         #_9 {:path          [:introduce-big-small :data 7 :data 1]
                              :node-data     {:data (get-scene-action-data [:introduce-big-small :data 7 :data 1])
                                              :path [:introduce-big-small 7 1]}
                              :type          :effect
                              :source        :scene
                              :effect-name   "glow big"
                              :parallel-mark :end
                              :effect        "glow-big"
                              :selected?     false
                              :delay         0}
                         #_10 {:path          [:introduce-big-small :data 8]
                               :node-data     {:data (get-scene-action-data [:introduce-big-small :data 8])
                                               :path [:introduce-big-small 8]}
                               :type          :effect
                               :source        :scene
                               :effect-name   "stop glow big"
                               :parallel-mark :none
                               :effect        "stop-glow-big"
                               :selected?     false
                               :delay         0}
                         #_11 {:path          [:introduce-big-small :data 9]
                               :node-data     {:data (get-scene-action-data [:introduce-big-small :data 9])
                                               :path [:introduce-big-small 9]}
                               :placeholder   nil
                               :type          :phrase
                               :source        :scene
                               :parallel-mark :none
                               :selected?     false
                               :delay         0
                               :character     "senora-vaca"
                               :text          "Let’s practice: say,"}
                         #_12 {:path          [:dialog-field-e0b429c2-cf25-4736-8160-75a9df533dac :data 0]
                               :node-data     {:data             (get-concept-action-data [:dialog-field-e0b429c2-cf25-4736-8160-75a9df533dac :data 0])
                                               :entity           :action
                                               :path             [:dialog-field-e0b429c2-cf25-4736-8160-75a9df533dac 0]
                                               :action-node-data {:data {:type     "action"
                                                                         :from-var [{:var-name     "current-concept"
                                                                                     :var-property "dialog-field-e0b429c2-cf25-4736-8160-75a9df533dac"}]}
                                                                  :path [:introduce-big-small 10]}}
                               :placeholder   nil
                               :type          :phrase
                               :source        :concept
                               :concept-name  "(a) apple"
                               :parallel-mark :none
                               :selected?     false
                               :delay         250
                               :character     "senora-vaca"
                               :text          "a"}]]

    (is (= (count actual-result)
           (count expected-result)))
    (doseq [idx (range (min (count actual-result)
                            (count expected-result)))]
      (let [actual-phrase (nth actual-result idx)
            expected-phrase (nth expected-result idx)]
        (is (= actual-phrase expected-phrase))
        (when-not (= actual-phrase expected-phrase)
          (print "--- --- ---")
          (print "idx:" idx)
          (print "path:" (if (= (:path actual-phrase) (:path expected-phrase))
                           (:path actual-phrase)
                           [(:path actual-phrase) (:path expected-phrase)]))
          (print-maps-comparison actual-phrase expected-phrase))))))
