(ns webchange.interpreter.renderer.text.chunks-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.interpreter.renderer.scene.components.text.chunks :refer [fill-absent-chunks set-chunk-text]]))

(deftest test-fill-absent-chunks
  (testing "should split absent chunks on new line symbol"
    (let [text-props {:text " xxx \n yy "}
          chunks-indexed [{:start 1 :end 4 :index 0}
                          {:start 7 :end 9 :index 1}]]
      (let [actual-result (->> (fill-absent-chunks text-props chunks-indexed)
                               (map (fn [chunk] (-> chunk
                                                    (set-chunk-text text-props)))))
            expected-result [{:start 0 :end 1 :text " "}
                             {:start 1 :end 4 :text "xxx" :index 0}
                             {:start 4 :end 6 :text " \n"}
                             {:start 6 :end 7 :text " "}
                             {:start 7 :end 9 :text "yy" :index 1}]]
        (is (= actual-result expected-result))))))
