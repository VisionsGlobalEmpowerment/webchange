(ns webchange.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [webchange.core-test]))

(doo-tests 'webchange.core-test)
