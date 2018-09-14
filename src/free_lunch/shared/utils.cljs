(ns free-lunch.shared.utils
  (:require
   [bignumber.core :as bn]
   [cljs.core.match :refer-macros [match]]
   [district.web3-utils :as web3-utils]
   [print.foo :refer [look] :include-macros true]))

(def not-nil? (complement nil?))

(defn calculate-meme-auction-price [{:keys [:meme-auction/start-price
                                            :meme-auction/end-price
                                            :meme-auction/duration
                                            :meme-auction/started-on] :as auction} now]
  (let [seconds-passed (- now started-on)
        total-price-change (- end-price start-price)
        current-price-change (/ (* total-price-change seconds-passed) duration)]
    (if (<= duration seconds-passed)
      end-price
      (+ start-price current-price-change))))

(defn parse-uint-date [date parse-as-date?]
  (let [date (bn/number date)]
    (match [(= 0 date) parse-as-date?]
      [true _] nil
      [false true] (web3-utils/web3-time->local-date-time date)
      [false (:or nil false)] date)))
