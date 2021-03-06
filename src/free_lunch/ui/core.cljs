(ns free-lunch.ui.core
  (:require
   [cljs.spec.alpha :as s]
   [clojure.string :as str]
   [district.ui.component.router :refer [router]]
   [district.ui.graphql]
   [district.ui.notification]
   [district.ui.now]
   [district.ui.reagent-render]
   [district.ui.router]
   [district.ui.smart-contracts]
   [district.ui.web3]
   [district.ui.web3-account-balances]
   [district.ui.web3-accounts]
   [district.ui.web3-sync-now]
   [district.ui.web3-tx]
   [district.ui.web3-tx-id]
   [district.ui.web3-tx-log]
   [district.ui.window-size]
   [free-lunch.shared.graphql-schema :refer [graphql-schema]]
   [free-lunch.shared.routes :refer [routes]]
   [free-lunch.shared.smart-contracts :refer [smart-contracts]]
   [free-lunch.ui.home.page]
   [free-lunch.ui.ipfs]
   [free-lunch.ui.subs]
   [mount.core :as mount]
   [print.foo :include-macros true]
   [re-frisk.core :refer [enable-re-frisk!]]))

(def ^boolean debug? js/goog.DEBUG)

(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (enable-re-frisk!)))

(def skipped-contracts [:ds-guard :param-change-registry-db :meme-registry-db :minime-token-factory])

(defn ^:export init []
  (s/check-asserts debug?)
  (dev-setup)
  (-> (mount/with-args
        (merge {:web3 {:url "http://localhost:8549"}
                :smart-contracts {:contracts (apply dissoc smart-contracts skipped-contracts)}
                ;; :web3-balances {:contracts (select-keys smart-contracts [:DANK])}
                :web3-account-balances {:for-contracts [:ETH]}
                :web3-tx-log {:open-on-tx-hash? true
                              :tx-costs-currencies [:USD]}
                :reagent-render {:id "app"
                                 :component-var #'router}
                :router {:routes routes
                         :default-route :route/home}
                                        ; :router-google-analytics {:enabled? (not debug?)}
                :graphql {:schema graphql-schema
                          :url "http://localhost:6300/graphql"}
                :ipfs {:host "http://127.0.0.1:5001" :endpoint "/api/v0"}}))
    (mount/start)))
