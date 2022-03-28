(ns webchange.editor-v2.activity-dialogs.prepare-phrase-actions
  (:require
    [cljs.test :refer [deftest testing is]]
    [utils.compare-maps :refer [print-maps-comparison]]
    [webchange.editor-v2.activity-dialogs.stub-case-1-concept-data :as case-1]
    [webchange.editor-v2.activity-dialogs.stub-case-2-concept-data :as case-2]
    [webchange.editor-v2.activity-dialogs.stub-scene-data :refer [scene-data]]
    [webchange.editor-v2.activity-dialogs.form.utils :refer [prepare-phrase-actions]]))

(defn- get-scene-action-data
  [action-path]
  (get-in scene-data (concat [:actions] action-path)))

(defn- get-concept-action-data
  [concept-data action-path]
  (get-in concept-data (concat [:data] action-path)))

(defn- compare-results!
  [actual-result expected-result]
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
        (print "type:" (:type expected-phrase))
        (print-maps-comparison actual-phrase expected-phrase)))))

(deftest test-prepare-phrase-actions--case-1
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
                :concept-data        case-1/concept-data}
        actual-result (prepare-phrase-actions params)
        expected-result [#_0 {:type          :guide
                              :source        :scene
                              :delay         0

                              :parallel-mark :none
                              :selected?     false

                              :effect-name   "Hide guide"

                              :action-path   {:scene [:introduce-big-small :data 0]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 0])
                              :path          [:introduce-big-small :data 0]}
                         #_1 {:type          :phrase
                              :source        :scene
                              :delay         1000

                              :parallel-mark :none
                              :selected?     false

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          ""

                              :action-path   {:scene [:introduce-big-small :data 1]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 1])
                              :path          [:introduce-big-small :data 1]}
                         #_2 {:type          :phrase
                              :source        :scene
                              :delay         0

                              :parallel-mark :none
                              :selected?     true

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          "This is the letter"

                              :action-path   {:scene [:introduce-big-small :data 2]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 2])
                              :path          [:introduce-big-small :data 2]}
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
                              :action-data   (get-concept-action-data case-1/concept-data [:dialog-field-ef56a7df-3b1d-4abb-a43d-e06bad56bba5 :data 0])
                              :path          [:dialog-field-ef56a7df-3b1d-4abb-a43d-e06bad56bba5 :data 0]}
                         #_4 {:type          :phrase
                              :source        :scene
                              :delay         500

                              :parallel-mark :none
                              :selected?     false

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          "There are 2 ways to write it "

                              :action-path   {:scene [:introduce-big-small :data 4]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 4])
                              :path          [:introduce-big-small :data 4]}
                         #_5 {:type          :phrase
                              :source        :scene
                              :delay         0

                              :parallel-mark :start
                              :selected?     false

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          "the lowercase"

                              :action-path   {:scene [:introduce-big-small :data 5 :data 0]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 5 :data 0])
                              :path          [:introduce-big-small :data 5 :data 0]}
                         #_6 {:type          :effect
                              :source        :scene
                              :delay         0

                              :parallel-mark :end
                              :selected?     false

                              :effect        "glow-small"
                              :effect-name   "glow small"

                              :action-path   {:scene [:introduce-big-small :data 5 :data 1]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 5 :data 1])
                              :path          [:introduce-big-small :data 5 :data 1]}
                         #_7 {:type          :effect
                              :source        :scene
                              :delay         0

                              :parallel-mark :none
                              :selected?     false

                              :effect-name   "stop glow small"
                              :effect        "stop-glow-small"

                              :action-path   {:scene [:introduce-big-small :data 6]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 6])
                              :path          [:introduce-big-small :data 6]}
                         #_8 {:type          :phrase
                              :source        :scene
                              :delay         500

                              :parallel-mark :start
                              :selected?     false

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          "and the uppercase"

                              :action-path   {:scene [:introduce-big-small :data 7 :data 0]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 7 :data 0])
                              :path          [:introduce-big-small :data 7 :data 0]}
                         #_9 {:type          :effect
                              :source        :scene
                              :delay         0

                              :parallel-mark :end
                              :selected?     false

                              :effect-name   "glow big"
                              :effect        "glow-big"

                              :action-path   {:scene [:introduce-big-small :data 7 :data 1]}
                              :action-data   (get-scene-action-data [:introduce-big-small :data 7 :data 1])
                              :path          [:introduce-big-small :data 7 :data 1]}
                         #_10 {:type          :effect
                               :source        :scene
                               :delay         0

                               :parallel-mark :none
                               :selected?     false

                               :effect-name   "stop glow big"
                               :effect        "stop-glow-big"

                               :action-path   {:scene [:introduce-big-small :data 8]}
                               :action-data   (get-scene-action-data [:introduce-big-small :data 8])
                               :path          [:introduce-big-small :data 8]}
                         #_11 {:type          :phrase
                               :source        :scene
                               :delay         0

                               :parallel-mark :none
                               :selected?     false

                               :placeholder   nil
                               :character     "senora-vaca"
                               :text          "Let’s practice: say,"

                               :action-path   {:scene [:introduce-big-small :data 9]}
                               :action-data   (get-scene-action-data [:introduce-big-small :data 9])
                               :path          [:introduce-big-small :data 9]}
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
                               :action-data   (get-concept-action-data case-1/concept-data [:dialog-field-e0b429c2-cf25-4736-8160-75a9df533dac :data 0])
                               :path          [:dialog-field-e0b429c2-cf25-4736-8160-75a9df533dac :data 0]}]]
    (compare-results! actual-result expected-result)))

(deftest test-prepare-phrase-actions--case-2
  (let [params {:dialog-action-path  [:describe-writing]
                :current-action-path [:dialog-field-45d4b490-d167-4992-bf64-3487e8dbd862 :data 0]
                :available-effects   [{:name "redraw letter" :action "redraw-letter"}
                                      {:name "Show Preview image" :action "show-uploaded-image-3"}
                                      {:name "Hide Preview image" :action "hide-uploaded-image-3"}]
                :scene-data          scene-data
                :concept-data        case-2/concept-data}
        actual-result (prepare-phrase-actions params)
        expected-result [#_0 {:type          :phrase
                              :source        :scene
                              :delay         1000

                              :parallel-mark :none
                              :selected?     false

                              :character     "senora-vaca"
                              :text          "The letter "
                              :placeholder   nil

                              :action-path   {:scene [:describe-writing :data 0]}
                              :action-data   (get-scene-action-data [:describe-writing :data 0])
                              :path          [:describe-writing :data 0]}
                         #_1 {:type          :phrase
                              :source        :concept
                              :delay         0

                              :parallel-mark :none
                              :selected?     false

                              :placeholder   nil
                              :concept-name  "(a) apple"
                              :character     "senora-vaca"
                              :text          "“a”. "

                              :action-path   {:scene   [:describe-writing :data 1]
                                              :concept [:dialog-field-8d4ef5e3-6e37-4a69-988f-de8cf45903f9 :data 0]}
                              :action-data   (get-concept-action-data case-2/concept-data [:dialog-field-8d4ef5e3-6e37-4a69-988f-de8cf45903f9 :data 0])
                              :path          [:dialog-field-8d4ef5e3-6e37-4a69-988f-de8cf45903f9 :data 0]}
                         #_2 {:type          :phrase
                              :source        :scene
                              :delay         500

                              :parallel-mark :none
                              :selected?     false

                              :character     "senora-vaca"
                              :text          "goes "
                              :placeholder   nil

                              :action-path   {:scene [:describe-writing :data 2]}
                              :action-data   (get-scene-action-data [:describe-writing :data 2])
                              :path          [:describe-writing :data 2]}
                         #_3 {:type          :phrase
                              :source        :concept
                              :delay         500

                              :parallel-mark :none
                              :selected?     false

                              :placeholder   nil
                              :concept-name  "(a) apple"
                              :character     "senora-vaca"
                              :text          "curve, line down"

                              :action-path   {:scene   [:describe-writing :data 3]
                                              :concept [:dialog-field-136fa9c0-4bde-45a0-a7aa-f933ab1cbdeb :data 0]}
                              :action-data   (get-concept-action-data case-2/concept-data [:dialog-field-136fa9c0-4bde-45a0-a7aa-f933ab1cbdeb :data 0])
                              :path          [:dialog-field-136fa9c0-4bde-45a0-a7aa-f933ab1cbdeb :data 0]}
                         #_4 {:type          :phrase
                              :source        :concept
                              :delay         1000

                              :parallel-mark :none
                              :selected?     true

                              :placeholder   nil
                              :concept-name  "(a) apple"
                              :character     "senora-vaca"
                              :text          "A"

                              :action-path   {:scene   [:describe-writing :data 4]
                                              :concept [:dialog-field-45d4b490-d167-4992-bf64-3487e8dbd862 :data 0]}
                              :action-data   (get-concept-action-data case-2/concept-data [:dialog-field-45d4b490-d167-4992-bf64-3487e8dbd862 :data 0])
                              :path          [:dialog-field-45d4b490-d167-4992-bf64-3487e8dbd862 :data 0]}
                         #_5 {:type          :phrase
                              :source        :scene
                              :delay         500

                              :parallel-mark :none
                              :selected?     false

                              :placeholder   nil
                              :text          "goes"
                              :character     "senora-vaca"

                              :action-path   {:scene [:describe-writing :data 5]}
                              :action-data   (get-scene-action-data [:describe-writing :data 5])
                              :path          [:describe-writing :data 5]}
                         #_6 {:type          :phrase
                              :source        :concept
                              :delay         0

                              :parallel-mark :start
                              :selected?     false

                              :placeholder   nil
                              :concept-name  "(a) apple"
                              :character     "senora-vaca"
                              :text          "curve, line down"

                              :action-path   {:scene   [:describe-writing :data 6]
                                              :concept [:dialog-field-e51a7576-061f-4e94-bbd6-936fc7df466c :data 0 :data 0]}
                              :action-data   (get-concept-action-data case-2/concept-data [:dialog-field-e51a7576-061f-4e94-bbd6-936fc7df466c :data 0 :data 0])
                              :path          [:dialog-field-e51a7576-061f-4e94-bbd6-936fc7df466c :data 0 :data 0]}
                         #_7 {:type          :effect
                              :source        :concept
                              :delay         0

                              :parallel-mark :end
                              :selected?     false

                              :concept-name  "(a) apple"
                              :effect-name   "redraw letter"
                              :effect        "redraw-letter"

                              :action-path   {:scene   [:describe-writing :data 6]
                                              :concept [:dialog-field-e51a7576-061f-4e94-bbd6-936fc7df466c :data 0 :data 1]}
                              :action-data   (get-concept-action-data case-2/concept-data [:dialog-field-e51a7576-061f-4e94-bbd6-936fc7df466c :data 0 :data 1])
                              :path          [:dialog-field-e51a7576-061f-4e94-bbd6-936fc7df466c :data 0 :data 1]}
                         #_8 {:type          :phrase
                              :source        :concept
                              :delay         0

                              :parallel-mark :none
                              :selected?     false

                              :placeholder   nil
                              :concept-name  "(a) apple"
                              :character     "senora-vaca"
                              :text          "A"

                              :action-path   {:scene   [:describe-writing :data 7]
                                              :concept [:dialog-field-7dcb4949-4ae7-4947-bdb9-e744399aef73 :data 0]}
                              :action-data   (get-concept-action-data case-2/concept-data [:dialog-field-7dcb4949-4ae7-4947-bdb9-e744399aef73 :data 0])
                              :path          [:dialog-field-7dcb4949-4ae7-4947-bdb9-e744399aef73 :data 0]}
                         #_9 {:type          :phrase
                              :source        :scene
                              :delay         500

                              :parallel-mark :none
                              :selected?     false

                              :placeholder   nil
                              :character     "senora-vaca"
                              :text          "has a special sound it says"

                              :action-path   {:scene [:describe-writing :data 8]}
                              :action-data   (get-scene-action-data [:describe-writing :data 8])
                              :path          [:describe-writing :data 8]}
                         #_10 {:type          :phrase
                               :source        :concept
                               :delay         0

                               :parallel-mark :none
                               :selected?     false

                               :placeholder   nil
                               :concept-name  "(a) apple"
                               :character     "senora-vaca"
                               :text          "/a/"

                               :action-path   {:scene   [:describe-writing :data 9]
                                               :concept [:dialog-field-fbc892e9-258e-4a27-be9e-eb4d7011d394 :data 0]}
                               :action-data   (get-concept-action-data case-2/concept-data [:dialog-field-fbc892e9-258e-4a27-be9e-eb4d7011d394 :data 0])
                               :path          [:dialog-field-fbc892e9-258e-4a27-be9e-eb4d7011d394 :data 0]}
                         #_11 {:type          :phrase
                               :source        :scene
                               :delay         500

                               :parallel-mark :none
                               :selected?     false

                               :placeholder   nil
                               :character     "senora-vaca"
                               :text          "Say the"

                               :action-path   {:scene [:describe-writing :data 10]}
                               :action-data   (get-scene-action-data [:describe-writing :data 10])
                               :path          [:describe-writing :data 10]}
                         #_12 {:type          :phrase
                               :source        :concept
                               :delay         0

                               :parallel-mark :none
                               :selected?     false

                               :placeholder   nil
                               :concept-name  "(a) apple"
                               :character     "senora-vaca"
                               :text          "/a/"

                               :action-path   {:scene   [:describe-writing :data 11]
                                               :concept [:dialog-field-2b1330ce-7745-4a8e-87cc-7e77398939b2 :data 0]}
                               :action-data   (get-concept-action-data case-2/concept-data [:dialog-field-2b1330ce-7745-4a8e-87cc-7e77398939b2 :data 0])
                               :path          [:dialog-field-2b1330ce-7745-4a8e-87cc-7e77398939b2 :data 0]}
                         #_13 {:type          :phrase
                               :source        :scene
                               :delay         500

                               :parallel-mark :none
                               :selected?     false

                               :placeholder   nil
                               :character     "senora-vaca"
                               :text          "sound with me"

                               :action-path   {:scene [:describe-writing :data 12]}
                               :action-data   (get-scene-action-data [:describe-writing :data 12])
                               :path          [:describe-writing :data 12]}
                         #_14 {:type          :phrase
                               :source        :concept
                               :delay         500

                               :parallel-mark :none
                               :selected?     false

                               :placeholder   nil
                               :concept-name  "(a) apple"
                               :character     "senora-vaca"
                               :text          "/a/"

                               :action-path   {:scene   [:describe-writing :data 13]
                                               :concept [:dialog-field-02c7dada-5129-47cd-9f16-31298a67dca9 :data 0]}
                               :action-data   (get-concept-action-data case-2/concept-data [:dialog-field-02c7dada-5129-47cd-9f16-31298a67dca9 :data 0])
                               :path          [:dialog-field-02c7dada-5129-47cd-9f16-31298a67dca9 :data 0]}]]
    (compare-results! actual-result expected-result)))

(deftest test-prepare-phrase-actions--case-3
  (let [params {:dialog-action-path  [:dialog-main]
                :current-action-path [:dialog-main :data 1]
                :available-effects   []
                :scene-data          scene-data
                :concept-data        case-2/concept-data}
        actual-result (prepare-phrase-actions params)
        expected-result [{:type          :unknown
                          :source        :scene
                          :action-data   (get-scene-action-data [:dialog-main :data 0])
                          :action-path   {:scene [:dialog-main :data 0]}
                          :path          [:dialog-main :data 0]
                          :delay         0
                          :parallel-mark :none
                          :selected?     false}
                         {:path          [:dialog-main :data 1]
                          :action-path   {:scene [:dialog-main :data 1]}
                          :placeholder   "Enter phrase text"
                          :action-data   (get-scene-action-data [:dialog-main :data 1])
                          :type          :phrase
                          :source        :scene
                          :parallel-mark :none
                          :selected?     true
                          :delay         0
                          :character     "teacher"
                          :text          "¡Buena pregunta, Vera! ¡Yo me siento feliz cuando veo que mis estudiantes se ayudan entre ellos!"}
                         {:type          :unknown
                          :source        :scene
                          :action-data   (get-scene-action-data [:dialog-main :data 2])
                          :action-path   {:scene [:dialog-main :data 2]}
                          :path          [:dialog-main :data 2]
                          :delay         nil
                          :parallel-mark :none
                          :selected?     false}]]
    (compare-results! actual-result expected-result)))
