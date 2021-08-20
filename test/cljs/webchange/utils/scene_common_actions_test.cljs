(ns webchange.utils.scene-common-actions-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.utils.list :as lists]
    [webchange.utils.scene-common-actions :refer [remove-image]]
    [webchange.utils.scene-data :refer [find-action-recursively get-available-actions get-updates-history]]))

(defonce uploaded-image-name "uploaded-image-1")
(defonce uploaded-image-src "/upload/NFTPZIBJBISFBZGG.png")
(defonce uploaded-image-related-actions ["show-uploaded-image-1" "hide-uploaded-image-1"])

(defonce initial-scene-data {:assets        [{:url   "/upload/NCXFSGZIKQCVAKQZ.mp3"
                                              :date  1629271789116
                                              :lang  nil
                                              :size  2
                                              :type  "audio"
                                              :alias "none"}
                                             {:url  uploaded-image-src
                                              :size 1
                                              :type "image"}]

                             :objects       {:credits-page-title {:y           304
                                                                  :align       "center"
                                                                  :font-size   64
                                                                  :fill        "black"
                                                                  :editable?   {:select true}
                                                                  :type        "text"
                                                                  :x           480
                                                                  :font-family "Lexend Deca"
                                                                  :text        "my title"}
                                             :uploaded-image-1   {:y         736.6666666666666
                                                                  :editable? {:drag          true
                                                                              :select        true
                                                                              :show-in-tree? true}
                                                                  :type      "image"
                                                                  :src       uploaded-image-src
                                                                  :alias     "Test image"
                                                                  :origin    {:type "center-center"}
                                                                  :x         954.9999999999997
                                                                  :visible   true
                                                                  :links     [{:id   "show-uploaded-image-1"
                                                                               :type "action"}
                                                                              {:id   "hide-uploaded-image-1"
                                                                               :type "action"}]}}

                             :scene-objects [["layered-background"] ["teacher"] ["book-background"] ["book" "vera-04-girl-1" "uploaded-image-1"]]

                             :actions       {:show-uploaded-image-1 {:type       "set-attribute"
                                                                     :target     "uploaded-image-1"
                                                                     :attr-name  "visible"
                                                                     :attr-value true}
                                             :hide-uploaded-image-1 {:type       "set-attribute"
                                                                     :target     "uploaded-image-1"
                                                                     :attr-name  "visible"
                                                                     :attr-value false}
                                             :dialog-intro          {:data               [{:data [{:type     "empty"
                                                                                                   :duration 0}
                                                                                                  {:id   "show-uploaded-image-1"
                                                                                                   :type "action"}]
                                                                                           :type "sequence-data"}
                                                                                          {:data [{:type     "empty"
                                                                                                   :duration 0}
                                                                                                  {:id     "emotion_sad"
                                                                                                   :loop   false
                                                                                                   :type   "add-animation"
                                                                                                   :track  3
                                                                                                   :target "teacher"}]
                                                                                           :type "sequence-data"}
                                                                                          {:data [{:type     "empty"
                                                                                                   :duration 0}
                                                                                                  {:id   "hide-uploaded-image-1"
                                                                                                   :type "action"}]
                                                                                           :type "sequence-data"}]
                                                                     :type               "sequence-data"
                                                                     :phrase             "instruction"
                                                                     :concept-var        "current-word"
                                                                     :editor-type        "dialog"
                                                                     :phrase-description "Instruction"}}

                             :metadata      {:stage-size        "contain"
                                             :history           {:created {:activity-name "Book"
                                                                           :characters    [{:name     "teacher"
                                                                                            :skeleton "senoravaca"}]
                                                                           :cover-title   "my title"
                                                                           :name          "Book"
                                                                           :cover-layout  "title-top"
                                                                           :lang          "English"
                                                                           :skills        []
                                                                           :illustrators  ["illustrator"]
                                                                           :course-name   "my title"
                                                                           :cover-image   {:src "/upload/BYBCRIPVTBJJTLAQ.png"}
                                                                           :authors       ["author"]
                                                                           :template-id   32}
                                                                 :updated [{:data           {:name "vera"
                                                                                             :skin "04 girl"}
                                                                            :action         "add-character"
                                                                            :common-action? true}
                                                                           {:data           {:name  "Test image"
                                                                                             :image {:src uploaded-image-src}}
                                                                            :action         "add-image"
                                                                            :common-action? true}]}

                                             :available-actions [{:name   "Hide vera-04-girl-1"
                                                                  :type   "image"
                                                                  :links  [{:id   "vera-04-girl-1"
                                                                            :type "object"}]
                                                                  :action "hide-character-vera-04-girl-1"}
                                                                 {:name   "Show Test image"
                                                                  :type   "image"
                                                                  :links  [{:id   "uploaded-image-1"
                                                                            :type "object"}]
                                                                  :action "show-uploaded-image-1"}
                                                                 {:name   "Hide Test image"
                                                                  :type   "image"
                                                                  :links  [{:id   "uploaded-image-1"
                                                                            :type "object"}]
                                                                  :action "hide-uploaded-image-1"}]}})

(deftest uploaded-image-removed-correctly
  (let [result-scene-data (remove-image initial-scene-data {:name uploaded-image-name})]
    (testing "Image removed from :assets"
      (is (= (some (fn [{:keys [url] :as asset}]
                     (and (= url uploaded-image-src) asset))
                   (:assets result-scene-data))
             nil)))

    (testing "Image removed from :objects"
      (is (not (contains? (:objects result-scene-data) (keyword uploaded-image-name)))))

    (testing "Image removed from :scene-objects"
      (is (= (some (fn [layer]
                     (and (lists/in-list? layer uploaded-image-name) layer))
                   (:scene-objects result-scene-data))
             nil)))

    (testing "Show/hide actions removed from :action"
      (doseq [related-action uploaded-image-related-actions]
        (is (not (contains? (:actions result-scene-data) (keyword related-action))))))

    (testing "Show/hide actions removed from sequences where they are used"
      (is (= (find-action-recursively result-scene-data (fn [{:keys [id type]}]
                                                          (and (= type "action")
                                                               (= id "hide-uploaded-image-1"))))
             nil)))

    (testing "Show/hide actions removed from available actions"
      (let [available-actions (get-available-actions result-scene-data)]
        (doseq [related-action uploaded-image-related-actions]
          (is (= (some (fn [{:keys [action] :as available-action}]
                         (and (= action related-action) available-action))
                       available-actions)
                 nil)))))

    (testing "Image removed from scene editing history"
      (let [updates-history (get-updates-history result-scene-data)]
        (is (= (some (fn [history-item]
                       (let [src (get-in history-item [:data :image :src])
                             action-type (get history-item :action)]
                         (and (= action-type "add-image")
                              (= src uploaded-image-src)
                              history-item)))
                     updates-history)
               nil))))

    (testing "Check overall scene data"
      (is (= result-scene-data {:assets        [{:url   "/upload/NCXFSGZIKQCVAKQZ.mp3"
                                                 :date  1629271789116
                                                 :lang  nil
                                                 :size  2
                                                 :type  "audio"
                                                 :alias "none"}]

                                :objects       {:credits-page-title {:y           304
                                                                     :align       "center"
                                                                     :font-size   64
                                                                     :fill        "black"
                                                                     :editable?   {:select true}
                                                                     :type        "text"
                                                                     :x           480
                                                                     :font-family "Lexend Deca"
                                                                     :text        "my title"}}

                                :scene-objects [["layered-background"] ["teacher"] ["book-background"] ["book" "vera-04-girl-1"]]

                                :actions       {:dialog-intro {:data               [{:data [{:type     "empty"
                                                                                             :duration 0}
                                                                                            {:id     "emotion_sad"
                                                                                             :loop   false
                                                                                             :type   "add-animation"
                                                                                             :track  3
                                                                                             :target "teacher"}]
                                                                                     :type "sequence-data"}]
                                                               :type               "sequence-data"
                                                               :phrase             "instruction"
                                                               :concept-var        "current-word"
                                                               :editor-type        "dialog"
                                                               :phrase-description "Instruction"}}

                                :metadata      {:stage-size        "contain"
                                                :history           {:created {:activity-name "Book"
                                                                              :characters    [{:name     "teacher"
                                                                                               :skeleton "senoravaca"}]
                                                                              :cover-title   "my title"
                                                                              :name          "Book"
                                                                              :cover-layout  "title-top"
                                                                              :lang          "English"
                                                                              :skills        []
                                                                              :illustrators  ["illustrator"]
                                                                              :course-name   "my title"
                                                                              :cover-image   {:src "/upload/BYBCRIPVTBJJTLAQ.png"}
                                                                              :authors       ["author"]
                                                                              :template-id   32}
                                                                    :updated [{:data           {:name "vera"
                                                                                                :skin "04 girl"}
                                                                               :action         "add-character"
                                                                               :common-action? true}]}

                                                :available-actions [{:name   "Hide vera-04-girl-1"
                                                                     :type   "image"
                                                                     :links  [{:id   "vera-04-girl-1"
                                                                               :type "object"}]
                                                                     :action "hide-character-vera-04-girl-1"}]}})))))
