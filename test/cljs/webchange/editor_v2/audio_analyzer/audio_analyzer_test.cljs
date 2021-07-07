(ns webchange.editor-v2.audio-analyzer.audio-analyzer-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.audio-analyzer.data :refer [script-data]]
    [webchange.editor-v2.audio-analyzer.region-data :refer [get-start-end-for-text]]
    [webchange.editor-v2.audio-analyzer.talk-data :refer [get-chunks-for-text]]))

(deftest test-get-action-audio-data-region
  (let [text "This branch looks lovely! It seems perfect for me. What do you think? Do you agree?"
        {start :start end :end} (get-start-end-for-text text script-data)]
      (is (= start 49.98))
      (is (= end 55.86))))


(deftest test-short-text-region
  (let [text "This branch looks lovely!"
        {start :start end :end} (get-start-end-for-text text script-data)]
    (is (= start 49.98))
    (is (= end 51.81))))

(deftest test-can-find-end-region-correctly
  (let [text "This branch looks lovely! It seems perfect for me. What do you think? Do you agree?"
        script-data [{:conf 1.0, :end 2, :start 1, :word "this"}
                     {:conf 1.0, :end 4, :start 3, :word "branch"}
                     {:conf 1.0, :end 6, :start 5, :word "looks"}
                     {:conf 1.0, :end 8, :start 7, :word "lovely"}
                     {:conf 1.0, :end 10, :start 9, :word "it"}
                     {:conf 1.0, :end 12, :start 11, :word "seems"}
                     {:conf 1.0, :end 14, :start 13, :word "perfect"}
                     {:conf 1.0, :end 16, :start 15, :word "for"}
                     {:conf 1.0, :end 18, :start 17, :word "me"}
                     {:conf 1.0, :end 20, :start 19, :word "what"}
                     {:conf 0.537042, :end 22, :start 21, :word "do"}
                     {:conf 1.0, :end 24, :start 23, :word "yau"}
                     {:conf 1.0, :end 26, :start 25, :word "think"}
                     {:conf 1.0, :end 28, :start 27, :word "do"}
                     {:conf 1.0, :end 30, :start 29, :word "you"}
                     {:conf 1.0, :end 32, :start 31, :word "agree"}
                     {:conf 1.0, :end 34, :start 33, :word "what"}
                     {:conf 0.537042, :end 36, :start 35, :word "do"}
                     {:conf 1.0, :end 38, :start 37, :word "you"}
                     {:conf 1.0, :end 40, :start 39, :word "think"}
                     {:conf 1.0, :end 42, :start 41, :word "do"}
                     {:conf 1.0, :end 44, :start 43, :word "you"}
                     {:conf 1.0, :end 46, :start 45, :word "agree"}
                     {:conf 1.0, :end 48, :start 47, :word "what"}
                     {:conf 0.537042, :end 51, :start 49, :word "do"}
                     {:conf 1.0, :end 53, :start 52, :word "you"}
                     {:conf 1.0, :end 55, :start 54, :word "think"}
                     {:conf 1.0, :end 57, :start 56, :word "do"}
                     {:conf 1.0, :end 59, :start 58, :word "you"}
                     {:conf 1.0, :end 61, :start 60, :word "agree"}
                     ]
        {start :start end :end} (get-start-end-for-text text script-data)]
    (is (= start 1))
    (is (= end 32))))

(deftest test-get-chunks
  (let [text "This branch looks lovely! It seems perfect for me. What do you think? Do you agree?"
        script-data [{:conf 1.0, :end 2, :start 1, :word "this"}
                     {:conf 1.0, :end 4, :start 3, :word "branch"}
                     {:conf 1.0, :end 6, :start 5, :word "looks"}
                     {:conf 1.0, :end 8, :start 7, :word "lovely"}
                     {:conf 1.0, :end 10, :start 9, :word "it"}
                     {:conf 1.0, :end 12, :start 11, :word "seems"}
                     {:conf 1.0, :end 14, :start 13, :word "perfect"}
                     {:conf 1.0, :end 16, :start 15, :word "for"}
                     {:conf 1.0, :end 18, :start 17, :word "me"}
                     {:conf 1.0, :end 20, :start 19, :word "what"}
                     {:conf 0.537042, :end 22, :start 21, :word "do"}
                     {:conf 1.0, :end 24, :start 23, :word "yau"}
                     {:conf 1.0, :end 26, :start 25, :word "think"}
                     {:conf 1.0, :end 28, :start 27, :word "do"}
                     {:conf 1.0, :end 30, :start 29, :word "you"}
                     {:conf 1.0, :end 32, :start 31, :word "agree"}
                     {:conf 1.0, :end 34, :start 33, :word "what"}
                     {:conf 0.537042, :end 36, :start 35, :word "do"}
                     {:conf 1.0, :end 38, :start 37, :word "you"}
                     {:conf 1.0, :end 40, :start 39, :word "think"}
                     {:conf 1.0, :end 42, :start 41, :word "do"}
                     {:conf 1.0, :end 44, :start 43, :word "you"}
                     {:conf 1.0, :end 46, :start 45, :word "agree"}
                     {:conf 1.0, :end 48, :start 47, :word "what"}
                     {:conf 0.537042, :end 51, :start 49, :word "do"}
                     {:conf 1.0, :end 53, :start 52, :word "you"}
                     {:conf 1.0, :end 55, :start 54, :word "think"}
                     {:conf 1.0, :end 57, :start 56, :word "do"}
                     {:conf 1.0, :end 59, :start 58, :word "you"}
                     {:conf 1.0, :end 61, :start 60, :word "agree"}]
        chunks (get-chunks-for-text text script-data {:start 1 :end 34})]
    (is (= 1 (:at (first chunks))))
    (is (= 31 (:at (last chunks))))
    (is (= 16 (count chunks)))))


(deftest test-get-action-audio-data-region
  (let [text "This branch looks lovely! It seems perfect for me. What do you think? Do you agree?"
        region (get-start-end-for-text text script-data)
        chunks (get-chunks-for-text text script-data region)]
    (is (= 49.98 (:at (first chunks))))
    (is (= 55.32 (:at (last chunks))))
    (is (= 16 (count chunks)))))
