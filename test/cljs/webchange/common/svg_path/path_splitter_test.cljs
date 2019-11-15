(ns webchange.common.svg-path.path-splitter-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.common.svg-path.path-splitter :as ps]))

(deftest test-split-path-array
  (let [split-path-array #'ps/split-path-array]
    (is (= (split-path-array ps/delimiters ["M 80 25 M 80 10"]) ["M 80 25" "M 80 10"]))
    (is (= (split-path-array ps/delimiters ["M 80 25 m 10 15"]) ["M 80 25" "m 10 15"]))
    (is (= (split-path-array ps/delimiters ["L 10 20 L 15 35"]) ["L 10 20" "L 15 35"]))
    (is (= (split-path-array ps/delimiters ["L 10 20 l 15 35"]) ["L 10 20" "l 15 35"]))
    (is (= (split-path-array ps/delimiters ["H 5 V 15"]) ["H 5" "V 15"]))
    (is (= (split-path-array ps/delimiters ["h 20 v 35"]) ["h 20" "v 35"]))
    (is (= (split-path-array ps/delimiters ["C 20 20, 40 20, 50 10 c 20 20, 40 20, 50 10"])
           ["C 20 20, 40 20, 50 10" "c 20 20, 40 20, 50 10"]))
    (is (= (split-path-array ps/delimiters ["C 20 20 40 20 50 10 c 20 20 40 20 50 10"])
           ["C 20 20 40 20 50 10" "c 20 20 40 20 50 10"]))
    (is (= (split-path-array ps/delimiters ["Q 20 20, 40 20 q 20 20, 40 20"])
           ["Q 20 20, 40 20" "q 20 20, 40 20"]))
    (is (= (split-path-array ps/delimiters ["Q 20 20 40 20 q 20 20 40 20"])
           ["Q 20 20 40 20" "q 20 20 40 20"]))
    (is (= (split-path-array ps/delimiters ["S 20 20 40 20 s 20 20 40 20"])
           ["S 20 20 40 20" "s 20 20 40 20"]))
    (is (= (split-path-array ps/delimiters ["T 40 20 t 40 20"])
           ["T 40 20" "t 40 20"]))
    (is (= (split-path-array ps/delimiters ["A 40,40 0 1,0 80,75 a 40,40 0 1,0 80,75"])
           ["A 40,40 0 1,0 80,75" "a 40,40 0 1,0 80,75"]))

    (is (= (split-path-array ps/delimiters ["M10,20L40,50Q20,30 40,10"]) ["M10,20" "L40,50" "Q20,30 40,10"]))
    (is (= (split-path-array ps/delimiters ["M227.03,235.02L356.22,93.94"]) ["M227.03,235.02" "L356.22,93.94"]))
    ))

(deftest test-parse-path-str
  (let [parse-path-str #'ps/parse-path-str]
    (is (= (parse-path-str "M 80 25") ["M" 80 25]))
    (is (= (parse-path-str "m 10 15") ["m" 10 15]))
    (is (= (parse-path-str "L 10 20") ["L" 10 20]))
    (is (= (parse-path-str "l 15 35") ["l" 15 35]))
    (is (= (parse-path-str "H 80") ["H" 80]))
    (is (= (parse-path-str "h 10") ["h" 10]))
    (is (= (parse-path-str "V 10") ["V" 10]))
    (is (= (parse-path-str "v 15") ["v" 15]))
    (is (= (parse-path-str "C 20 20, 40 20, 50 10") ["C" 20 20 40 20 50 10]))
    (is (= (parse-path-str "C 20 20 40 20 50 10") ["C" 20 20 40 20 50 10]))
    (is (= (parse-path-str "c 20 20, 40 20, 50 10") ["c" 20 20 40 20 50 10]))
    (is (= (parse-path-str "c 20 20 40 20 50 10") ["c" 20 20 40 20 50 10]))
    (is (= (parse-path-str "Q 20 20, 40 20") ["Q" 20 20 40 20]))
    (is (= (parse-path-str "Q 20 20 40 20") ["Q" 20 20 40 20]))
    (is (= (parse-path-str "q 20 20, 40 20") ["q" 20 20 40 20]))
    (is (= (parse-path-str "q 20 20 40 20") ["q" 20 20 40 20]))
    (is (= (parse-path-str "S 20 20, 40 20") ["S" 20 20 40 20]))
    (is (= (parse-path-str "S 20 20 40 20") ["S" 20 20 40 20]))
    (is (= (parse-path-str "s 20 20, 40 20") ["s" 20 20 40 20]))
    (is (= (parse-path-str "s 20 20 40 20") ["s" 20 20 40 20]))
    (is (= (parse-path-str "T 40 20") ["T" 40 20]))
    (is (= (parse-path-str "t 40 20") ["t" 40 20]))
    (is (= (parse-path-str "A 40,40 0 1,0 80,75") ["A" 40 40 0 1 0 80 75]))
    (is (= (parse-path-str "a 40,40 0 1,0 80,75") ["a" 40 40 0 1 0 80 75]))

    (is (= (parse-path-str "M10,20") ["M" 10 20]))
    (is (= (parse-path-str "Q20,30 40,10") ["Q" 20 30 40 10]))
    (is (= (parse-path-str "L356.22,93.94") ["L" 356.22 93.94]))
    ))

(deftest test-relative->absolute
  (let [relative->absolute #'ps/relative->absolute]
    (is (= (relative->absolute {:x 10 :y 20} ["M" 80 25]) ["M" 80 25]))
    (is (= (relative->absolute {:x 10 :y 20} ["m" 80 25]) ["M" 90 45]))
    (is (= (relative->absolute {:x 10 :y 20} ["L" 10 30]) ["L" 10 30]))
    (is (= (relative->absolute {:x 10 :y 20} ["l" 10 30]) ["L" 20 50]))
    (is (= (relative->absolute {:x 10 :y 20} ["H" 20]) ["H" 20]))
    (is (= (relative->absolute {:x 10 :y 20} ["h" 20]) ["H" 30]))
    (is (= (relative->absolute {:x 10 :y 20} ["V" 30]) ["V" 30]))
    (is (= (relative->absolute {:x 10 :y 20} ["v" 30]) ["V" 50]))
    (is (= (relative->absolute {:x 10 :y 20} ["C" 20 20 40 20 50 10]) ["C" 20 20 40 20 50 10]))
    (is (= (relative->absolute {:x 10 :y 20} ["c" 20 20 40 20 50 10]) ["C" 30 40 50 40 60 30]))
    (is (= (relative->absolute {:x 10 :y 20} ["Q" 20 20 40 20]) ["Q" 20 20 40 20]))
    (is (= (relative->absolute {:x 10 :y 20} ["q" 20 20 40 20]) ["Q" 30 40 50 40]))
    (is (= (relative->absolute {:x 10 :y 20} ["S" 20 20 40 20]) ["S" 20 20 40 20]))
    (is (= (relative->absolute {:x 10 :y 20} ["s" 20 20 40 20]) ["S" 30 40 50 40]))
    (is (= (relative->absolute {:x 10 :y 20} ["T" 40 20]) ["T" 40 20]))
    (is (= (relative->absolute {:x 10 :y 20} ["t" 40 20]) ["T" 50 40]))
    (is (= (relative->absolute {:x 10 :y 20} ["A" 40 40 0 1 0 80 75]) ["A" 40 40 0 1 0 80 75]))
    (is (= (relative->absolute {:x 10 :y 20} ["a" 40 40 0 1 0 80 75]) ["A" 40 40 0 1 0 90 95]))
    ))

(deftest test-relatives->absolutes
  (let [relatives->absolutes #'ps/relatives->absolutes]
    (is (= (relatives->absolutes
             [["M" 80 25] ["l" 10 15] ["h" 5] ["v" 10] ["V" 20]])
           [["M" 80 25] ["L" 90 40] ["H" 95] ["V" 50] ["V" 20]]))
    (is (= (relatives->absolutes
             [["M" 80 25] ["c" 20 20 40 20 50 10]])
           [["M" 80 25] ["C" 100 45 120 45 130 35]]))
    (is (= (relatives->absolutes
             [["M" 80 25] ["q" 20 20 40 20]])
           [["M" 80 25] ["Q" 100 45 120 45]]))
    (is (= (relatives->absolutes
             [["C" 100 45 120 45 130 35] ["s" 20 20 40 20]])
           [["C" 100 45 120 45 130 35] ["S" 150 55 170 55]]))
    ))

(deftest test-split-should-return-vectors-of-commands-with-coordinates
  (is (= (ps/split-path "M72.5,60.1a37.5,37.5,0,1,1,0-39.2m0-18v75")
         [["M" 72.5 60.1]
          ["A" 37.5 37.5 0 1 1 72.5 20.9]
          ["M" 72.5 2.8999999999999986]
          ["V" 77.9]])))

(deftest test-split-with-should-return-strings
  (is (= (ps/split-path-str-with "M72.5,60.1a37.5,37.5,0,1,1,0-39.2m0-18v75" ["M" "m"])
         ["M 72.5 60.1 A 37.5 37.5 0 1 1 72.5 20.9"
            "M 72.5 2.8999999999999986 V 77.9"])))