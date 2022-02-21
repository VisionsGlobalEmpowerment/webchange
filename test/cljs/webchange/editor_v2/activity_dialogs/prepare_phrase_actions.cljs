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

                              :parallel-mark :none
                              :selected?     false

                              :effect-name   "Hide guide"

                              :action-path   {:scene [:introduce-big-small :data 0]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 0])}
                         #_1 {:type          :phrase
                              :source        :scene
                              :delay         1000

                              :parallel-mark :none
                              :selected?     false

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          ""

                              :action-path   {:scene [:introduce-big-small :data 1]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 1])}
                         #_2 {:type          :phrase
                              :source        :scene
                              :delay         0

                              :parallel-mark :none
                              :selected?     true

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          "This is the letter"

                              :action-path   {:scene [:introduce-big-small :data 2]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 2])}
                         #_3 {:type          :phrase
                              :source        :concept
                              :delay         0

                              :parallel-mark :none
                              :selected?     false

                              :placeholder   nil
                              :concept-name  "(a) apple"
                              :character     "senora-vaca"
                              :text          "a"

                              :action-path   {:scene   [:introduce-big-small :data 3]
                                              :concept [:dialog-field-ef56a7df-3b1d-4abb-a43d-e06bad56bba5 :data 0]}
                              :action-data   (get-concept-action-data [:dialog-field-ef56a7df-3b1d-4abb-a43d-e06bad56bba5 :data 0])}
                         #_4 {:type          :phrase
                              :source        :scene
                              :delay         500

                              :parallel-mark :none
                              :selected?     false

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          "There are 2 ways to write it "

                              :action-path   {:scene [:introduce-big-small :data 4]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 4])}
                         #_5 {:type          :phrase
                              :source        :scene
                              :delay         0

                              :parallel-mark :start
                              :selected?     false

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          "the lowercase"

                              :action-path   {:scene [:introduce-big-small :data 5 :data 0]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 5 :data 0])}
                         #_6 {:type          :effect
                              :source        :scene
                              :delay         0

                              :parallel-mark :end
                              :selected?     false

                              :effect        "glow-small"
                              :effect-name   "glow small"

                              :action-path   {:scene [:introduce-big-small :data 5 :data 1]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 5 :data 1])}
                         #_7 {:type          :effect
                              :source        :scene
                              :delay         0

                              :parallel-mark :none
                              :selected?     false

                              :effect-name   "stop glow small"
                              :effect        "stop-glow-small"

                              :action-path   {:scene [:introduce-big-small :data 6]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 6])}
                         #_8 {:type          :phrase
                              :source        :scene
                              :delay         500

                              :parallel-mark :start
                              :selected?     false

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          "and the uppercase"

                              :action-path   {:scene [:introduce-big-small :data 7 :data 0]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 7 :data 0])}
                         #_9 {:type          :effect
                              :source        :scene
                              :delay         0

                              :parallel-mark :end
                              :selected?     false

                              :effect-name   "glow big"
                              :effect        "glow-big"

                              :action-path   {:scene [:introduce-big-small :data 7 :data 1]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 7 :data 1])}
                         #_10 {:type          :effect
                               :source        :scene
                               :delay         0

                               :parallel-mark :none
                               :selected?     false

                               :effect-name   "stop glow big"
                               :effect        "stop-glow-big"

                               :action-path   {:scene [:introduce-big-small :data 8]}
                               :action-data   (get-scene-action-data [:introduce-big-small :data 8])}
                         #_11 {:type          :phrase
                               :source        :scene
                               :delay         0

                               :parallel-mark :none
                               :selected?     false

                               :placeholder   nil
                               :character     "senora-vaca"
                               :text          "Let’s practice: say,"

                               :action-path   {:scene [:introduce-big-small :data 9]}
                               :action-data   (get-scene-action-data [:introduce-big-small :data 9])}
                         #_12 {:type          :phrase
                               :source        :concept
                               :delay         250

                               :parallel-mark :none
                               :selected?     false

                               :placeholder   nil
                               :concept-name  "(a) apple"
                               :character     "senora-vaca"
                               :text          "a"

                               :action-path   {:scene   [:introduce-big-small :data 10]
                                               :concept [:dialog-field-e0b429c2-cf25-4736-8160-75a9df533dac :data 0]}
                               :action-data   (get-concept-action-data [:dialog-field-e0b429c2-cf25-4736-8160-75a9df533dac :data 0])}]]
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
