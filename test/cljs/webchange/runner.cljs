(ns webchange.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [webchange.core-test]
              [webchange.interpreter.events-test]))

(doo-tests 'webchange.core-test
           'webchange.interpreter.events-test)
