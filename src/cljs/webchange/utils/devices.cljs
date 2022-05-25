(ns webchange.utils.devices)

(def devices {:android-tablet       "Android Tablet",
              :ipad                 "iPad",
              :android-mobile-phone "Android Mobile Phone",
              :iphone               "iPhone",
              :desktop-computer-pc  "Desktop Computer: PC",
              :desktop-computer-mac "Desktop Computer: Mac",
              :laptop-computer-pc   "Laptop Computer: PC",
              :laptop-computer-mac  "Laptop Computer: Mac"})

(def devices-options (map (fn [[value text]]
                            {:text  text
                             :value (clojure.core/name value)})
                          devices))
