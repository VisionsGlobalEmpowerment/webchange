(ns webchange.ui-framework.test-page.buttons
  (:require
    [webchange.ui-framework.components.index :refer [button]]
    [webchange.ui-framework.test-page.utils :refer [group group-body group-header table]]))

(defn buttons
  []
  [group
   [group-header "Main Buttons"]
   [group-body
    [table {:columns ["default" "hover" "active" "disabled"]
            :rows ["variant: contained | size: big"]
            :data    [[[button {:variant "contained"
                                :size    "big"}
                        "Button"]
                       [button {:variant "contained"
                                :size    "big"
                                :state   "hover"}
                        "Button"]
                       [button {:variant "contained"
                                :size    "big"
                                :state   "active"}
                        "Button"]
                       [button {:variant   "contained"
                                :size      "big"
                                :disabled? true}
                        "Button"]]]}]]])
