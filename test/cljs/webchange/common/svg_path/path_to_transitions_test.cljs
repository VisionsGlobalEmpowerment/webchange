(ns webchange.common.svg-path.path-to-transitions-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.common.svg-path.path-to-transitions :as ptt]))

(deftest test-apply-origin
  (let [apply-origin #'ptt/apply-origin]
    (is (= (apply-origin {:x 10 :y 15} ["M" 80 25]) ["M" 90 40]))
    (is (= (apply-origin {:x 100 :y 200} ["L" 80 25]) ["L" 180 225]))
    (is (= (apply-origin {:x 50 :y 70} ["H" 20]) ["H" 70]))
    (is (= (apply-origin {:x 50 :y 70} ["V" 20]) ["V" 90]))
    (is (= (apply-origin {:x 50 :y 70} ["C" 30 40 50 40 60 30]) ["C" 80 110 100 110 110 100]))
    (is (= (apply-origin {:x 50 :y 70} ["Q" 30 40 50 40]) ["Q" 80 110 100 110]))
    (is (= (apply-origin {:x 50 :y 70} ["S" 30 40 50 40]) ["S" 80 110 100 110]))
    (is (= (apply-origin {:x 50 :y 70} ["T" 30 40]) ["T" 80 110]))
    (is (= (apply-origin {:x 50 :y 70} ["A" 40 40 0 1 0 80 75]) ["A" 40 40 0 1 0 130 145]))
    ))

(deftest test-apply-scale
  (let [apply-scale #'ptt/apply-scale]
    (is (= (apply-scale nil ["M" 80 25]) ["M" 80 25]))
    (is (= (apply-scale {:x 1 :y 1} ["M" 80 25]) ["M" 80 25]))
    (is (= (apply-scale {:x 2 :y 3} ["M" 80 25]) ["M" 160 75]))
    (is (= (apply-scale {:x 2 :y 3} ["A" 40 40 0 1 0 80 75]) ["A" 80 120 0 1 0 160 225]))
    ))

(deftest test-get-transition
  (let [get-transition #'ptt/get-transition]
    (is (= (get-transition {:x 10 :y 20} nil ["M" 80 25]) [{:x 80 :y 25}]))
    (is (= (get-transition {:x 10 :y 20} nil ["L" 50 60]) [{:x 50 :y 60}]))
    (is (= (get-transition {:x 10 :y 20} nil ["H" 50]) [{:x 50 :y 20}]))
    (is (= (get-transition {:x 10 :y 20} nil ["V" 50]) [{:x 10 :y 50}]))
    (is (= (get-transition {:x 10 :y 20} nil ["C" 30 40 50 40 60 30])
           [{:bezier [{:x 30 :y 40} {:x 50 :y 40} {:x 60 :y 30}]}]))
    (is (= (get-transition {:x 10 :y 20} nil ["Q" 30 40 50 40])
           [{:bezier [{:x 30 :y 40} {:x 50 :y 40}]}]))
    (is (= (get-transition {:x 95 :y 80} ["C" 40 10 65 10 95 80] ["S" 150 150 180 80])
           [{:bezier [{:x 125 :y 150} {:x 150 :y 150} {:x 180 :y 80}]}]))
    (is (= (get-transition {:x 40 :y 10} ["M" 40 10] ["S" 150 150 180 80])
           [{:bezier [{:x 150 :y 150} {:x 150 :y 150} {:x 180 :y 80}]}]))
    (is (= (get-transition {:x 40 :y 10} ["S" 65 10 95 80] ["S" 150 150 180 80])
           [{:bezier [{:x 125 :y 150} {:x 150 :y 150} {:x 180 :y 80}]}]))
    (is (= (get-transition {:x 95 :y 80} ["Q" 40 10 65 10] ["T" 150 150])
           [{:bezier [{:x 90 :y 10} {:x 150 :y 150}]}]))
    (is (= (get-transition {:x 40 :y 10} ["M" 40 10] ["T" 150 150])
           [{:x 150 :y 150}]))
    (is (= (get-transition {:x 40 :y 10} ["T" 150 150] ["T" 150 150])
           [{:x 150 :y 150}]))                              ; <- not really correct but let it be
    (is (= (get-transition {:x 80 :y 25} nil ["A" 40 40 0 1 0 80 75])
           [{:bezier [{:x 69.3845832715486, :y 11.741348755731238} {:x 51.553475440960696, :y 6.632281187803727} {:x 35.52736115395997, :y 12.257453718120182}]}
            {:bezier [{:x 19.501246866959256, :y 17.882626248436637} {:x 8.77501000800801, :y 33.015333234477765} {:x 8.77501000800801, :y 49.99999999999999}]}
            {:bezier [{:x 8.77501000800801, :y 66.98466676552222} {:x 19.50124686695925, :y 82.11737375156335} {:x 35.52736115395997, :y 87.74254628187981}]}
            {:bezier [{:x 51.55347544096068, :y 93.36771881219627} {:x 69.38458327154859, :y 88.25865124426878} {:x 80, :y 75.00000000000001}]}]))
    ))

(deftest test-get-transitions
  (let [get-transitions #'ptt/get-transitions]
    (is (= (get-transitions
             {:duration 100}
             [["M" 80 25] ["L" 90 40] ["H" 95] ["V" 50]])
           [{:x 80 :y 25 :duration 100}
            {:x 90 :y 40 :duration 100}
            {:x 95 :y 40 :duration 100}
            {:x 95 :y 50 :duration 100}]))
    (is (= (get-transitions
             {:duration 100}
             [["M" 80 25] ["C" 30 40 50 40 60 30]])
           [{:x 80 :y 25 :duration 100}
            {:bezier [{:x 30 :y 40} {:x 50 :y 40} {:x 60 :y 30}] :duration 100}]))
    (is (= (get-transitions
             {:duration 100}
             [["M" 80 25] ["Q" 30 40 50 40]])
           [{:x 80 :y 25 :duration 100}
            {:bezier [{:x 30 :y 40} {:x 50 :y 40}] :duration 100}]))
    (is (= (get-transitions
             {:duration 100}
             [["M" 80 25] ["C" 40 10 65 10 95 80] ["S" 150 150 180 80]])
           [{:x 80 :y 25 :duration 100}
            {:bezier [{:x 40 :y 10} {:x 65 :y 10} {:x 95 :y 80}] :duration 100}
            {:bezier [{:x 125 :y 150} {:x 150 :y 150} {:x 180 :y 80}] :duration 100}]))
    (is (= (get-transitions
             {:duration 100}
             [["M" 80 25] ["Q" 40 10 65 10] ["T" 180 80]])
           [{:x 80 :y 25 :duration 100}
            {:bezier [{:x 40 :y 10} {:x 65 :y 10}] :duration 100}
            {:bezier [{:x 90 :y 10} {:x 180 :y 80}] :duration 100}]))
    ))

(deftest test-transition->path
  (let [transition->path #'ptt/transition->path]
    (is (= (transition->path {:x 260 :y 150}) ["L" 260 150]))
    (is (= (transition->path {:bezier [{:x 20 :y 20} {:x 40 :y 20} {:x 50 :y 10}]}) ["C" 20 20 40 20 50 10]))
    (is (= (transition->path {:bezier [{:x 20 :y 20} {:x 40 :y 20}]}) ["Q" 20 20 40 20]))
    ))

(deftest test-get-transitions-durations
  (let [get-transitions-durations #'ptt/get-transitions-durations]
    (is (= (get-transitions-durations
             [{:x 100 :y 0}
              {:x 100 :y 100}
              {:x 0 :y 100}]
             3
             {:x 0 :y 0})
           [1 1 1]))
    ))

(deftest test-path->transitions
  (is (= (ptt/path->transitions
           {:path     "M 80 25 A 40,40 0 1,0 80,75 M 80 10 L 80 90"
            :origin   {:x 1005 :y 585}
            :scale    {:x 2 :y 2}
            :duration 5})
         [{:x 1165, :y 635, :duration 0.9834258124954962}
          {:bezier [{:x 1143.7691665430973, :y 608.4826975114624}
                    {:x 1108.1069508819214, :y 598.2645623756075}
                    {:x 1076.05472230792, :y 609.5149074362404}],
           :duration 0.5788134618388621}
          {:bezier [{:x 1044.0024937339185, :y 620.7652524968732}
                    {:x 1022.5500200160161, :y 651.0306664689556}
                    {:x 1022.5500200160161, :y 685}],
           :duration 0.5788126561802741}
          {:bezier [{:x 1022.5500200160161, :y 718.9693335310444}
                    {:x 1044.0024937339185, :y 749.2347475031268}
                    {:x 1076.05472230792, :y 760.4850925637596}],
           :duration 0.578812835215516}
          {:bezier   [{:x 1108.1069508819214, :y 771.7354376243925}
                      {:x 1143.7691665430973, :y 761.5173024885376}
                      {:x 1165, :y 735}],
           :duration 0.5788134618388621}
          {:x 1165, :y 605, :duration 0.7626614841932022}
          {:x 1165, :y 765, :duration 0.9386602882377875}]))
  )

(deftest test-get-moves-lengths
  (is (= (ptt/get-moves-lengths "M 80 25 A 40,40 0 1,0 80,75 M 80 10 L 80 90") [83.81527307120105 65]))
  (is (= (ptt/get-moves-lengths "M 80 0 L 80 90 m 100 0") [80 100]))
  )
