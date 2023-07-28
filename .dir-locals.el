((nil .
      ((cider-preferred-build-tool . lein)
       (cider-lein-parameters . "with-profile +cljs,+cljs-test repl :headless")
       (cider-default-cljs-repl          . custom)
       (cider-custom-cljs-repl-init-form . "(do (webchange.user/start-server) (webchange.user/cljs-repl))")
       (cider-offer-to-open-cljs-app-in-browser . nil)
       (clojure-indent-style . 'align-arguments)
       (eval . (define-clojure-indent
		 (ns '(1 (0)))
		 (cond-> 0)
		 (cond->> 0)
		 (as-> 0)
		 (reg-event-fx 0)
		 (reg-sub 0)
		 (defroutes 'defun)
		 (GET 2)
		 (POST 2)
		 (PUT 2)
		 (DELETE 2)
		 (HEAD 2)
		 (ANY 2)
		 (OPTIONS 2)
		 (PATCH 2))))))
